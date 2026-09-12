# DFS → Topological Sort → Cycle Detection — My Confusion Notes

## What I struggled with

I was rusty because I had studied these topics 2–3 weeks ago but had not practiced them. The biggest confusion was understanding what the arrows meant when a new edge was added and how a cycle is actually formed.

## What confused me

### 1. Cycle vs. edge
I first thought the new edge `7 → 3` itself was a cycle.

Then it clicked: **one edge is not a cycle.** A cycle must come back to where it started.

So after adding `7 → 3`, I look for paths from `3 → 7`.

- `3 → 4 → 5 → 7`
- `3 → 6 → 7`

The new edge closes both:

- `3 → 4 → 5 → 7 → 3`
- `3 → 6 → 7 → 3`

**Mental model:** New edge `7 → 3` → look for paths `3 → 7` → every different path gives a different cycle.

### 2. Do I need BFS/DFS to find the cycles?
No. For a question like this, I can inspect the graph and trace paths. If DFS is being used, a directed edge to a vertex still in the recursion stack is a back edge and indicates a cycle.

### 3. Question (d) — merging cycles
I initially thought each cycle should become its own node.

Then I understood that overlapping cyclic vertices can be treated as one merged node. Here the cyclic group is:

`[3, 4, 5, 6, 7]`

This means those five original vertices are treated as **one single vertex/node** in the new graph. Vertices `1` and `2` stay as individual nodes. The outside connections are preserved when redrawing the graph.

## The thing that finally clicked

**Cycle:** find a path that returns to the starting vertex.

**New edge:** see whether it closes existing paths into cycles.

**Merge:** replace the cyclic group with one node, written like `[3,4,5,6,7]` for convenience.

## Exam-ready mental model

`New edge → find return paths → list every cycle → merge the cyclic group → redraw the graph.`
