import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        StringTokenizer st = null;

        int t = Integer.parseInt(br.readLine().trim());

        while (t-- > 0) {
            int n = Integer.parseInt(br.readLine().trim());
            st = new StringTokenizer(br.readLine());
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
            }

            boolean isOk = true;
            for (int i = 0; i < n - 1; i++) {
                
                if (arr[i] > arr[i + 1]) {
                    isOk = false;
                    break;
                }
            }

            pw.println(isOk ? "YES" : "NO");
        }

        pw.flush();
    }
}