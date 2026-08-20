import java.io.*;
import java.util.*;

public class Main {
    static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

       
        List<List<Integer>> neighbors = new ArrayList<>();
        for (int i = 0; i <= n; i++) neighbors.add(new ArrayList<>());

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i != j && gcd(i, j) == 1) {
                    neighbors.get(i).add(j);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            List<Integer> list = neighbors.get(x);
            if (k > list.size())
                sb.append(-1).append('\n');
            else
                sb.append(list.get(k - 1)).append('\n');
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        pw.print(sb);
        pw.flush();
    }
}