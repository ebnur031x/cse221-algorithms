# Bellman-Ford — My Understanding Notes

> These are my own understanding notes from learning Bellman-Ford step by step. Keep the wording simple. The goal is to rebuild the algorithm in my head and then translate it to Java/Codeforces syntax.

## 1. What Bellman-Ford solves

Same basic problem as Dijkstra:

**From one source node, find the shortest distance to every reachable node.**

Main difference:

- Dijkstra → does not safely handle negative edge weights.
- Bellman-Ford → handles negative edge weights.
- Bellman-Ford → can detect a negative-weight cycle.

## 2. Core idea

Bellman-Ford does NOT choose the closest node like Dijkstra.

Instead:

**Scan ALL edges → repeatedly relax them.**

For an edge `u → v` with weight `w`:

```text
dist[u] + w < dist[v]?
```

If yes:

```text
dist[v] = dist[u] + w
```

The relaxation idea is the same as Dijkstra. The big difference is that Bellman-Ford is **edge-based**.

## 3. Why V−1 passes?

`V−1` means **number of complete passes over ALL edges**.

It does NOT mean number of edges.

If there are 4 vertices:

```text
V = 4
V−1 = 3 passes
```

But `E` can be 3, 5, 10, etc.

Mental picture:

```text
Outer loop → V−1 passes
Inner loop → ALL E edges
```

Why V−1?

A shortest simple path can use at most `V−1` edges. Repeated relaxation lets the distance information spread along such a path.

Example:

```text
A → B → C
```

3 nodes → at most 2 edges → `V−1 = 2` passes.

## 4. Simple simulation

```text
A → B = 4
B → C = -2
A → C = 10
```

Start:

```text
A=0
B=∞
C=∞
```

Pass 1 — scan ALL edges:

```text
A→B: 0+4 = 4 → B=4
B→C: 4+(-2) = 2 → C=2
A→C: 0+10 = 10 → C stays 2
```

So:

```text
A=0
B=4
C=2
```

Pass 2 — scan ALL edges again. Nothing improves.

Because `V=3`, we need `V−1=2` passes.

Important:

**A direct edge does not get priority.**

`A→C=10` loses to `A→B→C=2` because only the smallest total path cost matters.

## 5. Negative-cycle detection

Normal Bellman-Ford:

```text
repeat V−1 times:
    scan ALL edges
    relax/update if cheaper
```

Then add a **separate extra scan**:

```text
scan ALL edges ONE more time
    if dist[u] + w < dist[v]
        negative cycle exists
```

The condition is the same relaxation condition.

But what it means is different:

- First `V−1` passes → if cheaper, **update**.
- Extra pass → if still cheaper, **negative cycle exists**; no update is needed.

Why no update?

After `V−1` passes, if there is no negative cycle, shortest distances should already be finalized. If something can still become smaller, we only need to know **that improvement is possible**. Updating would just continue decreasing values around the negative cycle.

Mental model:

```text
V−1 full scans → find shortest distances
extra full scan + IF → ask "can anything still get smaller?"
YES → negative cycle
NO  → no negative cycle
```

## 6. Negative-cycle example

```text
A → B = 2
B → C = -4
C → A = 1
```

Cycle total:

```text
2 + (-4) + 1 = -1
```

Every time we go around the cycle, the total gets 1 smaller.

So after the normal `V−1` passes, an extra scan can still find:

```text
dist[C] + 1 < dist[A]
```

TRUE → negative cycle.

The key is **not** the negative edge itself. A negative edge is allowed. The problem is that a cycle lets us keep decreasing the distance forever.

## 7. Codeforces setup = code version of the question

The graph is already given by the problem input. My setup code just reads and stores it.

Think:

**Setup/boilerplate = code version of the data in the question.**

Typical input:

```text
V E
then E lines: u v w
```

Sometimes a source node is also given, e.g. `V E S`. The algorithm does not fundamentally change; only the input/setup changes.

For Codeforces, use **BufferedReader + StringTokenizer** instead of Scanner because it is faster for large input.

## 8. Edge class = blueprint

```java
static class Edge {
    int u, v, w;
}
```

One `Edge` object contains the whole edge:

```text
e.u → starting node
e.v → ending node
e.w → weight
```

So:

```java
for (Edge e : edges)
```

means:

**For each complete Edge object stored in `edges`, take one edge at a time.**

`e` is the whole edge, not just one particular part.

## 9. Store all edges

```java
Edge[] edges = new Edge[E];
```

We have `E` edges, so we need `E` slots.

Then:

```java
for (int i = 0; i < E; i++) {
    edges[i] = new Edge();

    st = new StringTokenizer(br.readLine());

    edges[i].u = Integer.parseInt(st.nextToken());
    edges[i].v = Integer.parseInt(st.nextToken());
    edges[i].w = Integer.parseInt(st.nextToken());
}
```

This loop is for **filling the E edges given by the problem**, not Bellman-Ford processing yet.

## 10. `dist[]` and the source

```java
int[] dist = new int[V];
Arrays.fill(dist, Integer.MAX_VALUE);
dist[source] = 0;
```

`dist[]` stores distances **from the chosen source**.

After `fill`:

```text
A=∞
B=∞
C=∞
D=∞
```

Then if source is node 0 / A:

```text
A=0
B=∞
C=∞
D=∞
```

Important:

**0 is not always the source.** `0` is the source only when the problem says the source is node 0, or we choose node 0 as the source.

Think:

**source = my root/start point.**

## 11. Building the loops

First ask:

**What does Bellman-Ford need to repeat?**

Answer:

**ALL edges, V−1 times.**

So the outer loop is:

```java
for (int i = 1; i <= V - 1; i++) {
```

`i` = the **pass/round counter**.

Then ask:

**What contains every edge?**

Answer:

`edges`

So the inner loop is:

```java
for (Edge e : edges) {
```

Mental picture:

```text
Pass 1 → Edge 1, Edge 2, ... Edge E
Pass 2 → Edge 1, Edge 2, ... Edge E
...
Pass V−1 → Edge 1, Edge 2, ... Edge E
```

Important loop distinction:

```text
Outer loop = V−1 passes
Inner loop = all E edges
```

## 12. Normal Bellman-Ford engine

```java
for (int i = 1; i <= V - 1; i++) {
    for (Edge e : edges) {
        if (dist[e.u] + e.w < dist[e.v]) {
            dist[e.v] = dist[e.u] + e.w;
        }
    }
}
```

Read it as:

> **Do V−1 rounds. Every round, look at EVERY edge. If going through this edge gives a cheaper distance, update its destination.**

After this part, `dist[]` should contain the shortest distances from the source to every reachable node, assuming there is no negative cycle affecting the result.

## 13. Negative-cycle code

Separate loop — **NOT nested** after the normal Bellman-Ford loops:

```java
for (Edge e : edges) {
    if (dist[e.u] + e.w < dist[e.v]) {
        // negative cycle exists
    }
}
```

No update line here.

We only ask:

**Can ANY distance still become smaller after V−1 passes?**

If yes → negative cycle.

## 14. Full mental flow

```text
SETUP
↓
read V, E
↓
store all E edges
↓
create dist[]
↓
fill dist[] with ∞
↓
dist[source] = 0

NORMAL BELLMAN-FORD
↓
repeat V−1 times
↓
scan ALL edges
↓
if cheaper → update
↓
shortest distances found

NEGATIVE-CYCLE CHECK
↓
scan ALL edges ONE more time
↓
if anything can still improve
↓
negative cycle exists
```

## 15. Key comparison with Dijkstra

Both start from a source and use the same basic relaxation idea:

```text
dist[u] + weight < dist[v]
```

But:

```text
Dijkstra:
choose closest unvisited node
→ process its edges

Bellman-Ford:
scan ALL edges
→ repeat V−1 times
```

So:

**Same distance/relaxation concept, different way of processing the graph.**

## 16. Codeforces/XM mindset

Do not memorize the code as random syntax.

Build it from the job each part has:

```text
V, E → how much graph data exists?
edges[] → where do I store the given edges?
dist[] → where do I store source-to-node distances?
source → where does the calculation start?
V−1 loop → how many full passes?
Edge loop → what do I scan each pass?
if → can this edge make the destination cheaper?
update → save the cheaper distance
extra edge loop + if → can anything STILL improve?
```

This is the reusable Codeforces pattern: **understand the job → choose the variable → choose the loop → write the condition.**
