# Technical Reflection & Complexity Analysis — Lab 8

---

## 1. Handout Question 1: Two-Child Deletion Mechanism & Invariant Preservation

### Question
*How does deletion of a node with two children operate, how is the in-order successor selected and replaced, and how are successor child references preserved without corrupting the Binary Search Tree invariants?*

### Detailed Analysis & Implementation Mechanics

When deleting a node $N$ that possesses two non-empty children ($N.left \neq \text{null}$ and $N.right \neq \text{null}$), $N$ cannot simply be spliced out of the tree because it anchors two distinct subtrees. To maintain the fundamental BST ordering invariant:
$$\forall k_L \in \text{subtree}(N.left), \quad k_L < N.key < \forall k_R \in \text{subtree}(N.right)$$
the replacement node must have a key strictly greater than all keys in $N.left$ and strictly less than all other keys in $N.right$.

#### Selection of the In-Order Successor
The replacement node chosen is the **in-order successor**, defined as the node with the smallest key strictly greater than $N.key$.
In a BST, this node is located by moving once to the right child ($N.right$) and then following `left` pointers until reaching a node whose `left` reference is `null`:
```java
private BSTNode<K, V> minNode(BSTNode<K, V> node) {
    if (node.left == null) {
        return node;
    }
    return minNode(node.left);
}
```
By definition:
1. The successor has **no left child** (`successor.left == null`).
2. The successor may have a **right child** (`successor.right`), or it may be a leaf.

#### Preserving the Successor's Right Child
Removing the successor from its original position without losing its right child is accomplished recursively via `deleteMin`:
```java
private BSTNode<K, V> deleteMin(BSTNode<K, V> node) {
    if (node.left == null) {
        // node is the minimum/successor; bypass it by returning its right child
        return node.right;
    }
    node.left = deleteMin(node.left);
    return node;
}
```
When `deleteMin` encounters the successor (`node.left == null`), it returns `node.right`. The recursive caller (the parent of the successor) assigns its `left` reference to this returned right child.

#### Two-Child Replacement Flow
In `delete(BSTNode<K, V> node, K key)`:
```java
BSTNode<K, V> successor = minNode(node.right);
node.key = successor.key;
node.value = successor.value;
node.right = deleteMin(node.right);
```
1. `minNode(node.right)` locates the successor without mutating any references.
2. The target node's `key` and `value` are updated with `successor.key` and `successor.value`.
3. `deleteMin(node.right)` unlinks the original successor node from the right subtree while reattaching the successor's right subtree to the successor's parent.
4. The target node's `left` reference remains completely untouched, preserving the entire left subtree.

#### Mandatory Test Case Trace
Consider the tree constructed from inserting `[40, 20, 70, 10, 30, 55, 85, 60]`:

```text
            40 (target root to delete)
          /    \
        20      70
       /  \    /  \
     10   30  55   85
                \
                 60 (successor's right child)
```

1. Target is `40`, which has two children (`20` and `70`).
2. `minNode(70)` traverses `70 -> 55`. Since `55.left == null`, successor is `55`.
3. Successor `55` has a right child `60`.
4. Node `40` is overwritten with key `55` and value `55`.
5. `deleteMin(70)` is invoked:
   - At node `70`: `70.left` is `55`. Recurses on `55`.
   - At node `55`: `55.left` is `null`. Returns `55.right` (`60`).
   - At node `70`: sets `70.left = 60` and returns `70`.
6. Root `node.right` receives `70`.

Resulting Tree:
```text
            55 (new root)
          /    \
        20      70
       /  \    /  \
     10   30  60   85
```

- **Invariants preserved:** In-order traversal remains strictly ascending: `[10, 20, 30, 55, 60, 70, 85]`.
- **Node preservation:** Node `60` was not lost; it was promoted to `70.left`.
- **Acyclic:** No self-references or cycles were created.
- **Tree metrics:** Size decremented from 8 to 7; height changed from 3 to 2.

---

## 2. Handout Question 2: Complexity Analysis & Limitations of Unbalanced Trees

### Question
*What are the theoretical and empirical time complexities of operations in an unbalanced Binary Search Tree, how does insertion order dictate tree height, and what are the limitations of relying on an unbalanced BST versus balanced variants (such as AVL or Red-Black trees)?*

### Detailed Complexity Analysis

| Operation | Best Case | Average Case (Random) | Worst Case (Degenerate) | Space (Call Stack) |
|---|---|---|---|---|
| `put(K, V)` | $\mathcal{O}(1)$ (root) | $\mathcal{O}(\log n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `get(K)` | $\mathcal{O}(1)$ (root) | $\mathcal{O}(\log n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `delete(K)` | $\mathcal{O}(1)$ (root leaf) | $\mathcal{O}(\log n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `rangeSearch(min, max)` | $\mathcal{O}(1)$ (empty/out) | $\mathcal{O}(\log n + k)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `inOrder()` | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `preOrder()` | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `postOrder()` | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `height()` | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |
| `size()` | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | $\mathcal{O}(h)$ |

*Note:* $h$ is the tree height measured in edges; $k$ is the number of elements falling within the query range $[min, max]$.

### Balanced vs. Degenerate Comparison (8 Keys)

#### Scenario A: Balanced Insertion Order
Inserting keys `[40, 20, 70, 10, 30, 55, 85, 60]`:
- **Tree Structure:**
  - Root: `40` (depth 0)
  - Depth 1: `20`, `70`
  - Depth 2: `10`, `30`, `55`, `85`
  - Depth 3: `60`
- **Tree Height:** `3` edges.
- **Lookup Path for Key `60`:**
  1. `40` $\rightarrow$ compare: $60 > 40$ (go right)
  2. `70` $\rightarrow$ compare: $60 < 70$ (go left)
  3. `55` $\rightarrow$ compare: $60 > 55$ (go right)
  4. `60` $\rightarrow$ compare: $60 == 60$ (target found)
  - **Comparisons / Nodes Visited:** 4 nodes (3 edges traversed).
  - Search performance aligns with $\mathcal{O}(\log_2 n) = \lceil \log_2(8) \rceil = 3$.

#### Scenario B: Sorted Insertion Order (Degenerate)
Inserting keys `[10, 20, 30, 40, 50, 60, 70, 80]` sequentially:
- **Tree Structure:**
  ```text
  10
    \
     20
       \
        30
          \
           40
             \
              50
                \
                 60
                   \
                    70
                      \
                       80
  ```
- **Tree Height:** `7` edges ($n - 1$).
- **Lookup Path for Key `80`:**
  1. `10` $\rightarrow$ go right
  2. `20` $\rightarrow$ go right
  3. `30` $\rightarrow$ go right
  4. `40` $\rightarrow$ go right
  5. `50` $\rightarrow$ go right
  6. `60` $\rightarrow$ go right
  7. `70` $\rightarrow$ go right
  8. `80` $\rightarrow$ found
  - **Comparisons / Nodes Visited:** 8 nodes (7 edges traversed).
  - Search performance completely degrades to linear scanning $\mathcal{O}(n)$.

### Limitations of an Unbalanced BST

1. **Pathological Degradation:**
   An unbalanced BST provides no guarantees on tree height. Under ordered, reverse-ordered, or patterned input data, the tree degrades into a linear linked list ($\mathcal{O}(n)$ search, insertion, and deletion), forfeiting the primary efficiency advantages of tree structures.
2. **Call Stack Overhead and Recursion Limits:**
   Because operations are implemented recursively, the execution stack depth equals the path length from root to target, which is $\mathcal{O}(h)$. In degenerate trees where $h = n - 1$, deep trees will exhaust available stack space and trigger `StackOverflowError` on large datasets ($n \ge 10^4$).
3. **Contrast with Self-Balancing Trees:**
   Self-balancing binary search trees (such as AVL trees using height-balancing rotations or Red-Black trees using color constraints and rotations) strictly enforce $h = \mathcal{O}(\log n)$ through tree rotations during insertions and deletions. This guarantees worst-case $\mathcal{O}(\log n)$ performance regardless of insertion order, avoiding degenerate linear chains.
