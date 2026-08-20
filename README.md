# CSE221 — Algorithms

Coursework for CSE221 (Algorithms): judge-style problem sets, solved mostly
in Java. This repo is updated as new assignments are released.

## Structure

| Folder | Assignment | Status |
|---|---|---|
| [`_A1D`](_A1D) | Assignment 1, Problem D — non-decreasing check | ✅ done |
| [`Assignment2`](Assignment2) | Assignment 2 — divide & conquer, BIT, modular arithmetic (A–F) | ✅ done (F incomplete) |
| [`A4`](A4) | Assignment 4 — graph representations (A–H) | ✅ done |
| [`A5`](A5) | Assignment 5 | 🚧 not started |

Each problem lives in its own folder as a standalone judge submission: a
`Main.java` (or `Solution.java`) that reads input from stdin and writes
output to stdout. Per-assignment READMEs summarize what each problem asks
and the approach used to solve it.

## Running a solution

Every solution is a single-file Java program:

```bash
cd A4/A
javac Main.java
java Main < sample_input.txt
```

## Notes

- Compiled `.class` files and course reference materials (PDFs, slides) are
  intentionally not tracked — see `.gitignore`.
- Folder naming (`_A1D`, `A4`, `Assignment2`, ...) mirrors what the judge /
  course site uses for each assignment.
