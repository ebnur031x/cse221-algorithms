# Codeforces Java I/O — My Learning Notes

These are notes from the part where I was confused about how a competitive-programming Java solution actually starts and ends. The algorithm can be understood, but I also need to know how to read the question's input and print the answer.

## My overall mental flow

```text
Question gives input
↓
Read input
↓
Split values
↓
Store graph/data
↓
Run algorithm
↓
Collect output
↓
Print answer
```

For me, the reusable pattern is:

> **read → split → calculate → collect → print**

---

# 1. BufferedReader

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
```

At first this line looks scary, but mentally I don't need to reinvent it every time.

Think:

> **This creates my fast input reader.**

It is setup. It does not magically give me `V` or `E` yet.

Then:

```java
br.readLine()
```

means:

> Read one whole line of input as a String.

Example input:

```text
5 6
```

After:

```java
br.readLine()
```

I basically have:

```text
"5 6"
```

as one String.

For multiple input lines, I call `br.readLine()` again.

My mental pipeline:

```text
BufferedReader setup
↓
br.readLine()
↓
one complete line as String
```

---

# 2. StringTokenizer

Suppose:

```java
br.readLine()
```

gives:

```text
"5 6"
```

But I need separate values.

So:

```java
StringTokenizer st = new StringTokenizer(br.readLine());
```

Think:

> **st takes the line and separates it into tokens, usually by spaces.**

So:

```text
"5 6"
 ↓
"5"   "6"
```

Then:

```java
st.nextToken()
```

takes them one by one.

Example:

```java
int V = Integer.parseInt(st.nextToken());
int E = Integer.parseInt(st.nextToken());
```

Important: `nextToken()` gives text/String, so:

```java
Integer.parseInt(...)
```

converts it to an integer.

My exact understanding:

1. Set up fast reader.
2. Read one line.
3. Tokenizer splits the line.
4. Take tokens one by one.
5. Convert/store them.

---

# 3. Reading edges — what I was thinking

For Bellman-Ford, the input gives edges like:

```text
u v w
```

meaning:

```text
from → to, weight
```

I need to prepare the graph data before Bellman-Ford can work.

Mental picture:

```text
Read V and E
↓
Define what one Edge looks like
↓
Create storage for E edges
↓
Read E edge lines
↓
Now Bellman-Ford has its data
```

If I use a constructor:

```java
static class Edge {
    int u, v, w;

    Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
}
```

then my natural style is:

```java
for (int i = 0; i < E; i++) {
    st = new StringTokenizer(br.readLine());

    int u = Integer.parseInt(st.nextToken());
    int v = Integer.parseInt(st.nextToken());
    int w = Integer.parseInt(st.nextToken());

    edges[i] = new Edge(u, v, w);
}
```

I specifically understood this as:

> **Read temporary values → create an Edge using constructor → store that Edge.**

This is cleaner for me than creating an empty object and assigning fields separately.

The loop:

```java
for (int i = 0; i < E; i++)
```

exists because the question gives **E edges**, so I need to read/store **E times**.

---

# 4. dist[] reminder

After storing the graph, Bellman-Ford needs somewhere to store shortest distances.

```java
int[] dist = new int[V + 1];
Arrays.fill(dist, INF);
dist[1] = 0;
```

In our example, nodes were numbered:

```text
1 ... V
```

So:

```text
dist[0] → unused
dist[1] → node 1
...
dist[V] → node V
```

Why fill with INF?

> At the start, I don't know paths to the nodes.

Why source = 0?

Not because the node itself is magically special. The distance from the starting node to itself is mathematically:

```text
source → source = 0
```

In our example, source was node 1, so:

```java
dist[1] = 0;
```

---

# 5. Output — StringBuilder

This was one of my confusing parts.

```java
StringBuilder sb = new StringBuilder();
```

Think:

> **An empty text box/container where I collect output.**

At first:

```text
sb = ""
```

Then:

```java
sb.append("Hello");
```

becomes:

```text
sb = "Hello"
```

I can keep adding:

```java
sb.append(dist[i]).append("\n");
```

Meaning:

1. Add `dist[i]`.
2. Add a newline.

So instead of immediately printing every answer, I can:

```text
calculate
↓
collect answers in sb
↓
print once
```

Finally:

```java
System.out.print(sb);
```

means:

> Print everything I collected.

For me:

```text
StringBuilder = collect/build output text
append() = add something to it
System.out.print(sb) = print everything
```

---

# 6. What is PrintWriter?

Another output tool:

```java
PrintWriter pw = new PrintWriter(System.out);
```

Think:

> **A printer connected to the console/output.**

Then:

```java
pw.println("Hello");
pw.println(5);
```

At the end:

```java
pw.flush();
```

means roughly:

> Make sure any buffered/waiting output is actually sent.

So I do NOT need to use both StringBuilder and PrintWriter every time.

Common choices:

### Style A

```java
StringBuilder sb = new StringBuilder();

sb.append("Hello\n");
sb.append("World\n");

System.out.print(sb);
```

### Style B

```java
PrintWriter pw = new PrintWriter(System.out);

pw.println("Hello");
pw.println("World");

pw.flush();
```

For now, I prefer:

```text
BufferedReader + StringTokenizer + StringBuilder
```

because the whole flow is easier for me to visualize.

---

# 7. My reusable Codeforces mental template

```text
INPUT
BufferedReader = read lines
StringTokenizer = split one line into values

DATA SETUP
arrays / Edge objects / graph storage

ALGORITHM
the actual logic

OUTPUT
StringBuilder = collect output
System.out.print = print it
```

The important thing for me is not to treat all of this as one giant memorized template.

I should ask:

> What job do I need right now?

- Need to read input? → BufferedReader
- Need separate values from a line? → StringTokenizer
- Need to store the graph? → array/list/Edge objects
- Need shortest answers? → dist[]
- Need to print lots of results cleanly? → StringBuilder

That way I can gradually rebuild the code instead of staring at a blank screen and forgetting what variable or loop comes next.

---

## Current Bellman-Ford progress checkpoint

I understand the overall structure now:

```text
Fast input setup
↓
Read V and E
↓
Store all E edges
↓
Create dist[]
↓
INF for unknown distances
↓
source distance = 0
↓
Bellman-Ford V−1 passes
↓
scan all edges and relax
↓
extra edge scan for negative cycle
↓
collect output
↓
print
```

### Biggest thing I need to remember about my own weakness

I often understand the algorithm but freeze at:

> "What variable do I declare?"  
> "Should I start a loop here?"  
> "What does this line connect to?"

So my approach should be:

> **First identify the job → then identify what data/variable is needed → then decide if repetition is needed → then write the loop/condition.**

Don't start from syntax. Start from **what the code needs to do**.
