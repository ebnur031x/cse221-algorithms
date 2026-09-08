# CSE221 LCS — My Understanding & Confusion-Clearing Notes

> Personal notes from the LCS learning/confusion-clearing process. The goal is to remember **what the table means, why each move happens, and how filling differs from traceback** — not to memorize a formula blindly.

---

## 1. The core picture

LCS = **Longest Common Subsequence**.

A subsequence keeps the original order of characters, but characters do **not** have to be next to each other.

For the DP table, the most important mental model is:

```text
Read's one character stays fixed for a row
        ↓
checks against Ref's characters one-by-one across the columns
        ↓
then move to the next Read character / next row
```

Example:

```text
Read = ACGTA
Ref  = AGCTG

Row 1: Read A → compare with Ref A, G, C, T, G
Row 2: Read C → compare with Ref A, G, C, T, G
Row 3: Read G → compare with Ref A, G, C, T, G
...
```

This cleared an important earlier misunderstanding: **LCS is NOT just comparing the characters at the same positions** like `A vs A`, `C vs G`, `G vs C`, etc.

---

# 2. The LCS DP table

For two strings of lengths `m` and `n`, use an:

```text
(m + 1) × (n + 1)
```

table.

The extra row and column represent the **empty prefix**.

For our practice:

```text
Read = ACGTA     (length 5)
Ref  = AGCTG     (length 5)
```

so the table is:

```text
6 × 6
```

Setup:

```text
        Ø   A   G   C   T   G
    Ø   0   0   0   0   0   0
    A   0
    C   0
    G   0
    T   0
    A   0
```

`Ø` means **empty / nothing**.

---

# 3. VERY IMPORTANT: indexing for LCS

For this DP table, think of the strings using **1-based positions**:

```text
Read[1] = A
Read[2] = C
Read[3] = G
Read[4] = T
Read[5] = A

Ref[1] = A
Ref[2] = G
Ref[3] = C
Ref[4] = T
Ref[5] = G
```

This is a mental model for the LCS DP table. It is separate from Java arrays, which normally use 0-based indexing.

---

# 4. What exactly does `dp[i][j]` mean?

This was one of the biggest sources of confusion, so lock this in:

> **`dp[i][j]` = the LCS length of the first `i` characters of Read and the first `j` characters of Ref.**

It does **NOT** mean:

> "the LCS of only Read[i] and Ref[j]."

The current characters are used to decide the recurrence, but the **cell stores the answer for the whole pair of prefixes**.

Example:

```text
Read = ACGTA
Ref  = AGCTG
```

`dp[2][3]` means:

```text
Read prefix = AC
Ref prefix  = AGC
```

The current characters being compared are:

```text
Read[2] = C
Ref[3]  = C
```

So there are two levels of thinking:

```text
CURRENT CHECK:
Read[i] vs Ref[j]

CELL MEANING:
LCS length of Read[1..i] vs Ref[1..j]
```

That distinction is crucial.

---

# 5. The first row and first column

### 🔒 LOCK THIS IN

When filling the standard LCS DP table:

> **The first row and first column are filled with 0s.**

Why?

Because they represent an empty prefix.

If one side is empty, the LCS length is 0.

```text
empty vs anything → 0
anything vs empty → 0
```

Therefore, after initializing them:

> **Start calculating from `dp[1][1]`.**

This is the first actual character-vs-character cell.

---

# 6. `dp[1][1]` — the first real cell

For our strings:

```text
Read[1] = A
Ref[1]  = A
```

They match.

So:

```text
dp[1][1] = dp[0][0] + 1
          = 0 + 1
          = 1
```

Important clarification:

`dp[0][0]` is **not an original string character**. It is the DP table's empty-prefix / empty-prefix cell.

The logic is:

```text
A == A
 ↓
use diagonal previous answer
 ↓
dp[0][0] + 1
```

---

# 7. MATCH: what happens when the characters match?

If:

```text
Read[i] == Ref[j]
```

then:

```text
dp[i][j] = dp[i-1][j-1] + 1
```

### Mental model

The current characters match, so we can include that matching character.

We therefore look at the smaller prefix where **both current characters have been removed**:

```text
[i][j]
  ↖
[i-1][j-1]
```

Then add the current matching character:

```text
diagonal answer + 1
```

### 🔒 Direction

From `[i][j]`, diagonal means:

> **UP + LEFT = `↖`**

Not up-right.

The indices change from:

```text
[i][j] → [i-1][j-1]
```

---

# 8. A match does NOT mean the whole strings are identical

This was another important confusion.

When we write:

```text
Read[i] == Ref[j]
```

we are checking **only the two current characters**.

We are NOT asking:

> "Are the two whole strings identical?"

Example:

```text
Read = ACGTA
Ref  = AGCTG
```

At `dp[2][3]`:

```text
Read[2] = C
Ref[3]  = C
```

They match even though the full strings are completely different.

So remember:

```text
character check = local
DP value         = broader prefix problem
```

---

# 9. MISMATCH: what happens when characters do NOT match?

If:

```text
Read[i] != Ref[j]
```

then:

```text
dp[i][j] = max(dp[i-1][j], dp[i][j-1])
```

There is **no diagonal +1** here.

Instead, we consider two possibilities:

```text
UP:   dp[i-1][j]
LEFT: dp[i][j-1]
```

Then take the larger value.

### Physical movement

From `[i][j]`:

```text
UP   = [i-1][j]
LEFT = [i][j-1]
```

So:

```text
mismatch → check UP and LEFT → take MAX
```

---

# 10. Why UP and LEFT?

When the current characters do not match, they cannot both be used together as the matching pair for this cell.

So we try leaving one behind:

```text
UP   → leave Read[i] behind
LEFT → leave Ref[j] behind
```

The two neighboring cells already contain the answers to those smaller prefix problems.

There is no hidden "advanced data" telling us what to choose.

> **The DP table itself is the evidence.**

If:

```text
UP   = 4
LEFT = 3
```

then:

```text
max(4, 3) = 4
```

so the current cell gets `4`.

---

# 11. Concrete cells from the practice

## `dp[1][1]`

```text
Read[1] = A
Ref[1]  = A
```

Match → diagonal + 1:

```text
dp[1][1] = dp[0][0] + 1 = 1
```

---

## `dp[1][2]`

```text
Read[1] = A
Ref[2]  = G
```

Mismatch.

Check:

```text
UP   = dp[0][2] = 0
LEFT = dp[1][1] = 1
```

Take max:

```text
max(0, 1) = 1
```

Therefore:

```text
dp[1][2] = 1
```

---

## `dp[1][3]`

```text
Read[1] = A
Ref[3]  = C
```

Mismatch.

Check:

```text
UP   = dp[0][3] = 0
LEFT = dp[1][2] = 1
```

Therefore:

```text
dp[1][3] = max(0, 1) = 1
```

This reinforced the row mental model: **Read's A stays fixed while it checks Ref's A, G, C, T, G across row 1.**

---

## `dp[2][3]` — a useful tricky match

```text
Read[2] = C
Ref[3]  = C
```

They match.

Therefore use the diagonal:

```text
dp[2][3] = dp[1][2] + 1
```

The important thing is not to accidentally use UP or LEFT just because they are nearby. **Match → diagonal.**

---

# 12. The table is filled differently from traceback

This distinction became very important during practice.

### Filling the DP table

You calculate **all cells** in bottom-up order:

```text
TOP-LEFT → → →
           ↓
           → → →
           ↓
           → → →
```

You start with the initialized first row/column, then calculate:

```text
dp[1][1]
→ dp[1][2]
→ dp[1][3]
→ ...
→ next row
→ ...
→ bottom-right
```

### Traceback

After the table is completely filled, you start from the **bottom-right** and follow **one path backward**.

```text
Filling:
🟦 🟦 🟦 🟦 🟦
🟦 🟦 🟦 🟦 🟦
🟦 🟦 🟦 🟦 🟦
→ calculate EVERYTHING

Traceback:
🟦 → 🟦 → 🟦 → 🟦 → dp[0][0]
→ only ONE PATH
```

So:

> **Filling visits/calculates the whole table. Traceback visits only the cells along the selected path.**

You will **not visit most cells during traceback**.

---

# 13. Why does traceback start at the bottom-right?

After the whole table is filled:

```text
dp[m][n]
```

contains the LCS length for the **entire two strings**.

So the bottom-right cell represents the final full problem.

To recover the actual LCS, we work backward from that final answer toward the empty-prefix cell:

```text
dp[m][n]
   ↓
smaller relevant cell
   ↓
smaller relevant cell
   ↓
...
   ↓
dp[0][0]
```

The bottom-right cell gives the **length**. Traceback recovers the **actual sequence**.

---

# 14. Filling vs traceback: the cleanest distinction

### Filling

Question:

> **What is the LCS length for every pair of prefixes?**

Direction:

```text
small → large
TOP-LEFT → BOTTOM-RIGHT
```

You calculate every cell.

### Traceback

Question:

> **Which characters/decisions produced the final LCS?**

Direction:

```text
large → small
BOTTOM-RIGHT → TOP-LEFT
```

You follow only one relevant path.

### 🔒 Lock-in sentence

> **Bottom-up filling builds the answers forward; traceback walks backward through the answers to recover the LCS.**

---

# 15. The bottom-right cell

After filling the table:

```text
dp[m][n] = LCS length of the complete strings
```

For example, if the bottom-right cell contains `4`, that means:

```text
The LCS length = 4
```

It does **not** mean the cell itself contains the actual four-character sequence.

To find the actual LCS, use traceback.

---

# 16. The two different things happening in a cell

A useful way to avoid confusion is to separate:

### Step 1 — Compare current characters

```text
Read[i] vs Ref[j]
```

### Step 2 — Use the appropriate recurrence

If match:

```text
diagonal + 1
```

If mismatch:

```text
max(UP, LEFT)
```

### Step 3 — Store the resulting LCS length

```text
dp[i][j] = result
```

So do not mentally collapse these into one thing.

```text
CURRENT CHARACTERS
       ↓
match or mismatch?
       ↓
choose recurrence
       ↓
calculate value
       ↓
store in dp[i][j]
```

---

# 17. A compact decision rule

For every cell `[i][j]`, ask:

```text
1. Which characters am I comparing?
   Read[i] and Ref[j]

2. Do they match?

   YES → diagonal + 1

   NO  → check UP and LEFT
          → take MAX
```

That is the core of filling an LCS DP table.

---

# 18. The most important mental model from the practice

The biggest conceptual correction was this:

### Wrong mental model

```text
Read[1] ↔ Ref[1]
Read[2] ↔ Ref[2]
Read[3] ↔ Ref[3]
...
```

as if LCS were simply comparing positions.

### Correct mental model

```text
              Ref →
          A   G   C   T   G
Read A    ↔   ↔   ↔   ↔   ↔
     C    ↔   ↔   ↔   ↔   ↔
     G    ↔   ↔   ↔   ↔   ↔
     T    ↔   ↔   ↔   ↔   ↔
     A    ↔   ↔   ↔   ↔   ↔
```

Every table cell represents one row-character/column-character comparison, while the **stored value represents the best LCS length for the corresponding prefixes**.

This mental picture makes the table much less mysterious.

---

# 19. My lock-in notes

## WHEN FILLING

1. **First row and first column = 0. Must. 🔒**
2. **Therefore start calculating from `dp[1][1]`.**
3. **Match → diagonal UP-LEFT (`↖`) + 1.**
4. **Mismatch → check UP and LEFT → take MAX.**

## INDEXING

5. **For the LCS DP table, think of string positions as 1-based.**
6. `dp[i][j]` means **first `i` characters of Read vs first `j` characters of Ref**.
7. The character comparison is only `Read[i]` vs `Ref[j]`.

## TRACEBACK

8. **Start at the bottom-right.**
9. **Follow ONE path backward.**
10. **Traceback does not visit most cells.**
11. **Filling = calculate everything. Traceback = follow one path.**

---

# 20. One-line exam memory

> **Initialize 0s → start at `dp[1][1]` → compare Read[i] with Ref[j] → match = ↖ + 1 → mismatch = max(↑, ←) → bottom-right gives LCS length → traceback follows one path backward to recover the sequence.**

---

# 21. If I get confused again

Do not jump straight to the formula.

Ask these questions in order:

```text
WHERE AM I?
→ What does dp[i][j] represent?

WHAT AM I COMPARING?
→ Read[i] vs Ref[j]

DO THEY MATCH?
→ YES or NO

IF YES:
→ diagonal UP-LEFT + 1

IF NO:
→ UP vs LEFT → MAX
```

And remember the biggest distinction:

> **The characters decide the move; the DP cell stores the answer for the prefixes.**

---

## Scope of this note

This note captures the LCS concepts and confusion-clearing developed in the practice so far: the DP table structure, 1-based table indexing, prefix meaning of `dp[i][j]`, initialization, match/mismatch recurrence, row-vs-column comparison, bottom-up filling, bottom-right interpretation, and traceback direction/path.

It is intentionally written around the mental models and corrections that made the topic click during practice, rather than as a generic textbook chapter.
