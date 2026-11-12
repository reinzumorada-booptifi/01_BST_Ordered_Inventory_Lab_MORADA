# Lab 8 — Binary Search Tree (BST)

An ordered inventory index implementation utilizing a generic Binary Search Tree (BST) in Java.

---

## 1. Project Overview

This project provides a robust, generic Binary Search Tree data structure designed to maintain key-value pairs in sorted order by key. All tree operations—including insertion, search, range querying, deletion, traversals, height calculation, and size tracking—are implemented recursively without external third-party libraries.

---

## 2. Features

- **Generic Types:** Supports generic comparable keys (`K extends Comparable<? super K>`) and generic values (`V`).
- **Purely Recursive Implementation:** All fundamental operations (`put`, `get`, `rangeSearch`, `delete`, traversals, `height`, `size`) are implemented with clean recursive algorithms.
- **Ordered Range Queries:** `rangeSearch(min, max)` returns values in ascending key order while actively pruning subtrees outside the query range.
- **Safe Two-Child Deletion:** In-order successor replacement with pointer rewiring that guarantees the successor's right child is preserved, preventing node loss, cycles, or memory leaks.
- **Value Replacement:** Inserting an existing key updates the associated value without creating redundant nodes or modifying tree size/height.
- **Strict Null Safety:** Validates arguments upfront, throwing `IllegalArgumentException` on null inputs.
- **Comprehensive Dependency-Free Test Harness:** Built-in standalone acceptance test suite with 25 test cases verifying contract correctness, invariants, edge cases, and deterministic randomized scenarios.

---

## 3. Project Structure

```text
Lab 8/
├── src/
│   ├── BSTNode.java              # Generic BST node (key, value, left, right)
│   └── BinarySearchTree.java     # Full generic BST implementation
├── test/
│   └── LabTestRunner.java        # Standalone acceptance test suite (25 tests)
├── out/                          # Compiled bytecode classes
├── AGENT_LOG.md                  # Detailed phase logs (PLAN, TEST, BUILD, AUDIT)
├── README.md                     # Project overview and usage documentation
└── REFLECTION.md                 # Technical reflection and complexity analysis
```

---

## 4. API Specification

### Class: `BSTNode<K extends Comparable<? super K>, V>`
- `public K key`: The comparable search key.
- `public V value`: The associated value payload.
- `public BSTNode<K, V> left`: Reference to left child subtree.
- `public BSTNode<K, V> right`: Reference to right child subtree.
- `public BSTNode(K key, V value)`: Constructs a leaf node with null child pointers.

### Class: `BinarySearchTree<K extends Comparable<? super K>, V>`

| Method | Signature | Description |
|---|---|---|
| `put` | `void put(K key, V value)` | Inserts or updates the key-value pair. Throws `IllegalArgumentException` if key or value is null. |
| `get` | `V get(K key)` | Retrieves the value associated with `key`, or `null` if absent. Throws `IllegalArgumentException` if key is null. |
| `rangeSearch` | `List<V> rangeSearch(K min, K max)` | Returns all values with keys in `[min, max]` in ascending order. Inverted ranges return an empty list. Throws `IllegalArgumentException` if bounds are null. |
| `delete` | `boolean delete(K key)` | Removes `key` and returns `true`, or returns `false` if not found. Preserves successor's right subtree in two-child deletion. Throws `IllegalArgumentException` if key is null. |
| `inOrder` | `List<V> inOrder()` | Returns all values in ascending key order (Left-Root-Right). |
| `preOrder` | `List<V> preOrder()` | Returns all values in pre-order sequence (Root-Left-Right). |
| `postOrder` | `List<V> postOrder()` | Returns all values in post-order sequence (Left-Right-Root). |
| `height` | `int height()` | Returns tree height in edges (-1 for empty tree, 0 for single leaf). |
| `size` | `int size()` | Returns the total count of unique keys/nodes currently in the tree. |

---

## 5. Behavioral Conventions & Invariants

1. **Null Policy:**
   - `put(null, value)` and `put(key, null)` throw `IllegalArgumentException`.
   - `get(null)` and `delete(null)` throw `IllegalArgumentException`.
   - `rangeSearch(null, max)` and `rangeSearch(min, null)` throw `IllegalArgumentException`.
   - Tree state is never mutated when a null argument exception is thrown.
2. **Range Bounds:**
   - Queries are inclusive of both `min` and `max`.
   - Inverted ranges (`min > max`) return an empty list without throwing an exception.
3. **Duplicate Keys:**
   - If `put` receives a key that already exists (`cmp == 0`), the existing value is replaced with the new value. The tree size, height, and structure remain unchanged.
4. **Height Convention:**
   - Height is measured in **edges** along the longest root-to-leaf path.
   - An empty tree has height `-1`.
   - A single-node tree (root only) has height `0`.
   - An internal node has height `1 + max(height(left), height(right))`.
5. **Size Tracking:**
   - `size()` accurately reflects the number of distinct nodes currently reachable in the tree.

---

## 6. Compilation Instructions

Ensure a Java Development Kit (JDK 17 or higher, e.g., JDK 26) is available in your environment.

From the project root directory (`Lab 8/`), execute:

```powershell
javac -d out src/*.java test/*.java
```

This compiles all source files and test runners into the `out/` directory.

---

## 7. Execution and Test Instructions

To run the complete dependency-free acceptance test suite with Java assertions enabled (`-ea`):

```powershell
java -ea -cp out LabTestRunner
```

### Expected Output

```text
==================================================
  Lab 8 - Binary Search Tree Acceptance Tests     
==================================================
[PASS] testEmptyTree
[PASS] testSingleNodeTree
[PASS] testInsertions
[PASS] testDuplicateKeyReplacement
[PASS] testSearch
[PASS] testMissingSearch
[PASS] testRangeSearch
[PASS] testInclusiveRangeBoundaries
[PASS] testInvertedRanges
[PASS] testInOrderTraversal
[PASS] testPreOrderTraversal
[PASS] testPostOrderTraversal
[PASS] testHeightAndSize
[PASS] testLeafDeletion
[PASS] testOneChildDeletion
[PASS] testTwoChildDeletion
[PASS] testRootDeletion
[PASS] testSuccessorWithRightChild
[PASS] testNullInputHandling
[PASS] testSortedInsertionDegenerateTree
[PASS] testDeterministicRandomized
[PASS] testDeleteMissingKey
[PASS] testDeleteAllNodes
[PASS] testGenericTypes
[PASS] testStudentAuthored_DeleteRoot40Successor55RightChild60
==================================================
Test Execution Summary:
  Total Tests : 25
  Passed      : 25
  Failed      : 0
==================================================
```

Exit code `0` indicates that all 25 test cases have passed successfully.
