package huffman;

// this class is a generic huffman node for a symbol S
// it's basically a pair (symbol, frequency) with the ability
// to relate to other nodes in a treeish manner
public class HuffmanNode<S> implements Comparable<HuffmanNode<S>> {
    // number of ocurrences of the symbol
    // could be a floating point, but the number
    // is usually well bounded and integer arithmetics
    // has better performance
    protected Integer frequency;

    public Integer getFrequency() {
        return frequency;
    }

    // this can be a single char, this node is generic
    // enough to support a symbol
    protected S symbol;

    public S getSymbol() {
        return symbol;
    }

    // this objects are used to traverse the binary tree
    private HuffmanNode<S> leftChild, rightChild;

    public HuffmanNode<S> getLeftChild() {
        return leftChild;
    }

    public HuffmanNode<S> getRightChild() {
        return rightChild;
    }

    // constructors

    public HuffmanNode(int f) {
        frequency = f;
    }

    public HuffmanNode(int f, S s) {
        this(f);
        symbol = s;
    }

    // we implement the comparable interface to be able to use
    // the native java priority queue more easily
    public int compareTo(HuffmanNode other) {
        return this.frequency.compareTo(other.frequency);
    }

    // this operation is used to mount the queue with internal nodes
    // notice that the symbol is null
    public HuffmanNode<S> add(HuffmanNode<S> other) {
        HuffmanNode<S> node = new HuffmanNode<>(this.frequency + other.frequency);
        node.leftChild = this;
        node.rightChild = other;
        return node;
    }

    // this will be used to check if we should stop on the tree hierarchy and
    // return the symbol found on the leaf
    // a simple version would be to check if the symbol is null, but the formal
    // definition of leaf is used instead
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    public String toString() {
        String out = frequency.toString();
        if (symbol != null)
            out += " {" + symbol + "}";
        return out;
    }
}
