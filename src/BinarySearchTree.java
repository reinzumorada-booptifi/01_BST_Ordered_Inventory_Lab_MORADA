import java.util.ArrayList;
import java.util.List;

/**
 * Generic Binary Search Tree implementation.
 *
 * All operations are implemented recursively adhering to the approved
 * API contract and behavioral conventions.
 *
 * @param <K> the type of keys, must be Comparable
 * @param <V> the type of values
 */
public class BinarySearchTree<K extends Comparable<? super K>, V> {
    private BSTNode<K, V> root;
    private boolean deleted;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a key-value pair into the tree.
     * If the key already exists, updates the value without increasing size.
     *
     * @param key   the key to insert (non-null)
     * @param value the value to associate with the key (non-null)
     * @throws IllegalArgumentException if key or value is null
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        root = put(root, key, value);
    }

    private BSTNode<K, V> put(BSTNode<K, V> node, K key, V value) {
        if (node == null) {
            return new BSTNode<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key to search for (non-null)
     * @return the value associated with key, or null if not found
     * @throws IllegalArgumentException if key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return get(root, key);
    }

    private V get(BSTNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    /**
     * Performs a range search returning all values whose keys fall in [min, max],
     * in ascending key order. Prunes subtrees outside the range.
     *
     * @param min inclusive lower bound (non-null)
     * @param max inclusive upper bound (non-null)
     * @return list of values in ascending key order; empty list if none match or inverted
     * @throws IllegalArgumentException if min or max is null
     */
    public List<V> rangeSearch(K min, K max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Range bounds cannot be null");
        }
        List<V> result = new ArrayList<>();
        if (min.compareTo(max) > 0) {
            return result;
        }
        rangeSearch(root, min, max, result);
        return result;
    }

    private void rangeSearch(BSTNode<K, V> node, K min, K max, List<V> result) {
        if (node == null) {
            return;
        }
        if (min.compareTo(node.key) < 0) {
            rangeSearch(node.left, min, max, result);
        }
        if (min.compareTo(node.key) <= 0 && node.key.compareTo(max) <= 0) {
            result.add(node.value);
        }
        if (max.compareTo(node.key) > 0) {
            rangeSearch(node.right, min, max, result);
        }
    }

    /**
     * Deletes the key and its associated value from the tree.
     * In the two-child case, replaces with the in-order successor and
     * preserves the successor's right child.
     *
     * @param key the key to delete (non-null)
     * @return true if key was found and removed, false otherwise
     * @throws IllegalArgumentException if key is null
     */
    public boolean delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        deleted = false;
        root = delete(root, key);
        return deleted;
    }

    private BSTNode<K, V> delete(BSTNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            deleted = true;
            // Case 1: No left child (includes leaf case where right is also null)
            if (node.left == null) {
                return node.right;
            }
            // Case 2: No right child
            if (node.right == null) {
                return node.left;
            }
            // Case 3: Two children -> find in-order successor (min in right subtree)
            BSTNode<K, V> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = deleteMin(node.right);
        }
        return node;
    }

    private BSTNode<K, V> minNode(BSTNode<K, V> node) {
        if (node.left == null) {
            return node;
        }
        return minNode(node.left);
    }

    private BSTNode<K, V> deleteMin(BSTNode<K, V> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return node;
    }

    /**
     * Returns a list of all values in in-order (ascending key order).
     *
     * @return list of values in ascending key order
     */
    public List<V> inOrder() {
        List<V> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        inOrder(node.left, result);
        result.add(node.value);
        inOrder(node.right, result);
    }

    /**
     * Returns a list of all values in pre-order (root, left, right).
     *
     * @return list of values in pre-order
     */
    public List<V> preOrder() {
        List<V> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preOrder(node.left, result);
        preOrder(node.right, result);
    }

    /**
     * Returns a list of all values in post-order (left, right, root).
     *
     * @return list of values in post-order
     */
    public List<V> postOrder() {
        List<V> result = new ArrayList<>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        postOrder(node.left, result);
        postOrder(node.right, result);
        result.add(node.value);
    }

    /**
     * Computes the height of the tree in edges.
     * Empty tree has height -1; single-node tree (leaf) has height 0.
     *
     * @return height in edges
     */
    public int height() {
        return height(root);
    }

    private int height(BSTNode<K, V> node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Computes the number of nodes (unique keys) in the tree.
     *
     * @return count of nodes
     */
    public int size() {
        return size(root);
    }

    private int size(BSTNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }
}
