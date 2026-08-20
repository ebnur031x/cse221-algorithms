import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[] degree = new int[n + 1];

        if (m > 0) {
            int[] u = new int[m], v = new int[m];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < m; i++) u[i] = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < m; i++) v[i] = Integer.parseInt(st.nextToken());

            for (int i = 0; i < m; i++) {
                degree[u[i]]++;
                degree[v[i]]++;
            }
        }

        int oddCount = 0;
        for (int i = 1; i <= n; i++)
            if (degree[i] % 2 != 0)
                oddCount++;

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        pw.println((oddCount == 0 || oddCount == 2) ? "YES" : "NO");
        pw.flush();
    }
}