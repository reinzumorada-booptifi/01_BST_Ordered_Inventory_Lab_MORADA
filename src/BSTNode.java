/**
 * Generic node for Binary Search Tree.
 *
 * @param <K> the type of keys maintained by this node, must be Comparable
 * @param <V> the type of mapped values
 */
public class BSTNode<K extends Comparable<? super K>, V> {
    public K key;
    public V value;
    public BSTNode<K, V> left;
    public BSTNode<K, V> right;

    public BSTNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }
}
