package huffman;

// this class is a generic huffman node for a symbol S
// it's basically a pair (symbol, frequency) with the ability
// to relate to other nodes in a treeish manner
public class HuffmanNode<S> implements Comparable<HuffmanNode<S>> {
    // number of ocurrences of the symbol
    // could be a floating point, but the number
    // is usually well bounded and integer arithmetics
    // has better performance
    Integer frequency;
    // this can be a single char, this node is generic
    // enough to support a symbol
    S symbol;

    // this objects are used to traverse the tree
    // only one of the parents should be set, the other must be null
    // this structure is used to easily know if a node
    // is a right or left child of its parent, i.e.,
    // in case it has a left parent, then it is a right child
    HuffmanNode left_child, right_child;
    HuffmanNode left_parent, right_parent;

    public HuffmanNode(int frequency) {
        this.frequency = frequency;
    }

    public HuffmanNode(int frequency, S symbol) {
        this(frequency);
        this.symbol = symbol;
    }

    // we implement the comparable interface to be able to use
    // the native java priority queue more easily
    public int compareTo(HuffmanNode other) {
        return this.frequency.compareTo(other.frequency);
    }

    // this operation is used to mount the queue with internal nodes
    // notice that the symbol is null
    public HuffmanNode add(HuffmanNode other) {
        HuffmanNode node = new HuffmanNode(this.frequency + other.frequency);
        // this node
        node.left_child = this;
        this.right_parent = node;
        // the other node
        node.right_child = other;
        other.left_parent = node;
        return node;
    }

    // this will be used to check if we should stop on the tree hierarchy and
    // return the symbol found on the leaf
    // a simple version would be to check if the symbol is null, but the formal
    // definition of leaf is used instead
    public boolean isLeaf() {
        return left_child == null && right_child == null;
    }

    public String toString() {
        String out = frequency.toString();
        if (symbol != null)
            out += " {" + symbol + "}";
        return out;
    }
}
