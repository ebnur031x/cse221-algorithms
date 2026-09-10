# DAG & Topological Sort — My Notes

## 1. DAG

**DAG = Directed Acyclic Graph**

- **Directed** → edges have a direction: `A → B`
- **Acyclic** → there is **no directed cycle**
- Example of a cycle: `A → B → C → A`

If a directed graph has a cycle, it cannot have a valid topological ordering.

---

## 2. Topological Sort

Topological Sort = **ordering the vertices so every dependency comes first.**

For every edge:

`A → B`

we must have:

`A before B`

### Important

- Works only on a **DAG**.
- There can be **multiple valid orders**.
- It is a **sorting/order problem**, not a grouping problem.

---

## 3. DFS Topological Sort — The Core Idea

The key idea:

> **Finish a node completely → push it into the topological stack.**

Do **NOT** push when first visiting the node.

### Steps

1. Start DFS.
2. Go as deep as possible.
3. When a node has no more unvisited outgoing neighbors, it is **finished**.
4. Push the finished node into a separate **topological stack**.
5. Continue until every vertex is visited.
6. Pop the stack → that is the topological order.

### Why popping works

Nodes are pushed in **finish order**.

The stack pops them in **reverse finish order**, which gives the topological order.

So if using an actual `Stack`, there is **no need to call `reverse()`**.

---

## 4. Two Stacks — Don't Mix Them Up

### DFS / Recursion Stack

Answers:

**"Where am I right now?"**

- Node enters when DFS visits it.
- Used for detecting a directed cycle.
- GRAY = currently active / inside recursion.

### Topological Stack

Answers:

**"Which nodes have I completely finished?"**

- Node enters only when DFS finishes it.
- Used to produce the topological ordering.

**Cycle detection uses the recursion/active stack, not the final topological stack.**

---

## 5. Example — Pen & Pencil Simulation

Graph:

```text
A → C
A → D
B → D
C → E
D → E
```

Assume DFS starts at `A` and follows `C` before `D`.

### DFS movement

```text
A
↓
C
↓
E
```

`E` has nothing left → **finish E → push E**

```text
Stack: [E]
```

Back to `C` → finish → push `C`

```text
Stack: [E, C]
```

Back to `A` → go to `D`

`D → E`, but E is already finished.

Finish `D` → push `D`

```text
Stack: [E, C, D]
```

Finish `A` → push `A`

```text
Stack: [E, C, D, A]
```

Now DFS reaches `B`.

`B → D`, but D is already finished.

Finish `B` → push `B`

```text
Stack: [E, C, D, A, B]
```

### Pop the stack

```text
B → A → D → C → E
```

Check every edge:

- `A → C` ✓
- `A → D` ✓
- `B → D` ✓
- `C → E` ✓
- `D → E` ✓

So it is a valid topological order.

---

## 6. Isolated Vertex

A vertex with no incoming or outgoing edges is still valid.

Example:

```text
A → B       H
```

`H` has no dependency, so it can appear **anywhere** in a valid topological ordering.

In DFS:

```text
visit H → immediately finish H → push H
```

---

## 7. Kahn's Algorithm (Indegree Method)

Another way to do Topological Sort.

### Steps

1. Calculate **indegree** of every vertex.
2. Put all vertices with `indegree = 0` into a queue.
3. Remove one vertex from the queue and add it to the answer.
4. For each outgoing edge, decrease that neighbor's indegree by 1.
5. If a neighbor becomes `0`, put it into the queue.
6. Repeat.

If fewer than `V` vertices are processed → **there is a cycle**.

### Indegree

**Indegree = number of incoming edges.**

For:

```text
A → B
C → B
```

`indegree(B) = 2`.

---

## 8. Topological Sort vs SCC

These are different problems.

| Topic | Main job |
|---|---|
| **Topological Sort** | **ORDER** vertices by dependencies |
| **SCC** | **GROUP** mutually reachable vertices |

Topological Sort asks:

> "What order should these nodes come in?"

SCC asks:

> "Which nodes belong to the same mutually reachable group?"

They may both use DFS, but they solve different problems.

---

## 9. Topological Sort vs Kosaraju

### Topological Sort

```text
DFS → finish nodes → stack → pop order
```

**Do not reverse graph edges.**

### Kosaraju

```text
DFS original graph
        ↓
record finish order
        ↓
REVERSE every edge
        ↓
DFS again using finish order
        ↓
SCCs
```

Memory trick:

**Kosaraju = DFS → Reverse → DFS → SCCs**

---

## 10. One-Line Memory

### DAG
**Directed + No Cycle**

### Topological Sort
**Dependency order: `A → B` means A comes before B.**

### DFS Topological Sort
**Finish → Push → Pop**

### Kahn
**Indegree 0 → Queue → Remove → Decrease indegrees**

### SCC
**Mutually reachable group**

### Kosaraju
**DFS → Reverse → DFS**
