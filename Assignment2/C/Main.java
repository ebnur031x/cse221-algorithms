import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());
        long mod = 107;
        
        pw.println(power(a, b, mod));
        pw.flush();
    }
    
    static long power(long a, long b, long mod) {
        long result = 1;         
        a = a % mod;            
        
        while (b > 0) {
            if (b % 2 == 1) {    
                result = (result * a) % mod;  
            }
            a = (a * a) % mod; 
            b = b / 2;        
        }
        
        return result;
    }
}