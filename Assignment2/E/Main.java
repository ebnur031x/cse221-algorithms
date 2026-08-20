import java.io.*;

public class Main {
    static long MOD;

    static long power(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    // returns (a^1 + a^2 + ... + a^n) % MOD
    static long geoSum(long a, long n) {
        if (n == 0) return 0;
        if (n == 1) return a % MOD;
        if (n % 2 == 0) {
            // S(n) = S(n/2) * (1 + a^(n/2))
            long half = geoSum(a, n / 2);
            return half * ((1 + power(a, n / 2, MOD)) % MOD) % MOD;
        } else {
            // S(n) = S(n-1) + a^n
            return (geoSum(a, n - 1) + power(a, n, MOD)) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        StringBuilder sb = new StringBuilder();

        in.nextToken(); int T = (int) in.nval;

        while (T-- > 0) {
            in.nextToken(); long a = (long) in.nval;
            in.nextToken(); long n = (long) in.nval;
            in.nextToken(); MOD = (long) in.nval;

            sb.append(geoSum(a, n)).append('\n');
        }

        System.out.print(sb);

    }
}