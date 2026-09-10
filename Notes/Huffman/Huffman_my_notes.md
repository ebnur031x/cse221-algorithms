# Huffman Coding — My Understanding Notes

> Personal note from the session: the goal here is not to memorize Huffman mechanically. I want to be able to look at a Huffman problem and immediately know what is happening without my mind getting cluttered.

## Where I started / what confused me

At first, the Huffman tree itself was the confusing part. I understood that we repeatedly add frequencies, but I was unsure what the summed numbers meant and what the 0/1 values were doing.

The important breakthrough was realizing:

- Every time I add two nodes, **their sum becomes the parent node**.
- The two things I added become that parent's children.
- The original characters/frequencies stay at the leaves.
- The summed numbers are **not new characters**; they are combined frequencies of the subtree underneath them.

For example:

```text
2 + 3 = 5
5 + 7 = 12
12 + 10 = 22
```

becomes:

```text
          22
         /  \
       12    10
      /  \
     5    7
    / \
   2   3
```

That made the tree construction click.

---

## 1. The greedy rule — the one thing I must remember

**Always take the TWO smallest available frequencies/nodes.**

Then:

**merge them → put the sum back → repeat.**

Example:

```text
2, 3, 4, 6, 7, 8, 10

2 + 3 = 5
4 + 5 = 9
6 + 7 = 13
8 + 9 = 17
10 + 13 = 23
17 + 23 = 40
```

The root is 40.

### Mental shortcut

> **Two smallest → merge → put back → repeat.**

Do not lose this rule when the numbers change.

---

## 2. Building the tree

Every merge creates a parent:

```text
      5
     / \
    2   3
```

because:

```text
2 + 3 = 5
```

Then if 5 and 7 are the next two smallest:

```text
       12
      /  \
     5    7
    / \
   2   3
```

because:

```text
5 + 7 = 12
```

So my mental model is simply:

> **Whatever two nodes I merge, their sum sits above them as the parent.**

---

## 3. Left/right and 0/1

This was another point of confusion.

The **0 and 1 are NOT frequency nodes and NOT character nodes.**
They are just **labels on the branches**.

Normally:

- **Left branch = 0**
- **Right branch = 1**

Example:

```text
      5
     / \
   0/   \\1
   2     3
```

The 0/1 tells me which direction I took.

### Important

A character does **not inherently belong to 0 or 1**.
Its code depends on where that character is placed in the tree.

If the problem gives a left/right rule, follow it.
If it does not, I can choose a consistent left/right arrangement. Swapping equal-frequency/tied arrangements can change the actual bit patterns without changing the optimal cost.

---

## 4. Finding a character's codeword

This became clear once I stopped thinking of the 0/1s as nodes.

To find a character's code:

> **Start at the root and follow the path to that character. Write 0 for every left turn and 1 for every right turn.**

Example:

```text
          22
        /    \
      12      10
     /  \
    5    7
   / \
  A   B
```

For A:

```text
22 → 12 → 5 → A
 0    0    0
```

So:

```text
A = 000
```

For B:

```text
22 → 12 → 5 → B
 0    0    1
```

So:

```text
B = 001
```

### The key thought

> **Where is my character? Follow the path to it. The directions become the code.**

I do NOT choose branches randomly. The location of the character in the tree determines the path.

---

## 5. Encoding vs decoding

This distinction is now clear:

### Encoding

**Character → code**

```text
B → 001
A → 000
```

If the message is `BA`:

```text
B → 001
A → 000

BA → 001000
```

So encoding means replacing each character with its Huffman code and putting the codes together.

### Decoding

**Code → character**

Example:

```text
001 → B
```

Start at the root and use each bit as a direction until reaching a character/leaf.

### One-line memory

> **Encoding = character → code.**
> **Decoding = code → character.**

---

## 6. Total number of bits

If a character appears many times, its code is used many times.

Two terms matter:

- **Frequency** = how many times the character appears.
- **Code length** = number of bits in that character's Huffman code.

Therefore:

> **Total bits = sum of (frequency × code length) for all characters.**

I do NOT need to write the Σ symbol in the exam. I can simply calculate each character:

```text
A: frequency × code length
B: frequency × code length
C: frequency × code length
...

Then add them.
```

Example:

```text
A appears 1 time, code = 000 → 1 × 3 = 3 bits
B appears 2 times, code = 001 → 2 × 3 = 6 bits

Total = 9 bits
```

### Mental shortcut

> **How often? × How long? = bits used.**

---

## 7. Exam-style full Huffman problem

A common question pattern is:

1. Given characters and frequencies.
2. Construct the Huffman tree.
3. Write the codeword for every character.
4. Sometimes encode/decode a message.
5. Calculate total bits/storage.
6. Sometimes answer a short conceptual question about greedy choice, optimality, ties, or fixed-length encoding.

### Worked construction I completed

Given:

```text
A=2, B=3, C=4, D=6, E=7, F=8, G=10
```

Merges:

```text
2 + 3 = 5
4 + 5 = 9
6 + 7 = 13
8 + 9 = 17
10 + 13 = 23
17 + 23 = 40
```

One valid tree arrangement I built was:

```text
             40
           /    \
         17      23
        /  \    /  \
       F    9  G    13
           / \     / \
          C   5   D   E
             / \
            A   B
```

Using left = 0 and right = 1, the resulting codewords were:

```text
A = 0110
B = 0111
C = 010
D = 110
E = 111
F = 00
G = 10
```

The important part is not memorizing these codes. The important part is being able to **rebuild the tree and trace the paths**.

---

## 8. Greedy property and optimality

I already knew the greedy construction mechanically. The extra conceptual point is **why it matters**.

### Greedy property

> At every step, Huffman greedily chooses the two least-frequent nodes and merges them.

### Optimality

> Those greedy choices produce an **optimal prefix code**, meaning the total weighted code length is minimized among valid prefix-free codes.

Mental distinction:

> **Greedy = how Huffman chooses.**
> **Optimal = the quality of the final result: minimum total weighted bits.**

Do not overthink this for a short exam question.

---

## 9. Tie cases

A tie means two or more available nodes have the same frequency.

If there are equal-frequency choices, I can have different valid choices/left-right arrangements.

This can cause:

- different actual 0/1 codewords
- but the same relevant code lengths/optimal total cost

### Exam sentence

> **When frequencies are tied, different valid choices or left/right arrangements may produce different codewords, but the optimal cost remains the same.**

The key idea is: **different-looking Huffman trees do not automatically mean one is wrong.**

---

## 10. Fixed-length vs Huffman

### Fixed-length

Every character gets the same number of bits.

For 8 possible characters:

```text
2³ = 8
```

so fixed-length encoding uses **3 bits per character**.

### Huffman

Codes can have different lengths:

```text
frequent character → shorter code
rare character     → longer code
```

This can reduce the total number of bits when frequencies are unequal.

### Exam answer

> **Fixed-length encoding uses the same number of bits for every character, while Huffman encoding uses variable-length codes based on frequency. Huffman can require fewer total bits because frequent characters receive shorter codes.**

---

## 11. A conceptual question about a friend's Huffman implementation

If an exam gives someone else's finished codewords and asks what they did wrong and what the consequence is, I should NOT automatically say:

> “A and B are too long.”

Long codes for rare characters are actually expected in Huffman.

The safer reasoning is:

> **Check whether the given code lengths/tree follow the Huffman construction for the supplied frequencies. If not, the resulting code is not the required optimal Huffman code and may use more bits, reducing compression efficiency.**

For a 3-mark answer, give:

1. the implementation/construction mistake,
2. the consequence for code lengths/total bits,
3. the consequence for compression/storage efficiency.

No full simulation is needed unless the question explicitly asks for one.

---

## 12. Storage/bit-limit questions

If the question asks which character(s) fit within a specific bit limit, do not confuse the limit with the code length.

The relevant calculation is:

```text
frequency × code length
```

Then compare the result with the stated limit.

Example from the practice problem with an 11-bit limit:

```text
A: 2 × 4 = 8   → within 11
B: 3 × 4 = 12  → not within 11
...
```

So the process is:

> **Calculate required bits → compare with limit.**

---

# My final Huffman mental model

When I see a Huffman question, my brain should run this sequence:

```text
FREQUENCIES
     ↓
Take TWO smallest
     ↓
MERGE → SUM becomes parent
     ↓
Put sum back
     ↓
Repeat until ROOT
     ↓
Draw LEFT / RIGHT
     ↓
Left = 0, Right = 1
     ↓
Trace root → character
     ↓
Get CODEWORD
     ↓
Character → code = ENCODING
Code → character = DECODING
     ↓
Frequency × code length
     ↓
TOTAL BITS
```

## What I actually need to remember

**1. Two smallest.**

**2. Sum becomes parent.**

**3. 0/1 are branch directions, not nodes.**

**4. Follow the path to get a character's code.**

**5. Encoding = character → code; decoding = code → character.**

**6. Total bits = frequency × code length, added across characters.**

**7. Greedy choice = two smallest; optimality = minimum total weighted bits.**

**8. Ties can change bit patterns without changing optimal cost.**

**9. Fixed-length = same bits per character; Huffman = variable lengths based on frequency.**

---

# Personal checkpoint

At the beginning I was getting stuck on what the summed numbers meant, whether 0/1 were nodes, and whether I was supposed to choose branches when finding a character. The main thing that cleared the clutter was reducing everything to **movement through a tree**:

> **The tree tells me where to go. 0/1 records which direction I went.**

Once that clicked, encoding, decoding, and bit calculation became straightforward.

**Huffman basics: DONE.**

I do not need to spend hours here now. I can come back later for a quick exam-style revision, but I should move on to the next topic.
