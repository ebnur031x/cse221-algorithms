# CSE221 — SCC (Kosaraju) — My Mental Model

> Goal: come back later and have the movement click again instead of memorizing the algorithm.

## 1. What SCC means

**Strongly Connected Component = a group of vertices where every vertex can reach every other vertex following the directed arrows.**

Example:

```text
A → B
↑   ↓
└── C
```

A, B, C are one SCC because you can travel between them in both directions through directed paths.

### Important

- SCC is for **directed graphs**.
- Weighted or unweighted: **both work; weights are ignored**.
- Cycles are allowed.
- If a directed graph has **no cycle**, every vertex is its own SCC.
- An undirected graph uses **connected components** instead; SCC is not the useful concept there.

Mental question:

> **Which nodes belong together because they can reach each other?**

---

# 2. SCC vs Topological Sort

They feel similar because both use DFS, but they solve different problems.

```text
Topological Sort → SORT / ORDER nodes
SCC               → GROUP mutually reachable nodes
```

```text
Topo: directed DAG only
SCC:  directed graph, cycles are fine
```

Topo does **not** call SCC. They work independently.

---

# 3. The Kosaraju flow

Only remember:

```text
DFS → Reverse → DFS
```

More precisely:

```text
1. DFS on ORIGINAL graph G
   ↓
   when a node finishes → push to stack

2. Reverse EVERY edge
   A → B becomes B → A
   ↓
   Gᵀ (transpose)

3. Pop stack from the top
   ↓
   DFS on Gᵀ
   ↓
   every DFS run = ONE SCC
```

### Critical distinction from Topological Sort

```text
Topo:
  graph stays the same
  stack/result order is read in reverse

Kosaraju:
  graph itself is reversed
  then DFS again
```

So when I was confused about “reverse,” this is the exact difference:

**Topo reverses the order you read the result. Kosaraju reverses every graph arrow.**

---

# 4. Why the first DFS?

The first DFS is **not finding the SCCs yet**.

It only finds the important **finishing order**:

```text
visit node
  ↓
go deep
  ↓
finish node
  ↓
push node
```

That stack tells the second DFS **which node to start from first**.

---

# 5. Reverse the graph

Do this literally for every edge:

```text
A → B
```

becomes:

```text
B → A
```

Nothing else changes.

Do NOT confuse this with reversing the stack.

---

# 6. Second DFS = collect one SCC

Take the **top of the first stack**.

Run DFS on the **reversed graph**.

Everything reached in that DFS belongs to the same SCC.

Then:

```text
pop next
↓
if unvisited → DFS
↓
that DFS = next SCC
```

Repeat until the stack is empty.

---

# 7. Tiny example

```text
A → B
B → A
B → C
```

A and B are mutually reachable:

```text
A → B → A
```

So:

```text
SCC = {A, B}
```

C is separate because C cannot get back to A or B:

```text
SCC = {C}
```

The important idea is **mutual reachability**, not merely “there is an edge.”

---

# 8. Why cycles matter

A cycle like:

```text
A → B → C → A
```

naturally creates a multi-node SCC:

```text
{A, B, C}
```

If there is no cycle:

```text
A → B → C
```

then:

```text
{A}, {B}, {C}
```

So my intuition became:

> **A multi-node SCC needs a directed cycle / mutual reachability.**

But SCC is **not simply cycle detection**. It identifies the whole mutually reachable group.

---

# 9. Raw questions that made it click

> "seems like circular tho, like u have learnt cycle detection thing"

Yes — cycle detection is the foundation, but SCC asks a bigger question: **which nodes are mutually reachable?**

> "both are tryna solve same problem? Right? Sorting? Dfs?"

No. Both may use DFS, but **Topo = sort/order; SCC = group**.

> "So is it like topo is using scc to get one thing done that he cannot do?"

No. **They work independently.**

> "So topo sort only works with dag, and scc only work or figure out or mostly use when there is cycle and nodes in groups"

Correct mental model, with one detail: SCC works on any directed graph; cycles are simply where multi-node SCCs become interesting.

> "Mutual reachable whatever thing is without cycle. It’s not possible."

Correct for different vertices: mutual reachability implies a directed cycle. A single vertex can still be an SCC by itself.

> "A — B / So its already connected no need to scc? that is why scc does not work on undirected graph."

For undirected graphs, ordinary **connected components** are the appropriate concept.

> "now the steps are for scc 1. Dfs first normal like topo 2. Reverse the edge, not just order like topo 3. Again dfs to find the mutual reachable nodes?"

Exactly:

```text
DFS → Reverse edges → DFS → SCCs
```

---

# 10. Exam memory

```text
KOSARAJU

1. DFS(G) → push AFTER finish
2. Reverse EVERY edge → Gᵀ
3. clear visited
4. pop stack
5. DFS(Gᵀ)
6. each DFS = one SCC

Time: O(V + E)
```

### One-line mental picture

```text
FIRST DFS = finishing order
        ↓
REVERSE GRAPH = arrows flip
        ↓
SECOND DFS = collect groups
```
