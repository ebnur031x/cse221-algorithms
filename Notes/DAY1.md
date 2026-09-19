Now we at the o(N) part, how things work

already know something about it, now just making it more Concrete. 
so,
x = 10;       // 1 operation
y = x + 5;    // 1 operation

T(n)=2
Since 2 is constant, we are gonna write like, 
T(n)=o(1).

So, in <EXAM> if they ask, 
if, n gets bigger,<X=10> does the number of operations get bigger? does it has to do more job?
No, because, it just <OneLine> of code, it Executes exactly one time. so yes o(1).

BUT,one line ≠ automatically O(1). because thats "(for (int i = 0; i < n; i++) x++;)" ONE line too

Now to the bigger picture, the <LOOP> thing

for (i = 0; i < n; i++) {
    x = x + 1;
}

so, here is it just a simple operation? NO, why because it does just run one time. let see what it does
We count how many times each thing happens:

Thing	                    How many times?	                        Why?
i=0                             1                           Happens once at the beginning
i < n                          n+1                  Checked every round plus one final failed check
i++                             n                       once every sucessful <TRUE> round
x=x+1;                          n                       because, its inside the loop,only when <TRUE>too


now lets add them up:

T(n)= 1+(n+1)+n+n= n+2+n+n= 3n+2
BUT, the main idea, 3 and 2, is less significant than n, in this kind of case, we are always going to take the big o(n).

[Suppose:

T(n) = 3n + 2

If n = 10 → 32
If n = 1,000 → 3,002
If n = 1,000,000 → 3,000,002

As n gets huge, the +2 becomes tiny compared with 3n.

The 3 still matters to the actual amount of work, but it doesn't change the type of growth. 3n still grows linearly]

NEXT
1. i++ / i += constant → O(n)
2. i *= 2 / i /= 2 → O(log n)
3. Nested loops → multiply their iteration counts

so, 
<PATTERN 1> is already, I know, its that i++ thing, leads to o(n)
<PATTERN 2> now
Logarithm 

for (int i = 1; i <= n; i *= 2) {// say n=16
    print(i);
}

so, because of i*=2, then values it takes is, 1 → 2 → 4 → 8 → 16
Notice it doesn't go through every number. It doubles each time.

so, how many time can u double, before reaching that condtion? 
That number is <log₂(n) → therefore O(log n).>
Now, lets drive it from the loop itself


1. i=1, starting from, it doubles, each iteration, so
1, 2, 4, 8, 16

Iteration
1           2^0=1
2           2^1=2
3           2^2=4
4           2^3=8
5           2^4=16
6           2^k=2^k

2. loop continues while, i≤n
after k iterations, i=2^k. that leads to 2K=n, and stoppin the loop
so, now, k = log2​n, because of 2 base, from i*2
THEREFORE, the number of iteration is, k=log2n, since constant not gonna count, so its <O(log n)>

Logarithmic loop

Rule:

i *= c   OR   i /= c

where (c>1)

→ O(log n)

Also, multiplying by something like 0.5 is effectively division by 2, so that's logarithmic too.

× 0.5 → ÷ 2 → keeps halving
÷ 0.5 → × 2 → keeps doubling


NOW PATTENR 3
for (i = 1; i <= n; i++)
    for (j = 1; j <= n; j *= 2)
        print(i, j);

outer loop, n times, and for each n, inter loop, n times too, but doubling
so, is it, n*log 2 n

So total:

n × log n = O(n log n)

<Rule:

Sequential → add
Nested → multiply



SOLVING <SPRING2025> ques 1 a

 int b=0;
 for (int i = n; i > 0; i -= 2) {
    for (int j = n; j > 0; j--) {
        b += i * j;
    }
}

so, here i very important distinction;
in the first loop, -2, makes the n=10 to n=5. but it did not change the groth rate, like
n = 10 → 5
n = 100 → 50
n = 1000 → 500

see, 5, 50, 500, as n grows the growth rate is same, with its with a <CONSTANT> fixed number
"separating actual <runtime> from <growth_rate>"

so, 
<Iterations: n/2>*******
<Complexity: O(n)>****** DONTk k flkmgvflml;


i