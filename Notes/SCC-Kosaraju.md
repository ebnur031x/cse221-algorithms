# CSE221 — SCC (Kosaraju) — My Mental Model

> Goal: when I come back to this note during the semester, the flow should click again instead of feeling like code to memorize.

## 0. What SCC means

A **Strongly Connected Component** is a group where **every vertex can reach every other vertex following arrow direction**.

Important: just being connected is NOT enough.

Example: `3 → 4` does **not** make `{3,4}` an SCC unless `4 → 3` (or another directed path from 4 back to 3) also exists.

---

## 1. The Kosaraju flow I learned

Only 3 big things:

```text
1. DFS on G
   ↓
   put a node into STACK only AFTER all its neighbors finish

2. Reverse EVERY arrow
   G → Gᵀ

3. Pop from STACK (top first)
   DFS on Gᵀ
   ↓
   every DFS tree = ONE SCC
```

### The mental model

**First DFS = figure out finishing order.**

**Reverse graph = turn every arrow around.**

**Second DFS = use that finishing order to collect SCCs.**

---

# 2. Exact graph I hand-tracked

```text
1 ↔ 2 → 3 → 4
        ↘ 5
        ↘ 6 ↔ 7

8 → 4
```

Adjacency list:

```text
1 → [2]
2 → [1, 3]
3 → [4, 5, 6]
4 → []
5 → []
6 → [7]
7 → [6]
8 → [4]
```

---

# 3. DFS #1 — finishing stack

Start with `dfs1(1)`.

```text
1 → 2 → 3 → 4
             finish 4 → push 4
          → 5
             finish 5 → push 5
          → 6 → 7
                 finish 7 → push 7
              finish 6 → push 6
       finish 3 → push 3
    finish 2 → push 2
finish 1 → push 1
```

Then the outer loop reaches 8:

```text
8 → 4 (already visited)
finish 8 → push 8
```

Final stack, **bottom → top**:

```text
[4, 5, 7, 6, 3, 2, 1, 8]
                                      ↑ top
```

### Critical thing I struggled with

I initially needed to separate these two ideas:

- DFS **goes deeper first**.
- `stack.push(u)` happens **when u is finished**, AFTER all reachable unvisited neighbors are done.

So `push` is not what makes DFS go deep. The recursive `dfs1(v)` does that.

---

# 4. Why `void dfs1(int u)`?

This was one of my coding pain points: **where do `void`, `dfs1`, and `int u` even come from?**

We design the function based on what it needs to do:

```java
void dfs1(int u)
```

means:

- `dfs1` = just the name we give to the first DFS.
- `void` = it does not return a value; it changes `visited` and `stack`.
- `int u` = the vertex we are currently processing.

So:

```java
dfs1(2);
```

means:

> Run the same DFS starting from vertex 2.

This is **function design**, not a special SCC keyword.

---

# 5. DFS #1 code — connect every line to the movement

```java
void dfs1(int u) {
    visited[u] = true;

    for (int v : graph[u]) {
        if (!visited[v]) {
            dfs1(v);
        }
    }

    stack.push(u);
}
```

For `dfs1(1)`:

```text
u = 1
↓
visited[1] = true
↓
graph[1] = [2]
↓
v = 2
↓
2 is not visited
↓
dfs1(2)
```

So the code literally follows the graph.

### The line that matters most

```java
stack.push(u);
```

It is **after** the `for` loop.

That means:

```text
visit u
  ↓
visit all possible unvisited neighbors
  ↓
finish them
  ↓
ONLY THEN push u
```

---

# 6. Build Gᵀ — reverse EVERY edge

Original → transpose:

```text
1 → 2   ⇒   2 → 1
2 → 1   ⇒   1 → 2
2 → 3   ⇒   3 → 2
3 → 4   ⇒   4 → 3
3 → 5   ⇒   5 → 3
3 → 6   ⇒   6 → 3
6 → 7   ⇒   7 → 6
7 → 6   ⇒   6 → 7
8 → 4   ⇒   4 → 8
```

Correct `Gᵀ` adjacency list:

```text
1 → [2]
2 → [1]
3 → [2]
4 → [3, 8]
5 → [3]
6 → [3, 7]
7 → [6]
8 → []
```

### Mental rule

Do NOT think "find a new graph from scratch."

Think:

```text
Every arrow:
A → B
becomes
B → A
```

---

# 7. DFS #2 — use the STACK TOP first

Stack:

```text
[4, 5, 7, 6, 3, 2, 1, 8]
                         ↑
                        TOP
```

Pop from the top.

### Pop 8

```text
8 → []
```

Nothing reachable.

```text
SCC = {8}
```

### Pop 1

```text
1 → 2
2 → 1 (already visited)
```

```text
SCC = {1,2}
```

Pop 2 → already visited → skip.

### Pop 3

```text
3 → 2
```

2 is already visited.

```text
SCC = {3}
```

### Pop 6

```text
6 → 3, 7
```

3 is already visited.

Then:

```text
6 → 7 → 6
```

So:

```text
SCC = {6,7}
```

Pop 7 → already visited → skip.

### Pop 5

```text
5 → 3
```

3 already visited.

```text
SCC = {5}
```

### Pop 4

```text
4 → 3, 8
```

Both already visited.

```text
SCC = {4}
```

## Final SCCs

```text
{8}
{1,2}
{3}
{6,7}
{5}
{4}
```

Order does not matter; the grouping does.

---

# 8. DFS #2 code

```java
void dfs2(int u) {
    visited[u] = true;
    component.add(u);

    for (int v : transpose[u]) {
        if (!visited[v]) {
            dfs2(v);
        }
    }
}
```

The important difference from `dfs1`:

```text
DFS #1 → push finished nodes into stack
DFS #2 → add reached nodes into current component
```

There is **no `stack.push()` in DFS #2**.

---

# 9. Full Kosaraju skeleton

```java
// First DFS: finishing order
for (int u = 0; u < n; u++) {
    if (!visited[u]) {
        dfs1(u);
    }
}

// Reverse every edge
transposeGraph();

// Start fresh
Arrays.fill(visited, false);

// Second DFS: one DFS = one SCC
while (!stack.isEmpty()) {
    int u = stack.pop();

    if (!visited[u]) {
        component.clear();
        dfs2(u);
        System.out.println(component);
    }
}
```

Read it as English:

```text
DFS everything and remember finishing order.
↓
Reverse the graph.
↓
Forget the old visited marks.
↓
Take the latest-finishing node first.
↓
DFS.
↓
Whatever that DFS reaches = one SCC.
```

---

# 10. My recurring confusion / fixes

### "Does stack clearing mean DFS is pushing?"

No.

- DFS recursion = **go deeper**.
- `stack.push(u)` = **record that u finished**.
- `stack.pop()` in phase 2 = **choose the next starting vertex**.

### "Why does 8 become its own SCC if 8's neighbor is 4?"

Because phase 2 uses **Gᵀ**, not G.

Original:

```text
8 → 4
```

Transpose:

```text
4 → 8
```

Therefore in Gᵀ:

```text
8 → []
```

so `{8}` is an SCC.

### "Why isn't {3,4} an SCC?"

Because:

```text
3 → 4
```

but there is no directed path from 4 back to 3 in the original graph.

---

# 11. The one mental picture to keep

```text
             PHASE 1
               G
               ↓
          DFS deeply
               ↓
       finish → push
               ↓
             STACK
               ↓
       reverse ALL arrows
               ↓
              Gᵀ
               ↓
             PHASE 2
               ↓
       pop top of STACK
               ↓
          DFS in Gᵀ
               ↓
       reached nodes = SCC
               ↓
       repeat until empty
```

**Kosaraju = finish order + reverse graph + DFS again.**

---

# Raw questions I asked while learning

These are worth keeping because they capture exactly where the concepts clicked:

> "but core concept is what? it goes it deep? does it empty stack as go or push?"

> "so is it two diff thing, clearing stack vs adding stack?"

> "but what would i know, what to answer"

> "no push?"

> "should i always mention two thing?"

> "normal traversal means, just going deep and pop. and sequence means what? should mention the pop node first?"

> "Now, adj list"

> "if one arrow from 3 falls to 6. Then adj 3:[6] right?"

> "so, in reverse dfs, which node did u start from?"

> "8's neighor is 4, 4's neibhor is 3 and 8. so how the scc is just 8?"

> "But how did you got the errors and you know the parameters? How do you know it’s void not or anything?"

The important pattern behind these questions: **I understand algorithms better when I can physically trace what the code is doing.**

---

# Exam-speed memory

```text
Kosaraju:
1. DFS(G) → push AFTER finish
2. transpose(G) → reverse every edge
3. clear visited
4. pop stack
5. DFS(Gᵀ)
6. each DFS = one SCC

Time: O(V + E)
```
