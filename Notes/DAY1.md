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



SOLVING <SPRING2025> ques 1 **A**

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
<Iterations: n/2>*******
<Complexity: O(n)>******
*SO DONT MIX UP*

1st nested loop, 
outer, starts n, end i>0, i-=2, so, growth is n times, SO complexctiy = o(n)
inner, starts n, end j>0, j--, again, growth is n times, o(n)

nested total, o(n)*o(n)=o(n^2)

<SecondNestedLoop>
for (int k = 1; k < n; k *= 4) {
    for (int m = 0; m < n; m += 3) {
        if (m == 10) {
        break;
        }
    }
}


so OUTER, K < n TIMES, but k=*4, so its a log growth, means, o(log n)
now, inner, m < n, and m+=3, so growth is n times, so its, <o(n)>
now, nested total, o(N)*0 (log n)= o(n log n).
<NOTE> Multiplication, JUST multiply them, not choose the leading one**

SO, now total time complexctiy:
total from nested one + total from nested two
O(n² + n log n) → O(n²); because, **DOMINANT** growth rate is, o(n^2)

Quick rule:

Sequential → add: O(n)+O(log n)=O(n)
Nested → multiply: O(n)×O(logn)=O(nlogn)

[~~~~~~~~~~~~~~~~~~~~]

1. What is a **Recurrence**?

[So,jumped into class or topic 9]

[~~~~~~~~~~~~~~~~~~~~]
**RAM MODEL**
Each basic operation takes constant time, O(1).
like, x=10;, y=5;, sum=x+y; every basic operation is O(1), there its O(1), this is not like every **Execution**
<FOR_EXAMPLE>
for (int i = 0; i < n; i++) {
    x = x + 1;
}// involves multiple basic operation.
Each individual <operation> is O(1).

But the whole thing <executes> n times:

O(1) × n = O(n)

**BEST and WORST**

Best case = minimum work possible.
Worst case = maximum work possible.

[5,6,7,8,9]
if 5 found in the first, then it will be o(1), and if not found or last found <max_work> so its gonna be o(N)

THATS the main and core idea of BEST and WORST FOR NOW.

**Search algorithms**

<BINARY>
->Works on a <sorted> array
->Always start <MID> point.
->[mid = low + (high - low) / 2;] ([INDEX] not VALUE)<-<IMPORTANT>
mid = 10 + (70-10)/2; 


Array:  [10, 20, 30, 40, 50, 60, 70]
         ↑            ↑           ↑
        low          mid         high
         0            3           6

like, target is 70, start from 40, 40<70, so left side discard totally, including [40], now from 50 to 70, find the mid? then cut again....
so therefore, its → **O(log n)**
complexcity thinking n-n/2-n/4-n/8-n/16.....1
CORE QUESITON IS: <How many times can I divide n by 2 until I reach 1?>
That number is log₂ n, therefore O(log n).

NOW ONE SIMUALTION
A = [3, 7, 12, 18, 25, 31, 40, 46]
target = 7
mid= 0+(7-0)/2=3.5, java integer division gives: 3, and **3RD** index is 18, so 18 is mid
now, 7<18, from 18-46, will be discarded.

now 3 7 12
mid= 0+(2-0)/2=1, so index 1, so 7
7==7, so therefore we found it.

NOW, if target is not **PRESENT**
[3, 7, 12, 18, 25, 31, 40, 46]
target = 20

mid= 18; 18<20; [3,7,12,18] discared, now left with [25, 31, 40, 46]. now new <MID> 31. so, 20<31. right side [31,40,46] discared, now left with only, [25], NOW, 20<25, [25] also discard, now [EMPTY]-> NOT FOUND!!

**MODIFIED BINARY**

-Works on unsroted (with some condition), and sorted too, if the <target is not given>

-Go LEFT → keep mid → high = mid
-Go RIGHT → discard mid → low = mid + 1
-Can have multple peak, as the only condition is adj left and right has to smaller just.  
-[1,5,4,3,2]
1<5- so no 1, is not peak
1<5>4, yes, 5 is peak
5>4, no
4>3<2, no
3<2, and 2 is also not a peak. 

<Peak = bigger than the number(s) directly beside it> thats the main idea for modified binary search 

**Fitness & Feedback**

1. Check mid
2. Get feedback from the comparison
3. Use that feedback to decide which half to throw away

The comparison at mid gives information (feedback) that tells us which direction to search.
Why this makes Binary Search efficient? 
Each feedback lets safely discard about half the remaining elements.

so, thats the whole thing about it, like <feedback> wise

**LOWER BOUND**


MAIN  A[i]≥TARGET


1. the array has to be sorted
2. its a binary search at its core, but with bit more to go
3. For lower bound, not just scanning from left to right.
HOW IT WORKS,
lets say, i have these:
A = [1, 2, 3, 3, 4, 5], target = 3

Normal binary search-> Mid = 2 -> A[2]=3; 3==3, so found. <STOP_HERE>; You found 3 → stop.
NOW, 
Lower Bound
mid = 2 → A[2] = 3
3 ≥ 3 → valid candidate → save index 2.
<BUT> Could there be another valid index further LEFT? maybe, so <SAVE> what u have and move on with rules.
<RULES> 
A[mid] ≥ target → valid → possibility on LEFT → go left
A[mid] < target → invalid → must go RIGHT
THE FINAL Simulation. 

Started with

[1,2,3,3,4,5]
-> finding mid[2]=3 -> valid → save index 2, search left (A[mid] ≥ target)
-> [1,2]
-> mid = 1, value 2 < 3 → go right
-> but right is [], nothing's left to search
-> finish with saved index 2


**OK lets see some edge cases**
<That IS Lower Bound>
[Case_1] Target doesn't exist, but a valid bound exists

A = [1, 2, 4, 6, 8], target = 5
<We want_the_first_A[i]≥5> so, A[i]≥5 very important
*Mid=[2]=4. is 4<5 -> go <RIGHT>
[6,8] now both of them are valild, because of A[i]≥5
<BUT> lower bound is only ONE position: the first valid position → 6.

so always target is to find the CLOSET value to target, and make that lower bound. 

[Case_2] — Target is bigger than everything

[1,2,4,6,8], target = 10

Every value is < 10.

→ No valid index exists.

Lower bound = no position / not found.

These are the two important edge cases.

**COMPLEXCITY** of lower bound
Each step cuts the search space roughly in half? what does it Indicate? 
its not searching the whole array, so its not n==n, therefore, its O(log N). 

<same Big-O concept I already learned; binary search is just a very clear example of logarithmic growth.>


**UPPER BOUND**

MAIN A[i] > target 

[case 1 duplicates**]
A = [1, 2, 3, 3, 3, 5, 7]
Target = 3
we want FIRST value [strictly] greater than  3, not equal and obv not less

[Lower bound: A[mid] == target → save mid → go left]

Upper bound: A[mid] == target → go right (no saving the mid if equal)

from our example, mid = [3]=3, target, 3==3, in upper we go, right without saving

now, we have [3,5,7], mid= 5, 5>3, <MATCH> 5 is greater than 3, so 5 is our first vaild match. now my only check is, if i can find something which is smaller than 5 but greater than 3. because that would be updated version. because its a sorted array, i need to check my left for smaller elements. so the moment i saved, 5 as a vaild index, my array accessible array was [3,5,7]. 5 is chossen and i decied to go left, so now, i have only [3]. NOW, obv, it has only one element so mid, is 3 and compare is gonna be 3<3, which is not vaild. <The search range is now empty> and our final answer is **5**, which we saved from index [5].

Therefore the answer is:

Upper bound = index 5, value 5.


CASE 2: target is ABSENT 

Array: [1, 2, 4, 5, 7]
Target: 3

low = 0
high = 4
mid = 0+(4-0)/2=4
check, mid with target
4>3, yes the mid is valid one yet. save it. (VAILD means: A[i]>target)
NEXT, gonna go left because maybe there is something smaller than <4 and bigger than 3. so, left array 
[1,2] and same repeat the process finding mid and then compareing. 



**Ternary Search**

2 Mid points                                 1      2       3    (sections)   
M1 and M2, basically 2 cuts in the array, ------1------2--------

Formula(s) to find the mids: 
<M1 = L + R-L/3; M2= L + 2(R-L)/3>// Think: 1/3 from the left, 2/3 from the left.

M1 → compare → M2 → compare → choose 1/3 → repeat.

    A = [2, 5, 8, 12, 16, 20, 25, 30, 35]; TARGET = 25
    L=0; H=8;

 M1 = 0+(8-0)/3=2.6666=2 
 M2 = 0+2(8-0)/3=5.333=5
 
 Equality check. M1 with target, 
 is "M1==TARGET" -> 8==25? No; M2 now, 20==25? no. FOUND NEITHER 

 COMPARE NOW: smaller or larger. 
 Target 25, M1=8, so 25>8. too small, M1 is too small → target is not in the left section → move forward

M2, compare M2 with target; 20<25, <ALSO SMALL> target is not in the left or middle section → it must be in the right section. (FIRST M1 AND M2 IS DONE)

UPDATED array: old one was (0-5) <NOW> (6-8) 
🔁

[25, 30, 35]; L=6; R=8
M1= 6+(8-6)/3=6.666=[6]=25
M2= 6+2(8-6)/3 =7.333=[7]=30

Equality check; 25==25? yes saved index 6. STOP RETURN

Therefore, target is found at index 6.


Now which one faster. 

Binary vs Ternary
lets say u have n elements:
Binary: 
1. Find the mid
2. split into 2 sections, 1 compare 
3. throw half away. keep 1/2 

Ternary:
1. Find mids, M1 AND M2
2. Split into 3 sections, 2 compare
3. throw 2/3 away, keep 1/3

So at first glance, ternary looks better. But there's a catch
Binary needs fewer comparisons per step.
In <BIG O> they are same, O(log n), but Count the <comparisons per step × number of steps>


Take N = 27 elements.

Binary

Each step keeps half:

27 → 13.5 → 6.75 → 3.4 → 1

But each step has roughly 1 comparison.

5 × 1 = 5 comparisons

Ternary

Each step keeps one-third:

27 → 9 → 3 → 1

But each step can require 2 comparisons.


So:

Binary: more steps × fewer comparisons
Ternary: fewer steps × more comparisons

understanding the algorithmic trade-off, counting comparisons is the main useful metric here.

SO, cutting the array more, not gonna make the process faster. 

Also that connect us another thing which is, "<Quaternary>"

SAME, More sections means more cut points → more comparisons per step.


That gives the same trade-off we just learned:

More splitting ≠ automatically faster, because the extra comparisons can cancel out the benefit.













