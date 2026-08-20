# Assignment 4 — Graph Representations

Java solutions for a set of judge problems on basic graph representations and
grid/number-theory graph queries.

| Problem | Description | Idea |
|---|---|---|
| [A](A/Main.java) | Read a weighted edge list, print the weighted adjacency matrix | Direct array fill |
| [B](B/Main.java) | Read a weighted edge list, print the weighted adjacency list | Array of lists, one per node |
| [C](C/Main.java) | Read an unweighted adjacency list, print the adjacency matrix | Direct array fill |
| [D](D/Main.java) | Decide whether an undirected graph has an Eulerian path | Count odd-degree vertices (must be 0 or 2) |
| [E](E/Main.java) | Print in-degree minus out-degree for every vertex of a directed graph | Two degree arrays |
| [F](F/Main.java) | List all valid king moves from a square on an `n x n` board, sorted | Check the 8 offsets against the board bounds |
| [G](G/Main.java) | Given a set of points, decide whether any two are a knight's move apart | Hash the coordinates, probe the 8 knight offsets |
| [H](H/Main.java) | On the graph where `i ~ j` iff `gcd(i, j) = 1`, answer k-th neighbor queries | Precompute neighbor lists with `gcd`, answer by index |

Each problem folder is a standalone judge submission (`Main.java`, reads
stdin / writes stdout).
