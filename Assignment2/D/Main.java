import java.io.*;

public class Main {
    static final long MOD = 1_000_000_007L;

    public static void main(String[] args) throws IOException {
        DataInputStream in = new DataInputStream(System.in);
        StringBuilder sb = new StringBuilder();

        int T = nextInt(in);

        while (T-- > 0) {
            long a = nextLong(in) % MOD;
            long b = nextLong(in) % MOD;
            long c = nextLong(in) % MOD;
            long d = nextLong(in) % MOD;
            long x = nextLong(in);

            // identity
            long r0=1,r1=0,r2=0,r3=1;

            while (x > 0) {
                if ((x & 1) == 1) {
                    long t0 = (r0*a + r1*c) % MOD;
                    long t1 = (r0*b + r1*d) % MOD;
                    long t2 = (r2*a + r3*c) % MOD;
                    long t3 = (r2*b + r3*d) % MOD;
                    r0=t0; r1=t1; r2=t2; r3=t3;
                }
                long t0 = (a*a + b*c) % MOD;
                long t1 = (a*b + b*d) % MOD;
                long t2 = (c*a + d*c) % MOD;
                long t3 = (c*b + d*d) % MOD;
                a=t0; b=t1; c=t2; d=t3;
                x >>= 1;
            }

            sb.append(r0).append(' ').append(r1).append('\n');
            sb.append(r2).append(' ').append(r3).append('\n');
        }

        System.out.print(sb);
    }

    private static int nextInt(DataInputStream in) throws IOException {
        int ret = 0;
        int b = in.read();
        while (b < '0' || b > '9') b = in.read();
        while (b >= '0' && b <= '9') { ret = ret * 10 + (b - '0'); b = in.read(); }
        return ret;
    }

    private static long nextLong(DataInputStream in) throws IOException {
        long ret = 0;
        int b = in.read();
        while (b < '0' || b > '9') b = in.read();
        while (b >= '0' && b <= '9') { ret = ret * 10 + (b - '0'); b = in.read(); }
        return ret;
    }
}