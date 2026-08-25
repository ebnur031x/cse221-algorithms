# Dijkstra — My Understanding Notes

> These are my own understanding notes from the way I learned Dijkstra step by step. Keep the wording simple. The goal is to be able to rebuild the algorithm in my head and then translate it to Java.

## 1. The big idea

Dijkstra is basically:

**From one starting node (here A), find the shortest distance to every other node.**

Everything is rooted in the starting node A.

When I say:

- `dist[A] = 0` → distance from A to A is 0.
- `dist[C] = 1` → the current best-known distance **from A to C** is 1.
- `dist[D] = 4` → the current best-known distance **from A to D** is 4.

So `dist[X]` always means:

**A → X**, not X → something else.

---

## 2. Initial setup

At the beginning:

```text
A = 0
B = ∞
C = ∞
D = ∞
```

Why?

- We know the distance from A to itself: `0`.
- We don't know the distance to the others yet, so use infinity (`∞`) as "currently no path known."

We also have:

```text
visited[A] = false
visited[B] = false
visited[C] = false
visited[D] = false
```

`visited = true` means the shortest distance for that node has been **finalized/locked**.

Important:

**Having a distance value does NOT mean the node is finalized.**

Example:

```text
A = 0 ✓
B = 4
C = 1 ✓
D = 6
```

B and D have values, but only A and C are finalized.

---

## 3. Dijkstra is NOT physically walking

This was confusing at first.

If I am processing C, that does NOT mean I physically walked back through A or physically moved to B.

Dijkstra is calculating possible routes.

For example, if I know:

```text
A → C = 1
C → D = 3
```

then I calculate:

```text
A → C → D = 1 + 3 = 4
```

Later, when checking an edge like D → C, I might calculate a hypothetical:

```text
A → ... → D → C
```

That does not mean I actually walked backwards. I am only asking:

**"Would reaching C through D give me a shorter distance?"**

---

## 4. The main Dijkstra cycle

The whole algorithm keeps repeating this:

```text
1. Find the smallest-distance unvisited node.
2. Call it u.
3. Mark u visited/finalized.
4. Look at u's neighbors/edges.
5. Relax each neighbor.
6. Repeat.
```

The important rule for choosing `u`:

**Pick the smallest distance among ALL unvisited nodes.**

If two nodes tie, either one is okay. There is no hidden preference.

---

## 5. What "visited/finalized" really means

When a node is selected as the smallest unvisited node and marked visited, its current distance is considered the shortest possible distance.

So:

**visited = shortest distance finalized.**

But before that, a distance can still change.

Example:

```text
D = 6
```

Later I find:

```text
A → C → D = 4
```

So:

```text
D: 6 → 4
```

D was not visited yet, so its value was allowed to improve.

---

## 6. Relaxation = compare → update

This is the most important calculation.

Suppose I am at/processing A and there is an edge:

```text
A → B (weight 4)
```

Current:

```text
dist[A] = 0
dist[B] = ∞
e.weight = 4
```

Possible new path:

```text
dist[A] + edge weight
= 0 + 4
= 4
```

Compare it with B's current value:

```text
4 < ∞
```

TRUE → update:

```text
dist[B] = 4
```

So relaxation means:

> **"If this new path is shorter than the current/best-known path, replace the old value with the new one."**

The Java code is:

```java
if (dist[u] + e.weight < dist[e.to]) {
    dist[e.to] = dist[u] + e.weight;
}
```

Simple translation:

```text
new path < current path?
        ↓
      yes
        ↓
update current path
```

---

## 7. `dist[u]` — lifetime rule

This is important:

**`dist[u]` is the distance from the original start A to the current node `u`.**

If:

```text
u = A
```

then:

```text
dist[u] = dist[A] = 0
```

If:

```text
u = C
```

then:

```text
dist[u] = dist[C] = 1
```

That `1` means:

```text
A → C = 1
```

It does NOT mean C → A.

So when processing C and checking C → D (weight 3):

```text
dist[C] + 3
= 1 + 3
= 4
```

This represents:

```text
A → C → D = 4
```

---

## 8. `e`, `e.to`, and `e.weight`

For now, think of `e` as the current edge/connection being examined.

Example:

```text
A → B (weight 4)
```

Then:

```text
e        = the whole edge A → B
e.to     = B (where the edge goes)
e.weight = 4 (cost/weight of the edge)
```

So:

```java
dist[e.to]
```

means:

**the current distance of the node this edge goes to.**

For A → B:

```text
e.to = B
dist[e.to] = dist[B]
```

Important distinction:

```text
e.to       → which node?
e.weight   → what is the edge cost?
dist[e.to] → what is the current best distance to that node from A?
```

`e.to` is NOT itself a distance.

---

## 9. The two important inner loops

### Loop 1 — scan all nodes to find `u`

```java
for (int i = 0; i < 4; i++) {
```

This is just normal scanning:

```text
A → B → C → D
```

It does NOT mean Dijkstra visits them in that order.

Inside it:

```java
if (!visited[i] && dist[i] < min) {
    min = dist[i];
    u = i;
}
```

Simple meaning:

> **"If this node is not visited AND its distance is smaller than the best candidate I have found so far, make it my new candidate."**

`i` = current index/node being checked.

`dist[i]` = distance stored for that node.

Example:

```text
index:   0   1   2   3
node:    A   B   C   D
dist:    0   4   1   6
```

So `i = 1` means B, and `dist[i]` means `dist[B] = 4`.

### Loop 2 — scan only the current node's edges

```java
for (Edge e : graph[u]) {
```

Simple meaning:

> **"For every edge connected to the current node `u`, give me that edge one at a time."**

If `u = A`:

```text
graph[A]
  ↓
A→B(4)
A→C(1)
```

First iteration:

```text
e = A→B(4)
```

Second iteration:

```text
e = A→C(1)
```

`e` is the same variable, but it holds a different edge on each iteration.

Unlike a normal loop with `i++`, a for-each loop automatically moves to the next item.

---

## 10. Outer loop

There is also an outer loop:

```java
for (int step = 0; step < 4; step++) {
```

This repeats the whole Dijkstra cycle.

Think:

```text
step 0 → find u → finalize → relax
step 1 → find u → finalize → relax
step 2 → find u → finalize → relax
step 3 → find u → finalize → relax
```

So there are three levels of looping:

```text
OUTER LOOP
  ↓
repeat Dijkstra's main cycle

INNER LOOP #1
  ↓
scan all nodes → find smallest unvisited u

NEIGHBOR LOOP
  ↓
scan only u's edges → relax them
```

---

## 11. Full Java skeleton I understand now

```java
int[] dist = {0, INF, INF, INF};
boolean[] visited = {false, false, false, false};

for (int step = 0; step < 4; step++) {

    // Find smallest unvisited node
    int min = INF;
    int u = -1;

    for (int i = 0; i < 4; i++) {
        if (!visited[i] && dist[i] < min) {
            min = dist[i];
            u = i;
        }
    }

    // Finalize it
    visited[u] = true;

    // Relax its neighbors
    for (Edge e : graph[u]) {
        if (dist[u] + e.weight < dist[e.to]) {
            dist[e.to] = dist[u] + e.weight;
        }
    }
}
```

I should think of this code as my manual process translated into Java, NOT as a bunch of random syntax.

---

## 12. Full trace we did

Example graph:

```text
A ──4── B
│       │
1       2
│       │
C ──3── D
```

Start:

```text
A=0, B=∞, C=∞, D=∞
```

### Step 0 — choose A

A is the smallest unvisited.

Finalize A.

Relax A's edges:

```text
A→B = 4 → B becomes 4
A→C = 1 → C becomes 1
```

Now:

```text
A=0 ✓
B=4
C=1
D=∞
```

### Step 1 — choose B or C?

C=1 is smaller, so choose C.

Finalize C.

Relax C's edges:

```text
C→A = 1
1 + 1 = 2
2 < dist[A]=0? NO
```

Then:

```text
C→D = 3
1 + 3 = 4
4 < dist[D]=∞? YES
```

So:

```text
D = 4
```

### Step 2 — remaining B and D

B=4 and D=4 are tied.

Either can be chosen. There is no hidden reason B is special.

In our trace, B was chosen.

Finalize B.

Check B→A:

```text
4 + 4 < 0? NO
```

Check B→D:

```text
4 + 2 = 6
6 < 4? NO
```

D stays 4.

### Final

D is the only unvisited node, so finalize D.

Final shortest distances from A:

```text
A = 0
C = 1
B = 4
D = 4
```

---

## 13. The biggest things I was confused about — now clear

### "If I set D=6, why haven't I visited D?"

Because **discovered distance ≠ finalized distance**.

`dist[D]=6` only means:

> "I currently know a route to D costing 6."

It can later become 4.

### "Are we physically moving C → A → B?"

No.

The algorithm is calculating possible routes. It does not physically travel around the graph.

### "Why can D check C again?"

Because relaxation asks:

> "Would going through D give C a shorter distance?"

Usually it won't. That's okay.

### "Why is `dist[C]` 1 when I'm processing C?"

Because `dist[C]` means:

```text
A → C = 1
```

Always from the original start A.

### "Why are there so many loops?"

Each has a different job:

```text
outer loop     = repeat the Dijkstra cycle
node loop      = find the next smallest unvisited node
edge loop      = process the current node's neighbors
```

---

## 14. My one-line mental model

**Dijkstra = keep the best-known distance from A, always finalize the smallest unvisited one, then try using it to make its neighbors cheaper.**

And relaxation is simply:

**new path < old path? → update.**

---

## 15. Parked for later

I deliberately did NOT dive deeply into these while building the core logic:

- What the Java `Edge` class actually looks like and how I create an Edge object.
- What `graph[]` actually is.
- How an adjacency list stores all those `Edge` objects.
- Why Dijkstra works mathematically / why the finalized node is safe.
- Time complexity.
- PriorityQueue version of Dijkstra.
- Bellman-Ford and how it differs from Dijkstra.

These should be learned after the core Dijkstra flow is stable.
