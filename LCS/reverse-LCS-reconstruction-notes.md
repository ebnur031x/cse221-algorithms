# CSE221 — Reverse LCS Reconstruction

## The thing I finally understood

This note is not just a formula sheet. It records the confusion I had while solving the reverse-LCS problem and the mental model that finally made it click.

The problem is **reverse reconstruction**:

- `Y` is known: `1208`
- the completed LCS DP table is known
- `X` is hidden
- the job is to reconstruct a valid `X` from the table and `Y`

For this problem, **rows are Y and columns are X**. So `[i][j]` means the LCS length for the relevant prefixes ending at `Y[i]` and `X[j]`.

The reconstructed answer is:

> **X = 8020**

---

# 1. The biggest mental shift: this is reverse reconstruction

Normally in LCS:

> Know `X` + know `Y` → calculate the DP table.

Here it is reversed:

> Know `Y` + know the completed DP table → infer the hidden `X`.

**Infer** simply means: figure out the hidden information from the clues we have.

The DP values are not just final numbers. They are **clues about what must have happened**.

---

# 2. What does a DP cell actually tell me?

If a cell is `dp[i][j] = 1`, it means:

> The LCS of those two prefixes has length 1.

It does **not automatically mean the two current characters match**.

I must test two possibilities:

### Case 1 — Match
If the current characters match:

`dp[i][j] = dp[i-1][j-1] + 1`

So I check the diagonal.

### Case 2 — Mismatch
If the current characters do not match:

`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

So I check UP and LEFT.

Then I compare the result with the **actual value already written in the table**.

My useful mental rule became:

> **Guess a case → calculate what that case would produce → compare with the actual cell value → accept or reject.**

---

# 3. When do I take a character?

This was another important confusion.

### If MATCH is confirmed
A match proves:

`X[j] = Y[i]`

So **take that character** and record it for `X[j]`.

Then move **diagonally**.

### If MISMATCH is confirmed
No character is learned from that cell.

So:

> **Mismatch → take no character → move UP or LEFT.**

The direction is determined by which neighboring value produced the current cell value.

---

# 4. Zero cells: I still have to inspect both cases

This was a major point of confusion.

If a cell contains `0`, I do **not** skip the match/mismatch test.

For example, at `[1][2] = 0`:

### Mismatch
UP = `0`

LEFT = `0`

`max(0, 0) = 0`

This agrees with the actual cell → **mismatch is possible**.

### Match
Diagonal `[0][1] = 0`

`0 + 1 = 1`

But the actual cell is `0` → **match is impossible**.

Therefore mismatch is confirmed.

The lesson:

> **Even a zero cell must be inspected.**

A zero means the LCS length for those prefixes is zero, but I still use the recurrence to determine whether match or mismatch is compatible with that zero.

---

# 5. The actual traceback we followed

We started at the bottom-right:

`[4][4] = 2`

At `[4][4]`, both possibilities were initially possible:

- Mismatch: UP = `2`, LEFT = `1` → max = `2` → valid
- Match: diagonal `[3][3] = 1`, then `1 + 1 = 2` → also valid

So:

> **`[4][4]` alone was ambiguous.**

We then followed UP to `[3][4]`.

---

## `[3][4] = 2`

### Mismatch
UP = `1`

LEFT = `1`

`max(1,1) = 1`

But actual = `2` → mismatch is impossible.

### Match
Diagonal `[2][3] = 1`

`1 + 1 = 2`

Actual = `2` → match works.

Therefore:

`X4 = Y3 = 0`

So now:

> `X = _ _ _ 0`

Then move diagonally to `[2][3]`.

---

## `[2][3] = 1`

### Mismatch
UP = `0`

LEFT = `0`

`max(0,0) = 0`

Actual = `1` → impossible.

### Match
Diagonal `[1][2] = 0`

`0 + 1 = 1`

Actual = `1` → valid.

Therefore:

`X3 = Y2 = 2`

Now:

> `X = _ _ 2 0`

Then move diagonally to `[1][2]`.

---

## `[1][2] = 0`

This cell was fully inspected.

### Mismatch
UP = `0`

LEFT = `0`

`max(0,0) = 0`

Actual = `0` → valid.

### Match
Diagonal `[0][1] = 0`

`0 + 1 = 1`

Actual = `0` → impossible.

Therefore:

> **Mismatch is confirmed at `[1][2]`. No character is taken.**

Because it is a mismatch, we can move UP or LEFT. We chose LEFT to `[1][1]`.

---

## `[1][1] = 0`

Again, we must inspect both cases.

### Mismatch
UP = `0`

LEFT = `0`

`max(0,0) = 0`

Actual = `0` → valid.

### Match
Diagonal `[0][0] = 0`

`0 + 1 = 1`

Actual = `0` → impossible.

Therefore mismatch is confirmed again.

No character is taken.

At this point we reached the boundary, so **that particular traceback path is finished**.

---

# 6. Important: traceback is NOT the same as reverse reconstruction

This distinction caused a lot of confusion.

### Traceback
Traceback means physically following the DP table backward:

- UP
- LEFT
- DIAGONAL

Only adjacent cells are used.

You cannot jump from `[1][1]` to `[3][2]` during one traceback.

### Reverse reconstruction
Reverse reconstruction is the **whole job** of figuring out hidden `X` from known `Y` + the completed table.

A single traceback path may reveal only some characters.

So:

> **Traceback = one path/technique.**
>
> **Reverse reconstruction = the overall task.**

When a traceback reaches the boundary but some characters of `X` are still unknown, I can inspect other relevant cells in the table to recover those remaining characters.

I do **not** pretend that I am still physically walking the same traceback path.

---

# 7. How the remaining characters were found

After the first traceback path, we had:

> `X = _ _ 2 0`

We still needed `X1` and `X2`.

We did **not** choose cells randomly.

We looked for cells whose values strongly indicate a match through the diagonal.

### Finding X2
Look at:

`[3][2] = 1`

Its diagonal is:

`[2][1] = 0`

If the current characters match:

`0 + 1 = 1`

which exactly matches the actual cell value.

Since `Y3 = 0`, this gives:

> **X2 = 0**

Now:

`X = _ 0 2 0`

### Finding X1
Look at:

`[4][1] = 1`

Its diagonal is the boundary cell `[3][0] = 0`.

A match gives:

`0 + 1 = 1`

which matches the actual cell value.

Since `Y4 = 8`, this gives:

> **X1 = 8**

Therefore:

# `X = 8020`

---

# 8. The exact mental model I want to remember

When I see a reverse-LCS table, I should think:

> **The table is giving me clues about a hidden string.**

At a cell:

1. Look at the actual DP value.
2. Test **mismatch** using UP/LEFT.
3. Test **match** using DIAGONAL + 1.
4. Compare each result with the actual value.
5. If match is forced → take the known Y character as the corresponding X character.
6. If mismatch is forced → take no character; move UP/LEFT.
7. If both are possible → the current cell alone is ambiguous; use more clues.
8. If a traceback reaches the boundary but X is not fully known → the traceback path is finished, but the **reverse reconstruction is not necessarily finished**. Inspect other relevant cells for the remaining X positions.

---

# 9. My biggest confusions — and what fixed them

### Confusion 1: “Does a DP value of 1 mean the current characters match?”
**No.** It only means the LCS length for those prefixes is 1.

**Fix:** Always test match and mismatch against the recurrence.

### Confusion 2: “If the cell is 0, do I skip the match/mismatch test?”
**No.**

**Fix:** Every cell can be inspected. For zero, match would normally produce `1` from a zero diagonal, so it can often be rejected immediately.

### Confusion 3: “Do I take a character when there is a mismatch?”
**No.**

**Fix:** Only a confirmed match gives an equality `X[j] = Y[i]`.

### Confusion 4: “If I move diagonally, does that automatically mean a match?”
Not merely because I moved there. First the **current cell's match case must be confirmed**. The diagonal cell is simply the next cell to inspect after a confirmed match.

### Confusion 5: “Can I move from `[1][1]` to `[3][2]`?”
**No.** Not during the same traceback.

**Fix:** Traceback moves only to adjacent cells. Other cells can be inspected separately during the overall reverse reconstruction.

### Confusion 6: “If traceback ends at the base case, is the whole problem finished?”
**Not necessarily.**

**Fix:** A traceback path can finish while some hidden characters are still unknown. Then inspect other table cells that constrain those positions.

### Confusion 7: “Were `[3][2]` and `[4][1]` chosen randomly?”
**No.**

**Fix:** Look for table values where the diagonal + 1 explains the actual value. Those are match clues that can reveal the corresponding hidden X character.

---

# 10. The one-line version for exam stress

If I panic and forget everything, remember this:

> **Reverse LCS = use the completed table as clues. At each useful cell, test Match vs Mismatch. Match → take the Y character and go diagonal. Mismatch → take nothing and go UP/LEFT. If one traceback ends before all of X is known, inspect other cells for the remaining characters.**

---

# 11. Final reconstruction

Known:

`Y = 1208`

Recovered:

- `X4 = 0`
- `X3 = 2`
- `X2 = 0`
- `X1 = 8`

Therefore:

## **X = 8020**

And the important thing I should remember is not just the answer `8020`.

The real achievement was understanding **why the table can be read backward as evidence**.

I did not need to memorize a magical path. I learned to ask:

> **“What would this cell have to be if these characters matched? What would it be if they didn't? Which possibility agrees with the table?”**

That is the reasoning that made the reverse-LCS problem stop feeling mysterious.
