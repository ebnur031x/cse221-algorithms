# Assignment 2 — Divide & Conquer / Modular Arithmetic

Java solutions for a set of judge problems on divide-and-conquer algorithms,
Binary Indexed Trees, and modular exponentiation.

| Problem | Description | Idea |
|---|---|---|
| [A](A/Main.java) | Sort an array and count inversions | Merge sort, counting cross-inversions during the merge step |
| [B](B/Main.java) | Count pairs `(i, j)` with `i < j` and `A[i] > A[j]^2` | Coordinate compression + Binary Indexed Tree, scanned right to left, with a binary search for the threshold rank |
| [C](C/Main.java) | Compute `a^b mod 107` | Fast (binary) exponentiation |
| [D](D/Main.java) | Raise a 2x2 matrix to the `x`-th power mod `1e9+7`, for multiple queries | Binary exponentiation over 2x2 matrices |
| [E](E/Main.java) | Compute `a^1 + a^2 + ... + a^n mod MOD`, for multiple queries | Divide-and-conquer geometric sum: `S(n) = S(n/2) * (1 + a^(n/2))` for even `n` |
| [F](F/Main.java) | Compute `a^b mod 107` | ⚠️ **Incomplete** — calls a `power(...)` helper that was never written; does not compile |

Each problem folder is a standalone judge submission (`Main.java`, reads
stdin / writes stdout).
