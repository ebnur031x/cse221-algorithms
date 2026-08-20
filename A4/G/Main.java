import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int[] x = new int[k], y = new int[k];
        Set<Long> positions = new HashSet<>();

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            x[i] = Integer.parseInt(st.nextToken());
            y[i] = Integer.parseInt(st.nextToken());
            positions.add((long) x[i] * 2000 + y[i]);
        }

        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

        boolean found = false;
        for (int i = 0; i < k && !found; i++) {
            for (int d = 0; d < 8; d++) {
                int nx = x[i] + dx[d];
                int ny = y[i] + dy[d];
                if (nx >= 1 && nx <= n && ny >= 1 && ny <= m) {
                    long code = (long) nx * 2000 + ny;
                    if (positions.contains(code)) {
                        found = true;
                        break;
                    }
                }
            }
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        pw.println(found ? "YES" : "NO");
        pw.flush();
    }
}