# CSE221 — DAG & Topological Sort — My Mental Model

> Goal: come back later and have the movement click again, not memorize words.

## 1. What Topological Sort is

A **topological order** puts every prerequisite before the thing that depends on it.

```text
A → B  means  A must come before B
```

### Constraint

- Graph must be **directed**.
- Graph must be **acyclic** → a **DAG**.
- If there is a directed cycle, no topological ordering exists.
- Multiple valid topological orders can exist.
- Isolated nodes are fine; they have no ordering restriction.

---

## 2. The graph I hand-traced

```text
A → C
A → D
B → D
C → E
D → E
H
V
```

Think:

```text
A ─→ C ─→ E
│
└─→ D ─→ E
     ↑
     B

H     V   (isolated)
```

---

# 3. DFS Topological Sort — the exact movement

```text
1. Pick an unvisited node.
2. DFS → keep going as deep as possible.
3. When a node is completely finished → push it into TOPO stack.
4. Backtrack.
5. Continue until every node is finished.
6. Pop the TOPO stack → topological order.
```

### The key distinction I struggled with

**DFS stack / recursion stack** and **topological stack** are not the same thing.

```text
DFS recursion = where I am right now
Topo stack    = nodes I have completely finished
```

So:

```text
ENTER A
  ↓
go deep...
  ↓
FINISH A
  ↓
push A to topo stack
```

`push` does **not** make DFS go deeper. The recursive `dfs(v)` does.

---

## 4. Pencil simulation

Start DFS at A and choose C first:

```text
A
↓
C
↓
E
```

E has no outgoing edge:

```text
finish E → push E
```

Back to C:

```text
finish C → push C
```

Back to A, take D:

```text
A → D → E
```

E is already finished.

```text
finish D → push D
finish A → push A
```

Then B:

```text
B → D (already finished)
finish B → push B
```

H:

```text
H → nothing
finish H → push H
```

V:

```text
V → nothing
finish V → push V
```

One possible final stack, **bottom → top**:

```text
[E, C, D, A, B, H, V]
                         ↑ TOP
```

Pop it:

```text
V → H → B → A → D → C → E
```

Check the edges:

```text
A before C ✓
A before D ✓
B before D ✓
C before E ✓
D before E ✓
```

H and V can move around because they have no edges.

---

# 5. Why the stack feels like a reverse

If finish order is:

```text
E → C → D → A → B
```

then pushing into a real stack gives:

```text
TOP
 B
 A
 D
 C
 E
```

Popping naturally gives:

```text
B → A → D → C → E
```

So **with a real Stack, you do not need `reverse()`**.

If using an `ArrayList` containing finish order, then you would reverse the list.

Important: we are reversing the **result-reading order**, not the graph arrows.

---

# 6. Cycle detection connection

Example:

```text
1 → 2
2 → 3
3 → 1
```

DFS:

```text
1 → 2 → 3 → 1
```

When 3 sees 1 and 1 is still **GRAY / in the current DFS recursion path**, that is a **back edge** → cycle.

Therefore:

```text
cycle → not a DAG → no topological ordering
```

This connects directly to the cycle detection I already learned from DFS.

---

# 7. Kahn's Algorithm — different way to Topo Sort

Instead of DFS + finish stack, use:

```text
indegree = number of incoming edges
```

Then:

```text
1. Find all indegree-0 nodes.
2. Put them in a queue.
3. Remove one.
4. Decrease indegree of its outgoing neighbors.
5. Any neighbor reaching 0 goes into queue.
6. Repeat.
```

Mental model:

> **Who has no prerequisites right now?**

If fewer than V nodes are processed → cycle exists.

So there are two common Topo methods:

```text
DFS method  → finish → stack → pop
Kahn method → indegree 0 → queue
```

---

# 8. Raw questions that made it click

> "but core conecpt is what? it goes it deep? does it empty stack as go or push?"

The answer I needed:

**DFS goes deep. Pushing into the topo stack happens only when a node is finished.**

> "so is it two diff thing, clearing stack vs adding stack?"

Yes: recursion/DFS movement and the finish-order stack are different ideas.

> "should i always mention two thing?"

For DFS Topo, remember the two important actions:

**go deep → finish → push.**

> "normal travesal means, just going deep and pop. and sequence means what? should mention the pop node first?"

The final topological sequence comes from **popping the finish stack**.

> "when we reverse it after dfs full, do u use just recursion stack or really making that 3 2 1 stack, 1 2 3, with the reverse function?"

Do not reverse the recursion stack. Use a separate finish-order stack; popping it already gives the reverse order.

---

# 9. The one mental picture

```text
             DFS
              ↓
        go as deep as possible
              ↓
        node completely done?
              ↓ YES
        push into topo stack
              ↓
          backtrack
              ↓
        repeat for all nodes
              ↓
          pop the stack
              ↓
       TOPOLOGICAL ORDER
```

### Exam memory

```text
Topo = ORDER
DAG only
DFS: deep → finish → push
Pop stack → answer
Cycle → no Topo
```
