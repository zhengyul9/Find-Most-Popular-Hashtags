// Fibonacci Node Structure
public class NodeStructure {
    String key; // hashtag
    int val; // frequency
    NodeStructure left;
    NodeStructure right;
    NodeStructure child;
    NodeStructure parent;
    boolean mark;
    int degree;

    public NodeStructure (String hashtag, int k) {
        key = hashtag;
        val = k;
        parent = null;
        child = null;
        left = this;
        right = this;
        degree = 0;
        mark = false;
    }
}
