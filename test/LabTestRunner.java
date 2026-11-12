import java.util.*;

/**
 * Dependency-free Java acceptance test runner for Lab 8 — Binary Search Tree.
 *
 * Exercises all required contract behaviors, edge cases, BST invariants,
 * and the mandatory student-authored test.
 */
public class LabTestRunner {

    @FunctionalInterface
    interface TestCase {
        void run() throws Throwable;
    }

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Lab 8 - Binary Search Tree Acceptance Tests     ");
        System.out.println("==================================================");

        runTest("testEmptyTree", LabTestRunner::testEmptyTree);
        runTest("testSingleNodeTree", LabTestRunner::testSingleNodeTree);
        runTest("testInsertions", LabTestRunner::testInsertions);
        runTest("testDuplicateKeyReplacement", LabTestRunner::testDuplicateKeyReplacement);
        runTest("testSearch", LabTestRunner::testSearch);
        runTest("testMissingSearch", LabTestRunner::testMissingSearch);
        runTest("testRangeSearch", LabTestRunner::testRangeSearch);
        runTest("testInclusiveRangeBoundaries", LabTestRunner::testInclusiveRangeBoundaries);
        runTest("testInvertedRanges", LabTestRunner::testInvertedRanges);
        runTest("testInOrderTraversal", LabTestRunner::testInOrderTraversal);
        runTest("testPreOrderTraversal", LabTestRunner::testPreOrderTraversal);
        runTest("testPostOrderTraversal", LabTestRunner::testPostOrderTraversal);
        runTest("testHeightAndSize", LabTestRunner::testHeightAndSize);
        runTest("testLeafDeletion", LabTestRunner::testLeafDeletion);
        runTest("testOneChildDeletion", LabTestRunner::testOneChildDeletion);
        runTest("testTwoChildDeletion", LabTestRunner::testTwoChildDeletion);
        runTest("testRootDeletion", LabTestRunner::testRootDeletion);
        runTest("testSuccessorWithRightChild", LabTestRunner::testSuccessorWithRightChild);
        runTest("testNullInputHandling", LabTestRunner::testNullInputHandling);
        runTest("testSortedInsertionDegenerateTree", LabTestRunner::testSortedInsertionDegenerateTree);
        runTest("testDeterministicRandomized", LabTestRunner::testDeterministicRandomized);
        runTest("testDeleteMissingKey", LabTestRunner::testDeleteMissingKey);
        runTest("testDeleteAllNodes", LabTestRunner::testDeleteAllNodes);
        runTest("testGenericTypes", LabTestRunner::testGenericTypes);
        runTest("testStudentAuthored_DeleteRoot40Successor55RightChild60", LabTestRunner::testStudentAuthored_DeleteRoot40Successor55RightChild60);

        System.out.println("==================================================");
        System.out.println("Test Execution Summary:");
        System.out.println("  Total Tests : " + (passed + failed));
        System.out.println("  Passed      : " + passed);
        System.out.println("  Failed      : " + failed);
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    private static void runTest(String testName, TestCase testCase) {
        try {
            testCase.run();
            passed++;
            System.out.println("[PASS] " + testName);
        } catch (Throwable t) {
            failed++;
            System.out.println("[FAIL] " + testName + " -- " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    // =========================================================================
    // Acceptance Test Cases
    // =========================================================================

    public static void testEmptyTree() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        assertEquals(0, tree.size(), "empty tree size must be 0");
        assertEquals(-1, tree.height(), "empty tree height must be -1");
        assertNull(tree.get(10), "get on empty tree must return null");
        assertFalse(tree.delete(10), "delete on empty tree must return false");
        assertEquals(Collections.emptyList(), tree.inOrder(), "empty inOrder must be empty list");
        assertEquals(Collections.emptyList(), tree.preOrder(), "empty preOrder must be empty list");
        assertEquals(Collections.emptyList(), tree.postOrder(), "empty postOrder must be empty list");
        assertEquals(Collections.emptyList(), tree.rangeSearch(5, 15), "empty rangeSearch must be empty list");
    }

    public static void testSingleNodeTree() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(10, "ten");
        assertEquals(1, tree.size(), "single-node tree size must be 1");
        assertEquals(0, tree.height(), "single-node tree height must be 0");
        assertEquals("ten", tree.get(10), "get on root must return value");
        assertEquals(Arrays.asList("ten"), tree.inOrder(), "inOrder of single node");
        assertEquals(Arrays.asList("ten"), tree.preOrder(), "preOrder of single node");
        assertEquals(Arrays.asList("ten"), tree.postOrder(), "postOrder of single node");
    }

    public static void testInsertions() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {50, 30, 70, 20, 40, 60, 80};
        for (int k : keys) {
            tree.put(k, k * 10);
        }
        assertEquals(7, tree.size(), "size after 7 insertions");
        assertEquals(2, tree.height(), "height of balanced 7-node tree");
        for (int k : keys) {
            assertEquals(k * 10, tree.get(k), "get value for key " + k);
        }
        assertEquals(Arrays.asList(200, 300, 400, 500, 600, 700, 800), tree.inOrder(), "inOrder values must be sorted");
    }

    public static void testDuplicateKeyReplacement() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(40, "root-v1");
        tree.put(20, "left-v1");
        tree.put(60, "right-v1");
        assertEquals(3, tree.size(), "initial size");

        // Replace root
        tree.put(40, "root-v2");
        assertEquals(3, tree.size(), "size unchanged on duplicate root");
        assertEquals("root-v2", tree.get(40), "value updated on duplicate root");

        // Replace internal / leaf
        tree.put(20, "left-v2");
        assertEquals(3, tree.size(), "size unchanged on duplicate key");
        assertEquals("left-v2", tree.get(20), "value updated on duplicate key");

        tree.put(60, "right-v2");
        assertEquals(3, tree.size(), "size unchanged on duplicate key");
        assertEquals("right-v2", tree.get(60), "value updated on duplicate key");

        assertEquals(Arrays.asList("left-v2", "root-v2", "right-v2"), tree.inOrder(), "traversal has updated values");
    }

    public static void testSearch() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(50, "fifty");
        tree.put(25, "twenty-five");
        tree.put(75, "seventy-five");
        tree.put(10, "ten");
        tree.put(90, "ninety");

        assertEquals("fifty", tree.get(50), "search root");
        assertEquals("twenty-five", tree.get(25), "search left child");
        assertEquals("seventy-five", tree.get(75), "search right child");
        assertEquals("ten", tree.get(10), "search left leaf");
        assertEquals("ninety", tree.get(90), "search right leaf");
    }

    public static void testMissingSearch() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(50, "fifty");
        tree.put(25, "twenty-five");
        tree.put(75, "seventy-five");

        assertNull(tree.get(5), "key smaller than minimum");
        assertNull(tree.get(100), "key larger than maximum");
        assertNull(tree.get(35), "key between existing keys");
        assertNull(tree.get(60), "key between existing keys");
    }

    public static void testRangeSearch() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }
        // Range 25..60 should include keys 30, 40, 55, 60
        List<Integer> result = tree.rangeSearch(25, 60);
        assertEquals(Arrays.asList(30, 40, 55, 60), result, "range 25..60");

        // Range covering all keys
        List<Integer> all = tree.rangeSearch(0, 100);
        assertEquals(Arrays.asList(10, 20, 30, 40, 55, 60, 70, 85), all, "full range");

        // Range completely outside
        assertEquals(Collections.emptyList(), tree.rangeSearch(0, 5), "range below min");
        assertEquals(Collections.emptyList(), tree.rangeSearch(90, 100), "range above max");
    }

    public static void testInclusiveRangeBoundaries() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int k : new int[]{10, 20, 30, 40, 50}) {
            tree.put(k, k);
        }
        // Exact inclusive endpoints
        assertEquals(Arrays.asList(20, 30, 40), tree.rangeSearch(20, 40), "both boundaries present");
        // Single key inclusive
        assertEquals(Arrays.asList(30), tree.rangeSearch(30, 30), "min == max when present");
        assertEquals(Collections.emptyList(), tree.rangeSearch(25, 25), "min == max when absent");
        // Non-existent boundaries but overlapping elements
        assertEquals(Arrays.asList(20, 30, 40), tree.rangeSearch(15, 45), "bounds absent but overlapping");
    }

    public static void testInvertedRanges() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int k : new int[]{10, 20, 30, 40, 50}) {
            tree.put(k, k);
        }
        assertEquals(Collections.emptyList(), tree.rangeSearch(50, 20), "inverted range (50, 20) must return empty list");
        assertEquals(Collections.emptyList(), tree.rangeSearch(30, 10), "inverted range (30, 10) must return empty list");
    }

    public static void testInOrderTraversal() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }
        assertEquals(Arrays.asList(10, 20, 30, 40, 55, 60, 70, 85), tree.inOrder(), "in-order traversal");
    }

    public static void testPreOrderTraversal() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }
        assertEquals(Arrays.asList(40, 20, 10, 30, 70, 55, 60, 85), tree.preOrder(), "pre-order traversal");
    }

    public static void testPostOrderTraversal() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }
        assertEquals(Arrays.asList(10, 30, 20, 60, 55, 85, 70, 40), tree.postOrder(), "post-order traversal");
    }

    public static void testHeightAndSize() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        assertEquals(0, tree.size());
        assertEquals(-1, tree.height());

        tree.put(50, "fifty");
        assertEquals(1, tree.size());
        assertEquals(0, tree.height());

        tree.put(25, "twenty-five");
        assertEquals(2, tree.size());
        assertEquals(1, tree.height());

        tree.put(75, "seventy-five");
        assertEquals(3, tree.size());
        assertEquals(1, tree.height());

        tree.put(10, "ten");
        assertEquals(4, tree.size());
        assertEquals(2, tree.height());

        // Duplicate replacement doesn't affect height or size
        tree.put(50, "FIFTY");
        assertEquals(4, tree.size());
        assertEquals(2, tree.height());
    }

    public static void testLeafDeletion() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int k : new int[]{50, 25, 75, 10}) {
            tree.put(k, k);
        }
        assertEquals(4, tree.size());
        assertEquals(2, tree.height());

        // 10 is a leaf
        assertTrue(tree.delete(10), "delete leaf 10 must return true");
        assertNull(tree.get(10), "10 should be deleted");
        assertEquals(3, tree.size(), "size decremented after leaf deletion");
        assertEquals(1, tree.height(), "height updated after leaf deletion");
        assertEquals(Arrays.asList(25, 50, 75), tree.inOrder(), "in-order after leaf deletion");
    }

    public static void testOneChildDeletion() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int k : new int[]{50, 25, 75, 10, 60, 70}) {
            tree.put(k, k);
        }
        // 60 has one child 70 (right child)
        assertTrue(tree.delete(60), "delete node with right child");
        assertNull(tree.get(60));
        assertEquals(70, tree.get(70));
        assertEquals(Arrays.asList(10, 25, 50, 70, 75), tree.inOrder());

        // 25 has one child 10 (left child)
        assertTrue(tree.delete(25), "delete node with left child");
        assertNull(tree.get(25));
        assertEquals(10, tree.get(10));
        assertEquals(Arrays.asList(10, 50, 70, 75), tree.inOrder());
    }

    public static void testTwoChildDeletion() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int k : new int[]{50, 25, 75, 10, 30, 60, 90}) {
            tree.put(k, k);
        }
        // 25 has two children: 10 and 30. Successor is 30 (leaf)
        assertTrue(tree.delete(25), "delete internal node with two children");
        assertNull(tree.get(25));
        assertEquals(6, tree.size());
        assertEquals(Arrays.asList(10, 30, 50, 60, 75, 90), tree.inOrder());
    }

    public static void testRootDeletion() {
        // Root in single-node tree
        BinarySearchTree<Integer, String> tree1 = new BinarySearchTree<>();
        tree1.put(100, "hundred");
        assertTrue(tree1.delete(100));
        assertNull(tree1.get(100));
        assertEquals(0, tree1.size());
        assertEquals(-1, tree1.height());

        // Root with only left child
        BinarySearchTree<Integer, Integer> tree2 = new BinarySearchTree<>();
        tree2.put(50, 50);
        tree2.put(25, 25);
        assertTrue(tree2.delete(50));
        assertEquals(1, tree2.size());
        assertEquals(0, tree2.height());
        assertEquals(25, tree2.get(25));

        // Root with only right child
        BinarySearchTree<Integer, Integer> tree3 = new BinarySearchTree<>();
        tree3.put(50, 50);
        tree3.put(75, 75);
        assertTrue(tree3.delete(50));
        assertEquals(1, tree3.size());
        assertEquals(0, tree3.height());
        assertEquals(75, tree3.get(75));

        // Root with two children
        BinarySearchTree<Integer, Integer> tree4 = new BinarySearchTree<>();
        tree4.put(50, 50);
        tree4.put(25, 25);
        tree4.put(75, 75);
        assertTrue(tree4.delete(50));
        assertEquals(2, tree4.size());
        assertEquals(Arrays.asList(25, 75), tree4.inOrder());
    }

    public static void testSuccessorWithRightChild() {
        // Edge case from PLAN trace 7.2:
        // Node 20 has right child 30, which has right child 40.
        // Delete 20 (successor is immediate right child 30, which has right child 40)
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        tree.put(20, 20);
        tree.put(10, 10);
        tree.put(30, 30);
        tree.put(40, 40);

        assertTrue(tree.delete(20), "delete node where successor is immediate right child with right child");
        assertNull(tree.get(20));
        assertEquals(3, tree.size());
        assertEquals(1, tree.height());
        assertEquals(Arrays.asList(10, 30, 40), tree.inOrder());
    }

    public static void testNullInputHandling() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(50, "fifty");

        // put with null key
        try {
            tree.put(null, "val");
            fail("put with null key must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        // put with null value
        try {
            tree.put(10, null);
            fail("put with null value must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        // get with null key
        try {
            tree.get(null);
            fail("get with null key must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        // delete with null key
        try {
            tree.delete(null);
            fail("delete with null key must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        // rangeSearch with null bounds
        try {
            tree.rangeSearch(null, 50);
            fail("rangeSearch with null min must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            tree.rangeSearch(50, null);
            fail("rangeSearch with null max must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        // Ensure tree state unchanged
        assertEquals(1, tree.size(), "size unchanged after rejected null operations");
        assertEquals("fifty", tree.get(50), "tree intact after rejected null operations");
    }

    public static void testSortedInsertionDegenerateTree() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        for (int i = 1; i <= 8; i++) {
            tree.put(i * 10, i * 10);
        }
        assertEquals(8, tree.size(), "size of degenerate tree");
        assertEquals(7, tree.height(), "height of degenerate tree must be n - 1 = 7");
        assertEquals(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80), tree.inOrder());
        for (int i = 1; i <= 8; i++) {
            assertEquals(i * 10, tree.get(i * 10));
        }
    }

    public static void testDeterministicRandomized() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        Random rng = new Random(42);
        Set<Integer> inserted = new HashSet<>();
        while (inserted.size() < 100) {
            int key = rng.nextInt(1000);
            if (inserted.add(key)) {
                tree.put(key, key);
            }
        }
        assertEquals(100, tree.size());

        // In-order traversal must be strictly ascending
        List<Integer> inOrder = tree.inOrder();
        assertEquals(100, inOrder.size());
        for (int i = 0; i < inOrder.size() - 1; i++) {
            assertTrue(inOrder.get(i) < inOrder.get(i + 1), "in-order must be strictly ascending");
        }

        // Delete 50 elements
        List<Integer> toDelete = new ArrayList<>(inserted);
        Collections.shuffle(toDelete, new Random(101));
        for (int i = 0; i < 50; i++) {
            int key = toDelete.get(i);
            assertTrue(tree.delete(key), "delete must return true for existing key");
            assertNull(tree.get(key), "deleted key must return null");
        }
        assertEquals(50, tree.size());

        List<Integer> inOrderAfter = tree.inOrder();
        assertEquals(50, inOrderAfter.size());
        for (int i = 0; i < inOrderAfter.size() - 1; i++) {
            assertTrue(inOrderAfter.get(i) < inOrderAfter.get(i + 1), "in-order must remain strictly ascending");
        }
    }

    public static void testDeleteMissingKey() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(50, "fifty");
        tree.put(25, "twenty-five");
        assertFalse(tree.delete(999), "delete missing key returns false");
        assertFalse(tree.delete(1), "delete missing key returns false");
        assertEquals(2, tree.size(), "size unchanged on failed delete");
        assertEquals(1, tree.height(), "height unchanged on failed delete");
    }

    public static void testDeleteAllNodes() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }
        for (int k : keys) {
            assertTrue(tree.delete(k), "delete key " + k);
        }
        assertEquals(0, tree.size(), "tree empty after deleting all nodes");
        assertEquals(-1, tree.height(), "height -1 after deleting all nodes");
        assertEquals(Collections.emptyList(), tree.inOrder(), "inOrder empty");
        assertEquals(Collections.emptyList(), tree.preOrder(), "preOrder empty");
        assertEquals(Collections.emptyList(), tree.postOrder(), "postOrder empty");
    }

    public static void testGenericTypes() {
        BinarySearchTree<String, Double> tree = new BinarySearchTree<>();
        tree.put("banana", 1.99);
        tree.put("apple", 0.99);
        tree.put("cherry", 3.49);

        assertEquals(3, tree.size());
        assertEquals(0.99, tree.get("apple"));
        assertEquals(1.99, tree.get("banana"));
        assertEquals(3.49, tree.get("cherry"));
        assertEquals(Arrays.asList(0.99, 1.99, 3.49), tree.inOrder());
    }

    /**
     * Mandatory Student-Authored Test:
     * Delete root 40 when successor 55 has right child 60.
     *
     * Setup:
     *   Insert: 40, 20, 70, 10, 30, 55, 85, 60
     *   Delete: 40
     *
     * Expected Results:
     *   - delete(40) = true
     *   - get(40) = null
     *   - get(55) retains its original value
     *   - get(60) retains its original value
     *   - size = 7
     *   - height = 2
     *   - inOrder = [10, 20, 30, 55, 60, 70, 85]
     *   - preOrder = [55, 20, 10, 30, 70, 60, 85]
     *   - postOrder = [10, 30, 20, 60, 85, 70, 55]
     *   - rangeSearch(25, 60) = [30, 55, 60]
     */
    public static void testStudentAuthored_DeleteRoot40Successor55RightChild60() {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        int[] keys = {40, 20, 70, 10, 30, 55, 85, 60};
        for (int k : keys) {
            tree.put(k, k);
        }

        // Initial state verification before delete
        assertEquals(8, tree.size(), "initial size before delete");
        assertEquals(3, tree.height(), "initial height before delete");
        assertEquals(Arrays.asList(10, 20, 30, 40, 55, 60, 70, 85), tree.inOrder());
        assertEquals(Arrays.asList(40, 20, 10, 30, 70, 55, 60, 85), tree.preOrder());
        assertEquals(Arrays.asList(10, 30, 20, 60, 55, 85, 70, 40), tree.postOrder());

        // Perform root deletion
        boolean deleteResult = tree.delete(40);

        // Required assertions
        assertTrue(deleteResult, "delete(40) must return true");
        assertNull(tree.get(40), "get(40) must return null");
        assertEquals(Integer.valueOf(55), tree.get(55), "get(55) retains its original value");
        assertEquals(Integer.valueOf(60), tree.get(60), "get(60) retains its original value");
        assertEquals(7, tree.size(), "size = 7");
        assertEquals(2, tree.height(), "height = 2");
        assertEquals(Arrays.asList(10, 20, 30, 55, 60, 70, 85), tree.inOrder(), "inOrder = [10, 20, 30, 55, 60, 70, 85]");
        assertEquals(Arrays.asList(55, 20, 10, 30, 70, 60, 85), tree.preOrder(), "preOrder = [55, 20, 10, 30, 70, 60, 85]");
        assertEquals(Arrays.asList(10, 30, 20, 60, 85, 70, 55), tree.postOrder(), "postOrder = [10, 30, 20, 60, 85, 70, 55]");
        assertEquals(Arrays.asList(30, 55, 60), tree.rangeSearch(25, 60), "rangeSearch(25, 60) = [30, 55, 60]");
    }

    // =========================================================================
    // Assertion Utilities
    // =========================================================================

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            String msg = (message != null ? message + " — " : "")
                    + "expected: <" + expected + ">, but was: <" + actual + ">";
            throw new AssertionError(msg);
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message != null ? message : "expected condition to be true");
        }
    }

    private static void assertTrue(boolean condition) {
        assertTrue(condition, null);
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message != null ? message : "expected condition to be false");
        }
    }

    private static void assertFalse(boolean condition) {
        assertFalse(condition, null);
    }

    private static void assertNull(Object actual, String message) {
        if (actual != null) {
            throw new AssertionError((message != null ? message + " — " : "") + "expected null, but was: <" + actual + ">");
        }
    }

    private static void assertNull(Object actual) {
        assertNull(actual, null);
    }

    private static void fail(String message) {
        throw new AssertionError(message);
    }
}
