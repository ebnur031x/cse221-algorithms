# Reverse Sorting — Personal Understanding Notes

> **Course:** CSE221 Lab  
> **Assignment:** A1 — Problem E, “Reverse Sorting”  
> **Language:** Java  
> **Purpose of these notes:** Rebuild the idea from the exact points that were confusing, rather than memorizing code.

---

## 1. What the problem is asking

We are given an array of numbers. We may perform one special operation:

- Choose three consecutive positions.
- Reverse the order of those three elements.
- Repeat as many times as needed.
- Print `YES` and the operations if the array can become sorted; otherwise print `NO`.

The operation is restricted: we cannot reverse just any number of elements. It must be exactly three consecutive elements.

The output also matters. We cannot only print the final sorted array. We must remember which operations we performed so we can print them afterward.

## 2. The first important click: what does reversing three elements really do?

Imagine three consecutive elements:

```text
Before:  [A, B, C]
After:   [C, B, A]
```

The first and third elements exchange places. The middle element stays in the same position.

So, for understanding this problem, reversing three elements has the same effect on the array as swapping the elements two positions apart:

```text
Index:    0   1   2
Before:   A   B   C
After:    C   B   A
          ^       ^
          exchange
```

**Important:** The operation still is a reversal of a three-element subarray. “Swap the endpoints” is just a simpler way to understand its effect.

### Why the distance of two positions matters

If an element is at index 0, this operation can move it to index 2, then perhaps to index 4, and so on. It cannot move it directly to index 1 or 3.

Using zero-based indexes:

- Even indexes: `0, 2, 4, 6, ...`
- Odd indexes: `1, 3, 5, 7, ...`

An element can move within its own group, but not from one group to the other.

<u>**The moment it clicked:** I did not need to imagine every possible reversal separately. I could look at what the operation actually changes: the two endpoints move, and they are two indexes apart. That immediately revealed the even-index and odd-index groups.</u>

## 3. The parity idea — and why it matters

“Parity” is just a short word for whether a number is even or odd.

The array indexes are important here, not just the values stored at those indexes.

Example:

```text
Index:   0   1   2   3   4
Array:   5   2   1   4   3
```

The values currently at even indexes are:

```text
Indexes: 0   2   4
Values:  5   1   3
```

The values currently at odd indexes are:

```text
Indexes: 1   3
Values:  2   4
```

A length-three reversal can rearrange the values inside each group, but it cannot exchange a value from the even-index group with a value from the odd-index group.

For instance, a value at index 0 can travel to index 2 or 4, but it cannot travel to index 3. Those indexes have different parity.

<u>**The moment it clicked:** I had first seen the condition `currentIndex % 2 != targetIndex % 2` and wondered why I could not find it in the full code. The condition was an explanation of the rule, not a line used by the final implementation. The actual code follows the same rule by processing the two groups separately.</u>

### What does `%` mean?

In Java, `%` gives the remainder after division.

- `0 % 2` is `0` — even.
- `2 % 2` is `0` — even.
- `4 % 2` is `0` — even.
- `1 % 2` is `1` — odd.
- `3 % 2` is `1` — odd.

So this conceptual condition:

```java
if (currentIndex % 2 != targetIndex % 2)
```

means: “If the current index and the target index belong to different groups, the element cannot get there using this operation.”

This is a mental model, not a complete solution by itself. In particular, when duplicate values exist, deciding which occurrence of a value is its “target” can require care.

## 4. Why we sort the two groups separately

The solution uses a straightforward approach:

1. Sort the values at even indexes among themselves.
2. Sort the values at odd indexes among themselves.
3. Check whether the entire array is now in non-decreasing order.
4. If it is, print `YES` and the recorded operations. If not, print `NO`.

“Non-decreasing” means each value is less than or equal to the value after it. Equal neighbouring values are allowed.

Consider the example again:

```text
Original array:
[5, 2, 1, 4, 3]

Even-index values:
[5, 1, 3]  ->  [1, 3, 5]

Odd-index values:
[2, 4]     ->  [2, 4]
```

Put those sorted group values back into their original index positions:

```text
Index:  0  1  2  3  4
Array:  1  2  3  4  5
```

The full array is sorted, so this example can be solved.

Now imagine that after sorting the two groups, the full array still has a larger value before a smaller one. The allowed operation cannot fix that by moving values between parity groups. Therefore the answer is `NO`.

### Why not just stop when the array repeats?

During the earlier discussion, I wondered how to know when to stop applying operations. There are multiple possible choices of three-element subarray, and each choice can lead to a different next array. Seeing one repeated state or one unhelpful move is not, by itself, proof that sorting is impossible.

The parity observation gives a stronger way to reason: the operation never transfers an element between the even and odd index groups. Sorting each group and then checking the whole array tests whether the allowed movement is enough.

## 5. Java concept: what is an `ArrayList`?

Before understanding the full code, I needed to understand this line:

```java
ArrayList<int[]> moves = new ArrayList<>();
```

### A normal array

For example:

```java
int[] a = new int[5];
```

This creates an array with five positions. Its length is fixed when we create it.

### An ArrayList

An `ArrayList` is a list that can grow as we add items. We do not have to decide the number of items in advance.

That is useful here because, before we start solving the array, we do not know how many operations will be needed. There might be no operations, a few operations, or many operations.

The variable called `moves` is our notebook: whenever we perform an operation, we record it in the list.

### What does `ArrayList<int[]>` mean?

Read it from the inside out:

- `int[]` means an array of integers.
- `ArrayList<int[]>` means an ArrayList whose items are integer arrays.
- Each integer array stores the two numbers needed to describe one operation.

For example:

```java
ArrayList<int[]> moves = new ArrayList<>();

moves.add(new int[]{2, 4});
moves.add(new int[]{1, 3});
moves.add(new int[]{3, 5});
```

The list now contains three items:

```text
moves
  item 0 -> [2, 4]
  item 1 -> [1, 3]
  item 2 -> [3, 5]
```

Each pair represents one operation, using the problem's 1-based indexes.

- `new int[]{2, 4}` creates an integer array holding the numbers 2 and 4.
- `moves.add(...)` adds that pair to the list.
- `moves.size()` tells us how many items (operations) are currently in the list. In this example, it returns `3`, not `6`. There are three list items, each containing two integers.

<u>**The moment it clicked:** I did not need to know the number of moves beforehand. The ArrayList grows as the algorithm records each operation. At the end, `moves.size()` tells the program how many operations to print.</u>

## 6. Understanding the loops slowly

Here is the important part of the solution:

```java
for (int p = 0; p < 2; p++) {
    for (int i = p; i + 2 < n; i += 2) {
        for (int j = p; j + 2 < n; j += 2) {
            if (a[j] > a[j + 2]) {
                int temp = a[j];
                a[j] = a[j + 2];
                a[j + 2] = temp;

                moves.add(new int[]{j + 1, j + 3});
            }
        }
    }
}
```

Do not try to understand every loop at once. Start with the outer loop:

```java
for (int p = 0; p < 2; p++)
```

It runs twice:

- First, `p = 0`: work with even indexes.
- Then, `p = 1`: work with odd indexes.

That is how the code separates the two parity groups. It does not need to explicitly write a condition comparing `currentIndex % 2` with `targetIndex % 2`.

The next two loops make repeated passes through one group. The variable `j` takes indexes two apart:

- When `p = 0`, `j` is `0, 2, 4, 6, ...`.
- When `p = 1`, `j` is `1, 3, 5, 7, ...`.

The comparison:

```java
if (a[j] > a[j + 2])
```

asks whether the earlier value is larger than the value two positions later. If so, the code exchanges those two values.

The variable `i` controls how many passes are made. The inner loop starts from `p` each time, so each pass checks the group from its beginning. Repeating the passes is a simple bubble-sort-style way to order that group.

## 7. Why does the code swap two values instead of reversing three values?

The code does this:

```java
int temp = a[j];
a[j] = a[j + 2];
a[j + 2] = temp;
```

This swaps the values at positions `j` and `j + 2`. The value at position `j + 1` does not change.

That produces the same final array as reversing the three consecutive elements at positions `j`, `j + 1`, and `j + 2`, because the middle element stays where it is during a reversal.

The code then records the operation:

```java
moves.add(new int[]{j + 1, j + 3});
```

Java array indexes start at 0, but the problem's output uses indexes starting at 1. So:

- Java index `j` becomes output index `j + 1`.
- Java index `j + 2` becomes output index `j + 3`.

For example, the Java positions 0 through 2 correspond to the problem's positions 1 through 3. The operation is printed as `1 3`.

The program updates the array as if the reversal has happened, and saves the corresponding operation. Applying the saved operations to the original array has the same effect and reaches the same final arrangement.

## 8. How does the program decide YES or NO?

After sorting both parity groups, the code checks neighbouring values:

```java
for (int i = 0; i < n - 1; i++) {
    if (a[i] > a[i + 1]) {
        pw.println("NO");
        pw.flush();
        return;
    }
}
```

It checks pairs:

- `a[0]` and `a[1]`
- `a[1]` and `a[2]`
- `a[2]` and `a[3]`
- and so on.

If any earlier value is greater than the next value, the whole array is not sorted. The program prints `NO` and returns, so it stops there.

If the loop finishes without finding such a pair, the array is non-decreasing. The program prints:

```text
YES
number of operations
each operation on its own line
```

For a case that is already sorted, there may be zero operations. That is fine: the output can be `YES` followed by `0`.

## 9. Edge cases worth remembering

- **N = 1:** The array is already sorted. Print `YES` and `0`.
- **N = 2:** No length-three operation fits. If the two values are already sorted, print `YES` and `0`; otherwise print `NO`.
- **Already sorted array:** No operation is needed.
- **Duplicate values:** Equal values are allowed in a non-decreasing array. The check uses `>`, not `>=`, so equal neighbours do not cause `NO`.

## 10. A clean mental model to keep

When I return to this problem, I should remember the reasoning in this order:

1. **Understand the operation:** reversing three elements swaps the endpoints; the middle stays.
2. **Notice the restriction:** endpoints are two indexes apart.
3. **Discover parity:** even-index values stay in even indexes, and odd-index values stay in odd indexes.
4. **Sort each group separately:** compare values two positions apart and record each swap as a valid length-three reversal.
5. **Check the whole array:** if every neighbouring pair is in order, print `YES`; otherwise print `NO`.
6. **Remember the operations:** use an `ArrayList<int[]>` because the number of moves is not known beforehand.

<u>**My biggest learning lesson:** I was not failing to understand one giant piece of code. I was missing a few smaller ideas underneath it: what the operation changes, why indexes two apart matter, and how an ArrayList remembers moves. Once those ideas are clear, the loops and output become much easier to read.</u>

---

## 11. Final reference: the main Java pieces

| Code | Meaning |
|---|---|
| `int[] a = new int[n];` | Store the input array. |
| `ArrayList<int[]> moves = new ArrayList<>();` | Create a growable list to remember operations. |
| `p = 0` | Process even indexes. |
| `p = 1` | Process odd indexes. |
| `j += 2` | Move to the next index in the same parity group. |
| `a[j] > a[j + 2]` | Check whether these two values need exchanging. |
| `moves.add(new int[]{j + 1, j + 3});` | Record the operation using 1-based indexes. |
| `moves.size()` | Count the recorded operations. |
| `a[i] > a[i + 1]` | Detect that the whole array is not sorted. |

These notes are about understanding the solution, not memorizing it. The goal is to be able to explain why every major step exists before trying to reproduce the code.
