# Binary Search — My Understanding Notes

## 1. Core Binary Search

### Mental model
Binary Search is not just "cut the array in half."

It is:

**Check MID → get feedback → discard the impossible half → repeat.**

For normal Binary Search:
- target > A[mid] → go RIGHT
- target < A[mid] → go LEFT
- target == A[mid] → FOUND → stop

### Why O(log n)?
The remaining search space keeps shrinking:

n → n/2 → n/4 → n/8 → ... → 1

So the number of checks is about log₂ n.

### Important condition
Standard Binary Search needs the relevant array/order to be sorted.

---

## 2. Modified Binary Search — Peak Search

### Core idea
A modified Binary Search can use a different feedback rule.

Example:
[2, 5, 9, 12, 15, 7, 3]

- mid = 12
- 12 < 15 → slope is going RIGHT → discard LEFT
- Remaining: [15, 7, 3]
- New mid = 7
- 7 < 15 → peak is LEFT
- Remaining: [15]
- 15 is the peak

### The big connection
Normal Binary Search:
> compare MID with target

Peak Search:
> compare MID with neighbor

Both use:
> **check → feedback → discard half → new mid**

Peak Search does not require the whole array to be sorted.

---

# 3. Fitness / Feedback Function

### What it means
The comparison at MID gives us **feedback** about which side can still contain the answer.

For normal Binary Search:
- target > mid → RIGHT
- target < mid → LEFT
- target == mid → FOUND

### AHH moment
> Binary Search is not blindly cutting the array. The **comparison gives information** that makes one half safe to eliminate.

That same idea appears in modified Binary Search problems.

---

# 4. Lower Bound

## Definition
For a sorted array and target:

> **Lower Bound = first index i such that A[i] ≥ target.**

The target itself does **not** have to exist.

### Example
A = [1, 2, 3, 3, 4, 5], target = 3

Condition:

A[i] ≥ 3

First valid index = **2**, value = 3.

### The key difference from normal Binary Search

Normal Binary Search:
> find target → STOP

Lower Bound:
> find a valid position → **SAVE it, but DON'T STOP**

Then search LEFT for an earlier valid position.

### Rules
- A[mid] ≥ target → **valid candidate**
  - save mid
  - go LEFT
- A[mid] < target → invalid
  - go RIGHT

### Why go LEFT after finding a valid candidate?
We want the **FIRST** valid position.

If mid is valid, there might be another valid position earlier.

So:
> **valid → save → search left for a better/earlier candidate**

### AHH moment
The phrase "lower" does **not** mean simply "go left because lower = left."

The real reason is:
> We want the **first valid index**, so after finding a valid candidate, the left side contains the possibility of an even earlier valid candidate.

---

## Lower Bound — Target Absent

Example:

A = [1, 2, 4, 6, 8], target = 5

We are NOT trying to find 5.

We are looking for:
> first value ≥ 5

So:

1  2  4 | 6  8
          ↑

6 is the first value that crosses the threshold.

Therefore lower bound = **index 3**, value 6.

### Important AHH moment
The target is a **threshold**, not necessarily a value that must exist.

All values satisfying A[i] ≥ 5 are valid candidates:
- 6 ✓
- 8 ✓

But lower bound is only **one** position:
> the **first** valid one → 6.

### Candidate idea
If we find 6, save it.

Then ask:
> "Is there an earlier position that is also ≥ 5?"

If the left side has only 1, 2, 4, then no.

So the saved 6 remains the answer.

---

## Lower Bound — No Valid Value

Example:

[1,2,4,6,8], target = 10

Every value is < 10.

So no index satisfies:
A[i] ≥ 10

→ **No lower bound exists.**

---

# 5. Upper Bound

## Definition
For a sorted array and target:

> **Upper Bound = first index i such that A[i] > target.**

The important word is **strictly greater**.

### Example
A = [1,2,3,3,3,5,7], target = 3

Valid values must satisfy:

A[i] > 3

So:
- 3 ✗
- 3 ✗
- 3 ✗
- 5 ✓
- 7 ✓

Upper bound = **index 5**, value 5.

---

## Upper Bound Rules

- A[mid] > target → **valid**
  - save mid
  - go LEFT
- A[mid] ≤ target → invalid
  - go RIGHT

### AHH moment: Why does equality go RIGHT?
Because Upper Bound wants:

A[i] > target

If:
A[mid] == target

that element is **not valid**.

So we must move RIGHT, where larger values may exist.

### Very important comparison

**Lower Bound**
A[mid] ≥ target

**Upper Bound**
A[mid] > target

That single difference changes what counts as a valid candidate.

---

# 6. Lower vs Upper — Final Mental Model

Suppose target = 3.

### Lower Bound
Find the first place where we **reach 3 or pass it**:

A[i] ≥ 3

### Upper Bound
Find the first place where we **strictly pass 3**:

A[i] > 3

For:
[1,2,3,3,3,5,7]

- Lower Bound → first ≥ 3 → index **2**
- Upper Bound → first > 3 → index **5**

### One-line memory trick
> **LOWER: allow equality.**
>
> **UPPER: equality is NOT enough.**

---

# 7. Search-Range Detail

After processing MID, do **not** process that same MID again.

For both bounds:

### If MID is valid
Save it, then search **left of MID**.

### If MID is invalid
Discard it, then search **right of MID**.

So the next search range excludes the processed MID in both cases.

---

# 8. What I Actually Need to Remember for Exam

## Normal Binary Search
MID compared with target.

## Modified Binary Search
MID compared using whatever feedback the problem gives.

## Lower Bound
**First A[i] ≥ target**

- valid → save + LEFT
- invalid → RIGHT

## Upper Bound
**First A[i] > target**

- valid → save + LEFT
- invalid → RIGHT

## The common pattern
**Check MID → classify valid/invalid → move accordingly → keep candidate if needed.**

---

# 9. My Main Confusions — Now Cleared

### "Why not stop when I find the target?"
Because bounds are looking for a **position**, not merely any occurrence.

### "Why go LEFT after finding a valid value?"
Because we want the **first** valid position.

### "Why can lower bound return 6 when target is 5?"
Because lower bound does not require the target to exist. It wants the first value satisfying **≥ 5**.

### "Why don't we save 8 too?"
Because 8 is valid, but it is later than 6. Lower/upper bound wants the **first** valid position.

### "Why does equality go RIGHT for Upper Bound?"
Because Upper Bound requires **strictly greater** than the target.

### The biggest AHH
> **Bounds are about finding the boundary between invalid and valid positions.**

Lower Bound:
< target | ≥ target

Upper Bound:
≤ target | > target
