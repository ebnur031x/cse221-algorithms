import java.io.*;
import java.util.*;

public class Main {
    static int[] bit;
    static int sz;

    static void update(int i) {
        for (i++; i <= sz; i += i & (-i))
            bit[i]++;
    }

    static long query(int i) {
        long s = 0;
        for (i++; i > 0; i -= i & (-i))
            s += bit[i];
        return s;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = Long.parseLong(st.nextToken());

        // coordinate compression on original values
        long[] sorted = a.clone();
        Arrays.sort(sorted);
        // sorted[k] is the k-th smallest value
        sz = n;
        bit = new int[sz + 1];

        // compress: get rank of a value in sorted array
        // rank = index in sorted array (0-based)

        long count = 0;

        // scan right to left
        for (int i = n - 1; i >= 0; i--) {
            // count already-inserted j's where A[i] > A[j]^2
            // i.e. A[j]^2 < A[i]
            // i.e. find all inserted values v where v^2 < A[i]
            // v^2 < A[i] means:
            //   if A[i] <= 0: no v satisfies (v^2 >= 0 > A[i] impossible... wait v^2 >= 0 always)
            //   if A[i] > 0: |v| < sqrt(A[i])

            if (a[i] > 0) {
                // find largest index in sorted where sorted[k]^2 < a[i]
                // binary search for rightmost v where v*v < a[i]
                int lo = 0, hi = n - 1, best = -1;
                while (lo <= hi) {
                    int mid = (lo + hi) / 2;
                    if (sorted[mid] * sorted[mid] < a[i]) {
                        best = mid;
                        lo = mid + 1;
                    } else {
                        hi = mid - 1;
                    }
                }
                if (best >= 0)
                    count += query(best);
            }
            // if a[i] <= 0: v^2 < a[i] is impossible, add 0

            // insert a[i] into BIT at its rank
            int rank = Arrays.binarySearch(sorted, a[i]);
            update(rank);
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        pw.println(count);
        pw.flush();
    }
}