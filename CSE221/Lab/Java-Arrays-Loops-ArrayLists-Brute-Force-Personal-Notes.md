# From Confusion to Clarity: Java Arrays, Loops, Lists, and Brute Force

> **Personal CSE221 Lab notes — built from the way I worked things out**
>
> These notes are not just definitions to memorize. They record the questions I had, the parts that confused me, the mental models that helped, and the moments when the idea finally made sense. When something feels confusing again, read the relevant section slowly and trace the example by hand.

## 1. The most important learning rule

I do not need to understand everything instantly. It is okay to stop at a small detail and ask why it works. A question that pops into my head—even something as basic as “What does brute force mean?”—is worth asking.

The goal is not to look like I understand. The goal is to actually understand what the code is doing.

When I explain an idea in my own words, I should check it against a concrete example. If my explanation is right, keep it. If one part is wrong, identify that exact part instead of throwing away the whole mental model.

---

## 2. Array indices: why does a loop sometimes start at 1?

Consider this array:

```java
int[] arr = {2, 4, 1, 1};
```

It has four elements. Java array positions start at **0**, not 1:

| Index | 0 | 1 | 2 | 3 |
|---|---:|---:|---:|---:|
| Value | 2 | 4 | 1 | 1 |

For an array of length 4, the valid indices are `0, 1, 2, 3`. Index `4` is outside the array, and index `-1` is also invalid.

### The confusing code

```java
for (int j = 1; j < arr.length - 1; j++) {
    if (arr[j - 1] > arr[j + 1]) {
        // swap
    }
}
```

At first, the important question was: **If `j` starts at 1, doesn't `j - 1` become 0? Is that a problem?**

No. It becomes 0, and index 0 is valid.

For an array of length 4:

- `j = 1`: compare `arr[0]` and `arr[2]`.
- `j = 2`: compare `arr[1]` and `arr[3]`.
- The loop stops before `j = 3`, because `j < arr.length - 1` means `j < 3`.

### <u>The click moment: I understood why the loop starts at 1.</u>

My own reasoning was essentially:

> “Why it started from 1? Because when comparing, if I started from 0, there is no one in -1. The index goes 0, 1, 2, 3, so start from 1. Compare 0 and 2, using `j-1` and `j+1`.”

That is the right reasoning.

- If `j = 0`, then `j - 1 = -1`, which is invalid.
- If `j = 1`, then `j - 1 = 0` and `j + 1 = 2`, both valid.
- We also must stop before the last index, because `j + 1` would otherwise go beyond the array.

So this loop is built for an element that has a **left neighbour and a right neighbour**. The loop variable `j` represents the middle position; `j-1` and `j+1` point to the positions on either side.

### Three separate things to keep apart

1. **The array** determines which indices exist.
2. **The loop** determines which values `j` takes.
3. **The expression**—such as `arr[j - 1]`—determines which array position is accessed.

The value of `j` does not force every array access to use that same index. When `j = 1`, `arr[j - 1]` is simply `arr[0]`.

### A useful warning

The code above compares the elements on either side of `j`; it skips the middle element in that comparison. That is different from comparing adjacent elements. Whether this is useful depends on the task.

---

## 3. What does a nested loop do?

Look at this structure:

```java
for (int i = 0; i < arr.length - 1; i++) {
    for (int j = 0; j < arr.length - 1; j++) {
        if (arr[j] > arr[j + 1]) {
            // swap arr[j] and arr[j + 1]
        }
    }
}
```

There are two jobs here:

- The **outer loop**, using `i`, controls how many times the inner loop gets to run.
- The **inner loop**, using `j`, does the comparisons and possible swaps.

For an array of length 4, the outer loop runs with `i = 0, 1, 2`: three times. Each time, the inner loop runs through its comparisons.

### <u>The click moment: the outer loop does not sort the array by itself.</u>

The outer loop is not the part that compares values. It arranges for the inner loop to get another pass. The inner loop is where the work happens.

One **pass** means one complete run of the inner loop from beginning to end.

If `arr.length == 4`, then `i < arr.length - 1` means `i < 3`, so the outer loop has 3 iterations. In general, for length `n`, this outer loop runs `n - 1` times when `n` is positive.

### Why compare `arr[j]` with `arr[j + 1]`?

These are neighbouring elements. If the left one is bigger than the right one, swapping them moves the bigger value one position to the right.

For example, with `[4, 2, 3]`:

1. Compare 4 and 2. Since 4 is bigger, swap → `[2, 4, 3]`.
2. Compare 4 and 3. Since 4 is bigger, swap → `[2, 3, 4]`.

This is the central idea behind **bubble sort**: repeated comparisons of adjacent elements move larger values toward the right. The outer loop repeats the process so the array can become sorted. The name is useful only because we understand the reason behind the repeated passes.

**Important:** A loop that runs repeatedly does not automatically sort anything. The comparisons, swaps, loop boundaries, and number of passes all matter.

---

## 4. Why one successful example does not prove an algorithm works

We tried the comparison `arr[j - 1] > arr[j + 1]` with an example like:

```text
[2, 4, 1, 1]
```

A particular pass can produce a result that looks sorted by coincidence. That does not prove the rule sorts every possible array.

Try this array:

```text
[3, 1, 4, 2]
```

The comparisons of the elements on either side do not necessarily change it into sorted order. It can remain unsorted.

### The lesson

When testing an algorithm, do not only choose an example that makes it look successful. Try:
- an already sorted array;
- an array sorted in reverse;
- an array with repeated values;
- an array that is mixed up, such as `[3, 1, 4, 2]`;
- a very small array.

A good example helps me trace code. Several different examples help me discover whether my reasoning is actually reliable.

---

## 5. Arrays versus ArrayLists

Both can store a sequence of values, but they behave differently.

### A normal array

```java
int[] arr = new int[4];
arr[0] = 4;
arr[1] = 3;
arr[2] = 2;
arr[3] = 1;
```

- Its length is fixed when it is created.
- Access an element with `arr[i]`.
- Change an element with `arr[i] = 5`.
- To sort an integer array, use `Arrays.sort(arr)`.

### An ArrayList

```java
ArrayList<Integer> arr = new ArrayList<>();
arr.add(4);
arr.add(3);
arr.add(2);
arr.add(1);
```

- It can grow as elements are added.
- Access an element with `arr.get(i)`.
- Change an element with `arr.set(i, 5)`.
- Add another element with `arr.add(value)`.
- To sort it, use `Collections.sort(arr)`.

For ordinary arrays and lists, index counting still begins at 0.

### What does `add()` actually mean?

`add()` is a method provided by collection types such as `ArrayList`. In an `ArrayList`, `arr.add(4)` puts 4 at the end of the list.

For example:

```java
ArrayList<Integer> arr = new ArrayList<>();
arr.add(4);
arr.add(3);
arr.add(2);
arr.add(1);

System.out.println(arr);
```

Output:

```text
[4, 3, 2, 1]
```

The values stay in the order they were added. **`add()` does not reverse or sort the list.**

If I then write:

```java
Collections.sort(arr);
System.out.println(arr);
```

the list becomes:

```text
[1, 2, 3, 4]
```

That is because `Collections.sort(arr)` sorts the list into ascending order.

Also, not every Java object uses a method called `add()`. For example, `StringBuilder` uses `append()`, and a `HashMap` uses `put()`. The method depends on the type of object.

### Which one should I use?

For learning index-based operations, swaps, and sorting, normal arrays are a good starting point because their fixed positions make the index logic clear. An `ArrayList` is useful when I want a list that can grow or shrink.

Neither is automatically better for every problem. Choose based on what the problem needs.

---

## 6. What does `ArrayList<int[]>` mean?

This declaration can look intimidating:

```java
ArrayList<int[]> moves = new ArrayList<>();
```

Read it from the inside out:

- `int[]` means an array of integers.
- `ArrayList<int[]>` means an ArrayList whose items are integer arrays.
- `new ArrayList<>()` creates an empty outer list.

Example:

```java
ArrayList<int[]> moves = new ArrayList<>();
moves.add(new int[]{2, 4});
moves.add(new int[]{1, 3});
moves.add(new int[]{3, 5});
```

Each `new int[]{...}` creates one array. The outer list stores those arrays as its items.

Think of the data as:

```text
moves
  item 0 -> [2, 4]
  item 1 -> [1, 3]
  item 2 -> [3, 5]
```

To get a value:

```java
moves.get(0)[0]  // 2
moves.get(0)[1]  // 4
moves.get(1)[0]  // 1
```

`moves.get(0)` first retrieves the array at outer-list index 0. Then `[0]` or `[1]` accesses an element inside that array.

### Printing the contents

Printing the outer list directly with `System.out.println(moves)` will not neatly show the contents of each integer array. Java arrays normally print an identity-style string, not their elements.

Instead:

```java
for (int[] move : moves) {
    System.out.println(Arrays.toString(move));
}
```

Output:

```text
[2, 4]
[1, 3]
[3, 5]
```

This uses an enhanced `for` loop: each time through the loop, `move` refers to one array in the list.

---

## 7. What does `ArrayList<ArrayList<Integer>>` mean?

This looks even more complicated:

```java
ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
```

But the idea is simple: **it is a list containing other lists**.

- The outer `ArrayList` holds the groups.
- Each inner `ArrayList<Integer>` holds integer values within one group.

For example:

```java
ArrayList<ArrayList<Integer>> arr = new ArrayList<>();

arr.add(new ArrayList<>(Arrays.asList(2, 4)));
arr.add(new ArrayList<>(Arrays.asList(1, 3)));
arr.add(new ArrayList<>(Arrays.asList(3, 5)));
```

Picture it like rows:

```text
arr
  row 0 -> [2, 4]
  row 1 -> [1, 3]
  row 2 -> [3, 5]
```

To access a value:

```java
arr.get(0)        // [2, 4]
arr.get(0).get(1) // 4
```

Read `arr.get(0).get(1)` in two steps:

1. `arr.get(0)` gets the inner list at outer index 0: `[2, 4]`.
2. `.get(1)` gets index 1 of that inner list: `4`.

The expression uses `.get()` twice because there are two levels: first the outer list, then the inner list.

### <u>The click moment: it looks complicated, but the idea is just “a list of groups.”</u>

I wondered whether a nested list was for a more complicated task. The useful answer is: the syntax looks more complicated than the idea itself. I only need a nested list when the data naturally has multiple levels.

- One sequence of numbers? A normal array or `ArrayList<Integer>) is often enough.
- Several groups of numbers, or rows and columns? A nested list may fit naturally.
- A list of pairs such as coordinates or moves? `ArrayList<int[]>` can be useful.

**Do not use nested lists just because they look advanced.** Use them when the structure of the data calls for them.

The example uses `new ArrayList<>(Arrays.asList(...))) to make each inner list modifiable. `Arrays.asList(...)` on its own gives a fixed-size list, so wrapping it in `new ArrayList<>(...)` makes it easier to add or remove elements later.

---

## 8. What does brute force mean?

**Brute force means trying possibilities in a straightforward way, often checking each candidate or every possible option until the answer is found.**

Suppose I have:

```text
[7, 2, 9, 4]
```

and need to find 9. A straightforward search checks:
1. 7 — not the answer.
2. 2 — not the answer.
3. 9 — found it.

That is a brute-force search: check each element one by one until the target is found or the list ends.

### Are loops automatically brute force?

No. A loop is just a way to repeat work. Whether an algorithm is brute force depends on the strategy it uses.

- A loop that checks every array element to find a target is a straightforward search.
- Nested loops that try every pair of elements may be brute force for a pair-checking task.
- A loop can also be part of a more efficient algorithm that avoids unnecessary work.

### Is brute force bad?

Not automatically.

- It is often the easiest way to get a correct first solution.
- It can be completely fine when the input is small.
- It can become too slow when the number of possibilities grows very large.

In competitive programming, I need to consider both correctness and the input constraints. A brute-force solution can be a great first step: first understand what must be checked, then ask whether checking all of it is fast enough.

A useful thinking path is:

**Understand the task → write a straightforward correct approach → estimate how much work it does → improve it if the input limits require that.**

There is no shame in starting with brute force. The important skill is learning when it is sufficient and when it needs improvement.

### <u>The click moment: even a basic question like “What is brute force?” belongs in my learning process.</u>

The question popped into my head while I was thinking about the concepts. I did not need to wait for a teacher or a new chapter to ask it. Knowing the name helps me recognize a common strategy in future problems, but the meaning matters more than memorizing the term.

---

## 9. A small Java input/output reminder

For programming problems, a common pattern is:

```java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in)
        );

        int T = Integer.parseInt(br.readLine());
        PrintWriter pw = new PrintWriter(System.out);

        for (int i = 0; i < T; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            // Read the values for this test case.
            // Solve the problem.
            // Print the answer using pw.println(answer).
        }

        pw.flush();
    }
}
```

This is only a template for input where the first line contains `T` and each test case can be read from one line. The exact reading code must match the problem's input format.

A few details to remember:
- `BufferedReader` reads text input.
- `StringTokenizer` splits a line into tokens; it does not read input by itself.
- `PrintWriter` lets me print answers, and I can reuse it.
- `pw.println(answer)` runs whenever execution reaches that line.
- `pw.flush()` makes sure buffered output is sent at the end.
- Use `long` rather than `int` when the constraints or calculations can exceed the `int` range.

The template is a tool, not the problem-solving logic. I should spend most of my thinking on understanding what the task asks and how to solve it.

---

## 10. The mental models I want to keep

### Arrays and indices
An array is a row of numbered boxes. The first box is index 0. The last valid index is `length - 1`. An expression such as `arr[j + 1]` means “use the box one position after `j`,” so I must make sure that box exists.

### Loops
A loop controls repetition. The loop variable changes according to the loop rules. It does not automatically determine what the body does; the code inside the body determines the actual work.

### Nested loops
The outer loop can repeat the inner loop. The inner loop performs its own comparisons or actions. A complete run of the inner loop is one pass in this example.

### Lists
An `ArrayList<Integer>` is one flexible sequence of integers. An `ArrayList<int[]>` is a list of integer arrays. An `ArrayList<ArrayList<Integer>>` is a list of integer lists. Read the type from the inside out.

### Brute force
Brute force is a straightforward strategy that checks candidates directly. It can be useful and correct; whether it is fast enough depends on how many things must be checked.

---

## 11. Questions to ask myself when code confuses me

Do not immediately try to memorize the line. Ask:

1. What data structure am I looking at: an array, a list, a list of arrays, or a list of lists?
2. What values can the loop variable take?
3. For each value, which exact index does each expression access?
4. Are those indices valid?
5. What does the code inside the loop actually do?
6. Does one pass mean one comparison, or a full run of the inner loop?
7. Am I assuming that one example proves the algorithm always works?
8. Is the algorithm checking every candidate? If so, is that amount of work okay for the input size?
9. Can I explain the idea in my own words without repeating a definition?

If I cannot answer one of these yet, that is not failure. It tells me exactly which small piece to investigate.

---

## 12. Final personal reminder

I made progress by not skipping over the details that bothered me. The important breakthroughs were not about memorizing a Java keyword; they were about separating ideas that initially looked tangled together:

- The loop variable and the array index expression are related, but they are not the same thing.
- Starting a loop at 1 can be necessary when the code needs a left neighbour.
- The outer loop repeats work; the inner loop performs the comparisons.
- A result that looks sorted once does not prove the algorithm always sorts correctly.
- `add()` adds an item; it does not sort or reverse a list.
- A list of lists sounds complex, but it is simply a way to represent groups.
- Brute force is a strategy, not an insult or automatically a bad solution.

**When I get confused again, I should return to the smallest concrete example, trace each step, and explain what happens in my own words.** That is how I turn a line of code from something I have seen into something I understand.
