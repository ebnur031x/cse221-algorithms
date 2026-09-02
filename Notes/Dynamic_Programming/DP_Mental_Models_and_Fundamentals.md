# CSE221 Dynamic Programming — Mental Models & Fundamentals

> Personal notes built from the DP confusion-clearing process. The goal is to understand **why** a recurrence has the shape it has, not to memorize formulas.

---

## 1. The core DP idea

Dynamic Programming (DP) is a way to solve a problem by breaking it into smaller subproblems, solving those smaller problems, **saving their answers**, and reusing them when needed.

The most useful mental picture:

```text
smaller problems → save answers → build bigger problem
```

The DP array is like a notebook/table of answers.

```text
dp[i]
```

is **not automatically** "the answer to everything." Its meaning must be defined for the particular problem.

---

# 2. The three mental tools

When approaching a new DP problem, use these three questions.

### Tool 1 — State

> **What does `dp[...]` represent?**

Example:

```text
dp[i] = number of ways to reach stair i
```

or:

```text
dp[i] = minimum cost needed to reach stair i
```

The state definition is the precise meaning of each DP cell.

---

### Tool 2 — Work backward

Ask:

> **How could I arrive at this state?**
>
> **What was the previous state / last decision?**

For a stair problem where 1 or 2 steps are allowed:

```text
To reach i:
    I could come from i-1
    I could come from i-2
```

Therefore the smaller states needed are:

```text
i-1, i-2
```

This is **not** a universal rule that stairs always use `i-1` and `i-2`. Those states come from the problem's allowed moves.

If the problem allows 1, 2, or 3 steps:

```text
i-1, i-2, i-3
```

If it allows only 1 step:

```text
i-1
```

General principle:

> **List every previous state that can legally lead to the current state.**

---

### Tool 3 — Objective decides the combination

After finding the possible previous states, ask:

> **What does the problem actually want me to do with those possibilities?**

Common patterns:

| Problem goal | Typical combination |
|---|---|
| Count all valid possibilities | `+` |
| Find the minimum / cheapest | `min(...)` |
| Find the maximum / best | `max(...)` |

This is why the same underlying problem structure can have different recurrences.

---

# 3. The powerful recurrence question

The most important question is:

> **“To solve the problem for `i`, what smaller states do I need?”**

For stairs, a more concrete version is:

> **“To reach stair `i`, where could my last step have come from?”**

Then the recurrence follows from the answers to that question.

The full chain is:

```text
State
  ↓
How can I reach this state?
  ↓
Previous states / last decisions
  ↓
What does the objective want?
  ↓
Combine the previous answers
  ↓
Recurrence
```

Do not start by trying to remember a formula. Start by asking the question.

---

# 4. Example: counting ways to climb stairs

Suppose:

> You can move 1 or 2 steps at a time. Find the number of ways to reach stair `i`.

### State

```text
dp[i] = number of ways to reach stair i
```

### Work backward

To reach `i`, the last move could have been:

```text
i-1 → take 1 step → i
i-2 → take 2 steps → i
```

So the previous states are `i-1` and `i-2`.

### Objective

We want to **count all valid ways**.

The ways coming from `i-1` and the ways coming from `i-2` are both valid groups. We want both groups.

Therefore:

```text
dp[i] = dp[i-1] + dp[i-2]
```

### Why `+`?

Because we are counting **all** possibilities.

Think:

```text
ways from i-1
        +
ways from i-2
        =
all ways to reach i
```

The `+` does not come from DP itself. It comes from the **goal: count all ways**.

---

# 5. Example: minimum cost to reach a stair

Now change the question:

> You can move 1 or 2 steps at a time. Each stair has a cost. Find the **minimum cost** needed to reach stair `i`.

The allowed moves are the same, so the backward thinking is the same:

```text
i-1 or i-2
```

But the state changes.

### State

```text
dp[i] = minimum cost needed to reach stair i
```

Now we do not want all routes. We want the **cheapest** route.

So compare the two candidate previous routes:

```text
min(dp[i-1], dp[i-2])
```

Then pay the cost of the current stair:

```text
dp[i] = min(dp[i-1], dp[i-2]) + cost[i]
```

### Important detail: why is there both `min` and `+`?

They do different jobs.

```text
min(dp[i-1], dp[i-2])
```

means:

> Compare the two possible previous routes and choose the cheaper one.

Then:

```text
+ cost[i]
```

means:

> Once I choose that route, I still have to pay the cost of reaching the current stair `i`.

So:

```text
compare previous routes → choose cheapest → add current cost
```

The parentheses matter:

```text
min(dp[i-1], dp[i-2]) + cost[i]
```

is different from:

```text
min(dp[i-1], dp[i-2] + cost[i])
```

The second expression adds the current cost to only one candidate before comparing, which is a different meaning.

---

# 6. `+` vs `min()` — a very important pattern

This distinction is worth remembering as a **mental tool**, not as a formula to blindly memorize.

### Counting

Question:

> How many valid ways are there?

If two groups of ways are both valid, count both:

```text
dp[i] = dp[i-1] + dp[i-2]
```

### Minimum

Question:

> What is the cheapest valid way?

The possible routes compete with each other. Choose one:

```text
dp[i] = min(dp[i-1], dp[i-2]) + cost[i]
```

### Maximum

Question:

> What is the best / maximum result?

Compare the candidates and choose the larger one:

```text
dp[i] = max(candidate1, candidate2)
```

General mental rule:

> **All valid possibilities matter → combine them (often `+`).**
>
> **Only the best one matters → compare them (`min` / `max`).**

The exact operation always depends on the problem.

---

# 7. State and value are different things

A common source of confusion:

```text
dp[i]
```

has two different ideas inside it:

- `i` = **which subproblem / state** we are talking about
- `dp[i]` = **the answer/value for that state**

Example:

```text
index:  0  1  2  3  4
dp:     1  1  2  3  5
```

The `4` is the state/index. The `5` is the answer stored there.

They may sometimes look equal (`dp[3] = 3`), but that is coincidence, not a rule.

---

# 8. Base cases

A recurrence needs already-known smaller answers to start from.

For the simple stair-counting recurrence:

```text
dp[i] = dp[i-1] + dp[i-2]
```

we need:

```text
dp[0] = 1
dp[1] = 1
```

Then we can calculate:

```text
dp[2] = dp[1] + dp[0] = 2
dp[3] = dp[2] + dp[1] = 3
dp[4] = dp[3] + dp[2] = 5
```

### Why not use the recurrence for `dp[1]`?

Because:

```text
dp[1] = dp[0] + dp[-1]
```

would require an invalid state.

So base cases are the **foundation**; the recurrence builds later states.

Do not memorize that every DP starts at `0` and `1`. The base cases depend on the state and on which previous states the recurrence needs.

---

# 9. Why is `dp[0] = 1` in the counting-stairs example?

Here `0` means the starting position / zero steps taken.

```text
Start → stair 1 → stair 2 → ...
```

`dp[0] = 1` means there is **one way to be at the starting point**: take no steps.

This is useful because it lets the counting recurrence work:

```text
dp[2] = dp[1] + dp[0]
```

The `1` is not a universal DP default. It makes sense because this is a **counting** problem and the empty sequence is one valid way to start.

---

# 10. Bottom-up vs top-down

These are two ways of implementing the same DP reasoning.

### Bottom-up

Usually iterative. Start with small states and build upward:

```text
dp[0]
  ↓
dp[1]
  ↓
dp[2]
  ↓
dp[3]
  ↓
...
```

Typical structure:

```text
dp[0] = base
dp[1] = base

for i = 2 to n:
    dp[i] = ...
```

No recursion is required.

### Top-down / memoization

Usually recursive. Start from the final problem, recursively ask for smaller states, and save answers so repeated states do not get recalculated.

The important point:

> **Bottom-up vs top-down is an implementation direction, not a completely different DP idea.**

The same state and recurrence reasoning can be used in both.

---

# 11. Recursion and the duplicate-subproblem problem

In a naive recursive stair solution:

```text
Ways(4)
├── Ways(3)
│   ├── Ways(2)
│   └── Ways(1)
└── Ways(2)   ← repeated calculation
```

The important point is that `Ways(4)` is **not called again** when `Ways(3)` returns.

What happens is:

1. `Ways(4)` calls `Ways(3)`.
2. `Ways(3)` finishes and returns its answer.
3. Execution resumes inside the already-running `Ways(4)`.
4. `Ways(4)` then calls `Ways(2)`.
5. That `Ways(2)` was already calculated inside `Ways(3)`.

That repeated work is an **overlapping subproblem**.

DP fixes this by saving the answer to `Ways(2)` and reusing it.

---

# 12. The recursion tree vs the DP table

Naive recursion may repeatedly calculate the same subproblem.

DP says:

> **If I already know the answer to this smaller problem, don't calculate it again. Store it.**

So the DP table is not a record of every path or every calculation.

It is a table of **answers to subproblems**.

Example:

```text
index:  0  1  2  3  4
dp:     1  1  2  3  5
```

The table stores the answers. It does not need to store every individual path.

---

# 13. A reusable DP checklist

When facing a new 1D DP problem, mentally run this:

```text
1. STATE
   What exactly does dp[i] mean?

2. WORK BACKWARD
   To solve state i, how could I arrive here?
   What are the possible previous states / last decisions?

3. OBJECTIVE
   What does the problem want?
   Count? Minimum? Maximum? Something else?

4. COMBINE
   How does that objective tell me to combine the candidates?
   + / min / max / another operation

5. CURRENT CONTRIBUTION
   Do I need to add a current cost/value after choosing a previous state?

6. BASE CASES
   What smallest states are directly known?

7. BUILD
   Can I fill the states from small → large?
```

The key is that **Step 2 discovers the recurrence's structure**, while **Step 4 determines the operation**.

---

# 14. What NOT to memorize

Do not memorize:

```text
dp[i] = dp[i-1] + dp[i-2]
```

as "the DP formula."

Do not memorize:

```text
dp[i] = min(dp[i-1], dp[i-2]) + cost[i]
```

as "the minimum-cost formula."

Instead remember why each one exists.

```text
Allowed moves
      ↓
Possible previous states
      ↓
Problem objective
      ↓
Combination operation
      ↓
Recurrence
```

That way, when the problem changes the allowed moves, costs, choices, or objective, you can rebuild the recurrence instead of depending on memory.

---

# 15. One concrete example of the toolbox beyond stairs

## House Robber / Maximum Money

Problem:

> Houses are in a row. Each house has money. You cannot rob two adjacent houses. Find the maximum money you can collect.

Suppose:

```text
money = [2, 7, 9, 3]
```

### State

```text
dp[i] = maximum money obtainable from houses 0 through i
```

### Work backward

For house `i`, the last decision is either:

**Rob `i`:**

Then house `i-1` cannot be robbed, so the previous state is `i-2`:

```text
dp[i-2] + money[i]
```

**Skip `i`:**

Then the answer is simply what we could already get through `i-1`:

```text
dp[i-1]
```

### Objective

We want the **maximum**.

Therefore:

```text
dp[i] = max(dp[i-1], dp[i-2] + money[i])
```

Again, no formula was memorized. The toolbox produced it:

```text
STATE → LAST DECISION → CANDIDATES → MAX → RECURRENCE
```

This is why the mental model is useful.

---

# 16. The big takeaway

The most valuable DP habit is not knowing many formulas.

It is being able to look at a problem and ask:

> **What does my state mean?**
>
> **How could I have gotten here?**
>
> **What does the problem want me to do with those possibilities?**

Then:

```text
State
→ previous states / last decision
→ objective
→ combination
→ recurrence
→ base cases
→ DP table / implementation
```

If this way of thinking becomes natural, new DP problems become much less like **"Which formula do I remember?"** and much more like **"What is this problem logically asking me to combine?"**

---

## Current scope

These notes cover the DP fundamentals developed so far: state definition, working backward, recurrence construction, counting vs minimum/maximum objectives, base cases, overlapping subproblems, memoization, and bottom-up DP.

They are **learning notes, not a complete CSE221 exam-answer template**. As more DP patterns are learned, this document should grow with them rather than pretending that every DP problem fits the same exact recurrence shape.
