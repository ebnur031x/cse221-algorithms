# CSE221 0/1 Knapsack — My Notes

> Personal confusion-clearance notes. The goal is to understand the logic so I can rebuild it in an XM/final, not memorize a formula.

---

## 1. The problem idea

We have items. Each item has:

- **weight** = how much space it uses
- **value** = how much benefit/profit it gives
- **capacity** = the maximum total weight the bag can hold

For **0/1 Knapsack**, each item can be chosen only once:

```text
Take it OR don't take it.
No fractions.
```

### Important: weight vs capacity

Do not mix these up.

- **Weight** belongs to an item.
- **Capacity** belongs to the bag/state.

If C has weight `1` and the current bag capacity is `3`, then taking C leaves:

```text
3 - 1 = 2
```

The subtraction is always:

```text
capacity - current item's weight
```

---

# 2. What does "it fits" mean?

An item **fits** when its weight is less than or equal to the capacity currently available:

```text
item.weight <= capacity
```

It does NOT mean that the item has a high value.

There is no separate "minimum value" threshold.

Example:

```text
capacity = 3
B weight = 3
```

B fits because:

```text
3 <= 3
```

But if:

```text
capacity = 2
B weight = 3
```

B does not fit because:

```text
3 > 2
```

---

# 3. Our concrete example

```text
Item     Weight    Value
A           2        3
B           3        4
C           1        2

Capacity = 3
```

The capacity `3` is given by the problem. We then create DP columns for every smaller capacity:

```text
w = 0, 1, 2, 3
```

These are **subproblem capacities**, not four different original bag capacities.

---

# 4. State

Our state is:

```text
dp[i][w]
```

meaning:

> Maximum value achievable using the first `i` items with capacity `w`.

So:

```text
dp[2][3]
```

means:

> Using the first 2 items (A and B), what maximum value can I get if the bag can hold 3?

The item values themselves are fixed input values. For example:

```text
A = value 3
B = value 4
C = value 2
```

The DP cells are the values we calculate.

---

# 5. Why do we have capacities 0,1,2,3 if the question says capacity = 3?

Because DP solves **smaller versions of the same problem** too.

The original target is:

```text
dp[3][3]
```

But while solving it, we need states such as:

```text
dp[2][3]
dp[2][2]
dp[1][2]
```

So the table contains all capacities from `0` to `3`.

Think:

```text
Original problem: capacity 3
       ↓
Solve smaller capacities too
       ↓
0, 1, 2, 3
```

---

# 6. Filling the table — bottom-up

Our table is:

```text
                 capacity
              0    1    2    3
0 items       0    0    0    0
A             0    0    3    3
A + B         0    0    3    4
A + B + C     0    2    3    5
```

For the first row (A), we check A against each capacity:

```text
A weight = 2, value = 3
```

Therefore:

```text
capacity 0 → A doesn't fit → 0
capacity 1 → A doesn't fit → 0
capacity 2 → A fits → 3
capacity 3 → A fits → 3
```

So A's row is:

```text
0  0  3  3
```

Then we do the same structure for B, then C.

---

# 7. The recurrence: TAKE vs SKIP

This was the main part that needed to be understood slowly.

For a current item, there are two **hypothetical scenarios**:

```text
1. Skip the current item.
2. Take the current item.
```

We are NOT choosing one manually at the beginning.

The algorithm considers both possibilities, calculates what each would give, and then uses `max()` to choose the better result.

### If the current item fits:

```text
skip = dp[i-1][w]

take = current item's value + dp[i-1][w-current item's weight]

answer = max(skip, take)
```

The phrase to remember is:

> **Current item's value + best value from previous items using whatever capacity is left.**

---

# 8. Concrete recurrence example: dp[2][3]

Current item = B.

B:

```text
weight = 3
value = 4
```

Current state:

```text
dp[2][3]
```

means we are considering A+B with capacity 3.

### Scenario 1 — skip B

If B is skipped, we simply use the previous items with the same capacity:

```text
dp[1][3] = 3
```

### Scenario 2 — take B

B fits because:

```text
3 >= 3
```

Take B, so B contributes its fixed value:

```text
4
```

B uses all 3 capacity:

```text
3 - 3 = 0
```

So we ask what value can come from previous items using the remaining capacity:

```text
dp[1][0] = 0
```

Therefore:

```text
take = 4 + dp[1][0]
     = 4 + 0
     = 4
```

Now compare:

```text
max(skip, take)
= max(3, 4)
= 4
```

Therefore:

```text
dp[2][3] = 4
```

### Important

We did NOT take both `dp[1][3]` and `dp[1][0]`.

They represent two separate hypothetical scenarios:

```text
                 dp[2][3]
                /        \
          skip B         take B
             ↓              ↓
         dp[1][3]      4 + dp[1][0]
```

Then `max()` decides which scenario is better.

---

# 9. Concrete recurrence example: dp[3][3]

Current item = C.

```text
C weight = 1
C value = 2
```

State:

```text
dp[3][3]
```

### Skip C

```text
dp[2][3] = 4
```

### Take C

C fits because:

```text
1 <= 3
```

Remaining capacity:

```text
3 - 1 = 2
```

So:

```text
take = 2 + dp[2][2]
```

From the table:

```text
dp[2][2] = 3
```

Therefore:

```text
take = 2 + 3 = 5
```

Compare:

```text
max(4, 5) = 5
```

So:

```text
dp[3][3] = 5
```

---

# 10. What does "previous" mean?

In:

```text
dp[i-1][...]
```

"previous" means **one fewer item**.

For example:

```text
current = C → i = 3
previous = A+B → i-1 = 2
```

It does not mean we randomly jump several rows backward.

---

# 11. Base cases

For 0/1 Knapsack, two important base-case situations are:

```text
i = 0  → no items available → value 0
w = 0  → no capacity available → value 0
```

So:

```text
dp[0][w] = 0
dp[i][0] = 0
```

If either the number of available items is zero or the capacity is zero, the maximum value is zero.

These cells are easy to overlook when focusing on the interesting part of the table, but they are part of the DP foundation.

---

# 12. Full 0/1 Knapsack table for our example

```text
                 w = 0   w = 1   w = 2   w = 3

0 items            0       0       0       0
A                  0       0       3       3
A + B              0       0       3       4
A + B + C          0       2       3       5
```

The final answer is:

```text
dp[3][3] = 5
```

Meaning:

> Maximum value using A, B, C with capacity 3 is 5.

One optimal selection is:

```text
A + C
weight = 2 + 1 = 3
value  = 3 + 2 = 5
```

---

# 13. Bottom-Up vs Top-Down

These are **two ways to calculate the same DP problem**.

The underlying state and take/skip recurrence can be the same.

## Bottom-Up

Usually iterative.

We start from the base cases and calculate larger states:

```text
base cases
   ↓
A row
   ↓
A+B row
   ↓
A+B+C row
   ↓
final state dp[3][3]
```

We normally use loops to fill the table.

Mental model:

> **I walk/build through the table.**

---

## Top-Down / Recursive DP

We start by **asking for the final state**:

```text
dp[3][3]
```

We do NOT already know the final answer.

We are saying:

> "I need the answer for `dp[3][3]`. What smaller answers do I need to calculate it?"

Because the current item is C, the recurrence creates two recursive subproblems:

```text
skip C → dp[2][3]

take C → 2 + dp[2][2]
```

So:

```text
dp[3][3]
       /        \
  dp[2][3]    2 + dp[2][2]
```

Then those smaller states can themselves split into smaller states.

This is a **repeated structure**, not a one-time split.

Mental model:

> **Ask for final → recursively break into smaller states → reach base cases → answers return upward.**

---

# 14. Top-Down is not traceback

This was an important source of confusion.

### Bottom-Up

Calculate:

```text
base → → → final
```

### Traceback

After the table is already calculated, start at the final cell and move backward to determine **which items were chosen**:

```text
final → → → beginning
```

Traceback is a separate step after table calculation.

### Top-Down

Calculate recursively:

```text
final state
   ↓
smaller states
   ↓
base cases
   ↑
answers return
```

So:

> **Bottom-Up = calculate forward**
>
> **Traceback = walk backward to discover choices**
>
> **Top-Down = calculate backward recursively**

They may look similar because traceback and top-down both start at the final state, but they do different jobs.

---

# 15. The "physical movement" vs recursive movement distinction

In bottom-up, we can imagine literally visiting table cells using loops:

```text
for i = 1 ... n
    for w = 0 ... capacity
        calculate dp[i][w]
```

In top-down, there is no need to walk the whole table in order.

A recursive function creates calls such as:

```text
solve(3,3)
   ↓
solve(2,3)      solve(2,2)
   ↓                 ↓
smaller states    smaller states
   ↓                 ↓
base cases
```

So:

> **Bottom-Up = iterative/table movement.**
>
> **Top-Down = recursive function calls lead to the smaller states.**

---

# 16. What happens at a Top-Down base case?

The base case does **not** perform another `max()`.

When a recursive path reaches something like:

```text
dp[0][2]
```

there are no items left, so it simply returns:

```text
0
```

That answer goes back to the state that called it.

Then the previous state can finish its own take/skip comparison.

The important repeated pattern is:

```text
non-base state
    ↓
create take + skip possibilities
    ↓
solve smaller states
    ↓
answers return
    ↓
max(take, skip)
    ↓
return answer
```

This happens **at every relevant non-base state**.

A base case simply returns its known answer.

---

# 17. The biggest Top-Down mental model

Think of every non-base state as running the same little machine:

```text
STATE
  ↓
Does current item fit?
  ↓
If yes: consider TAKE and SKIP
  ↓
TAKE → current value + smaller state with leftover capacity
SKIP → smaller state with same capacity
  ↓
Both answers return
  ↓
MAX chooses the better one
  ↓
Return answer to caller
```

Then the same machine runs again for the smaller states.

That is why it is **recursive** and why the structure repeats.

---

# 18. A very important correction about "final value"

For 0/1 Knapsack:

```text
dp[n][W]
```

contains the **maximum total value** possible.

But do NOT memorize:

> "The final DP value is always max."

Different DP problems ask for different things.

Examples:

- Knapsack → maximum value
- LCS → length of the longest common subsequence
- Coin Change → depending on the version, minimum coins or number of ways

General rule:

> **The final DP state contains whatever the problem asks us to find.**

---

# 19. How to approach an XM question

When a question gives an item table like:

```text
item | weight/credit | value
```

and a capacity, use this flow:

### Step 1 — Identify the inputs

Find:

```text
item weights
item values
capacity W
```

### Step 2 — Define the state

For 0/1 Knapsack:

```text
dp[i][w] = maximum value using first i items with capacity w
```

### Step 3 — Set up the table

Use:

```text
rows = number of items + 1
columns = capacities 0 ... W
```

### Step 4 — Base cases

```text
dp[0][w] = 0
dp[i][0] = 0
```

### Step 5 — Fill row by row

For each item and each capacity:

1. Check whether the current item fits.
2. If it does not fit, skip it automatically:

```text
dp[i][w] = dp[i-1][w]
```

3. If it fits, consider both:

```text
skip = dp[i-1][w]

take = value[i] + dp[i-1][w-weight[i]]

 dp[i][w] = max(skip, take)
```

### Step 6 — Final answer

```text
dp[n][W]
```

### Step 7 — If asked for selected items, traceback

Start at the final cell and work backward through the completed table.

---

# 20. Traceback — the rules learned

Suppose we are at:

```text
dp[i][w]
```

Compare the current cell with the cell directly above:

```text
dp[i-1][w]
```

### If current > above

The current item contributed to the better value, so we treat the current item as **taken**.

Then subtract the current item's weight from the current capacity:

```text
w = w - weight[i]
```

and move diagonally to:

```text
dp[i-1][new w]
```

The diagonal movement comes from the subtraction of the current item's weight.

### If current = above

The current item was not necessary to improve the value. We can skip it and move straight up:

```text
dp[i][w] → dp[i-1][w]
```

For our exam-style traceback, using the "equal → skip" convention keeps the process deterministic.

### If current < above

That should not happen in a correctly filled 0/1 Knapsack table, because adding another available item cannot make the maximum achievable value worse.

So:

> **current < above → something is wrong with the table/calculation.**

---

# 21. Why traceback subtraction is always capacity - weight

If the current item was taken, it consumed some of the capacity.

So:

```text
remaining capacity
= current capacity - current item's weight
```

Example:

```text
current capacity = 2
current item weight = 2

remaining = 2 - 2 = 0
```

Both `2`s have different meanings:

- first `2` = current capacity
- second `2` = current item's weight

The weight is **not** taken from an item above or below. It belongs to the current item being traced.

---

# 22. Concrete traceback of our final answer

We have:

```text
                 0   1   2   3
0 items          0   0   0   0
A                0   0   3   3
A+B              0   0   3   4
A+B+C            0   2   3   5
```

Start:

```text
dp[3][3] = 5
```

Compare with above:

```text
dp[2][3] = 4
```

Since:

```text
5 > 4
```

C was taken.

C weight = 1, so:

```text
3 - 1 = 2
```

Move diagonally:

```text
dp[3][3] → dp[2][2]
```

Now:

```text
dp[2][2] = 3
```

Compare with above:

```text
dp[1][2] = 3
```

They are equal, so under our convention we skip B and move up:

```text
dp[2][2] → dp[1][2]
```

Now:

```text
dp[1][2] = 3
```

Compare with:

```text
dp[0][2] = 0
```

Since:

```text
3 > 0
```

A was taken.

A weight = 2:

```text
2 - 2 = 0
```

Move to:

```text
dp[0][0]
```

Base case reached.

Selected items:

```text
A + C
```

Total:

```text
weight = 2 + 1 = 3
value  = 3 + 2 = 5
```

---

# 23. What I was initially mixing up

These were the important confusions cleared during the learning process:

### Confusion 1 — W means both weight and capacity

Clarification:

```text
weight → belongs to item
capacity → belongs to bag/state
```

Use different mental labels even if a textbook uses `w` as the capacity index.

### Confusion 2 — "fit" means high value

No.

Fit means:

```text
item weight <= available capacity
```

Value only matters when comparing the take and skip results.

### Confusion 3 — Do we choose take or skip ourselves?

No.

They are two hypothetical scenarios. The algorithm calculates both and uses `max()`.

### Confusion 4 — Is `dp[2][3]` and `dp[2][2]` both being taken?

No.

They are two branches:

```text
skip current item → dp[2][3]

take current item → current value + dp[2][2]
```

Only the better branch contributes to the final state value.

### Confusion 5 — Is top-down just traceback?

No.

Traceback **discovers choices from an already-calculated table**.

Top-down **calculates the answers recursively**.

### Confusion 6 — Does the base case happen only once?

No.

The base case can be reached by multiple recursive branches. Every time a branch reaches a base case, it returns its known answer.

### Confusion 7 — Does `max()` happen only once?

No.

Every relevant non-base recursive state performs its own take/skip comparison.

This repeated structure was the key to understanding Top-Down.

---

# 24. The final mental picture

## Bottom-Up

```text
BASE
  ↓
small states
  ↓
bigger states
  ↓
final state
```

## Traceback

```text
final state
  ↓
walk backward through completed table
  ↓
identify chosen items
```

## Top-Down

```text
FINAL STATE REQUEST
        ↓
   take / skip
      ↙    ↘
 smaller   smaller
 states     states
    ↓         ↓
  repeat   repeat
      \     /
       BASE CASES
          ↑
    answers return
          ↑
       max(...)
          ↑
      final answer
```

### One-line memory

> **Bottom-Up calculates from base → final; Traceback finds choices from final → beginning; Top-Down calculates from final → base recursively, then returns answers upward.**

---

# 25. Reusable 0/1 Knapsack checklist

Before solving an exam problem:

```text
1. What is the capacity W?
2. What is each item's weight?
3. What is each item's value?
4. Define dp[i][w].
5. Make rows for items and columns 0...W.
6. Set dp[0][w] = 0 and dp[i][0] = 0.
7. For each cell, ask: does current item fit?
8. If no → copy above.
9. If yes → calculate TAKE and SKIP.
10. TAKE = current value + previous answer at leftover capacity.
11. SKIP = previous answer at same capacity.
12. max(TAKE, SKIP).
13. Final answer = dp[n][W].
14. If asked which items → traceback.
```

The most important logical sentence:

> **For the current item: if it fits, imagine both possibilities — take it or skip it — calculate both, and keep the better result.**

And for Top-Down:

> **Start by asking for the final state, recursively ask for the smaller states needed by the take/skip recurrence, stop at base cases, and let the answers return upward.**
