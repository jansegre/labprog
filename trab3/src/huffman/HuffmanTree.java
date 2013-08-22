package huffman;

import java.nio.ByteBuffer;
import java.util.*;

// this class is a generic huffman tree of nodes with symbol S
public class HuffmanTree<S> implements Iterable<HuffmanNode<S>> {
    protected HuffmanNode<S> root;
    protected Set<HuffmanNode<S>> leaves;
    protected Map<S, Vector<Boolean>> paths;
    protected int padding;

    // cached input
    protected Collection<HuffmanNode<S>> originalNodes;

    // mount the huffman tree from a given collection of nodes
    // which are pair of symbols and frequencies
    public HuffmanTree(Collection<HuffmanNode<S>> nodes) {
        originalNodes = new Vector<>(nodes);
        PriorityQueue<HuffmanNode<S>> queue = new PriorityQueue<>(nodes);
        while (queue.size() > 1)
            queue.add(queue.poll().add(queue.poll()));
        root = queue.poll();
        paths = new HashMap<>(256, 1.0f);
        leaves = new HashSet<>(256, 1.0f);
        padding = 0;
        cacheLeafs(root, new Vector<Boolean>());
        padding = 8 - padding;
    }

    void cacheLeafs(HuffmanNode<S> node, Vector<Boolean> prev) {
        if (node.isLeaf()) {
            leaves.add(node);
            paths.put(node.getSymbol(), prev);
            padding = (padding + node.getFrequency() * prev.size()) % 8;
        } else {
            Vector<Boolean> left = new Vector<>(prev);
            left.add(false);
            cacheLeafs(node.getLeftChild(), left);
            Vector<Boolean> right = new Vector<>(prev);
            right.add(true);
            cacheLeafs(node.getRightChild(), right);
        }
    }
    
    public Vector<Boolean> pathFor(S symbol) {
        return paths.get(symbol);
    }

    public Collection<S> getSymbols() {
        return paths.keySet();
    }

    public Set<HuffmanNode<S>> getLeaves() {
        return leaves;
    }

    public int getPadding() {
        return padding;
    }

    public HuffmanNode<S> getRoot() {
        return root;
    }

    // iterate in pre-order
    // this is the standard interface for iterables, although
    // it is only used for printing we could use it for more general operations
    // without being tied to a string representation or bloating the memory with
    // a copy of the nodes
    public Iterator<HuffmanNode<S>> iterator() {
        final HuffmanNode<S> initial_node = this.root;
        return new Iterator<HuffmanNode<S>>(){
            HuffmanNode<S> current = null;
            HuffmanNode<S> next = initial_node;
            Stack<HuffmanNode<S>> stack = new Stack<>();

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return next != null || !stack.isEmpty();
            }

            public HuffmanNode<S> next() {
                current = next;
                if (current == null) {
                    if (stack.isEmpty()) return null;
                    else current = stack.pop();
                }
                if (current.getRightChild() != null)
                    stack.push(current.getRightChild());
                next = current.getLeftChild();
                return current;
            }
        };
    }

    static public class ByteHuffmanTree extends HuffmanTree<Byte> {
        public ByteHuffmanTree(Collection<HuffmanNode<Byte>> nodes) {
            super(nodes);
        }
        public ByteHuffmanTree(byte[] bytes) {
            super(fromBytes(bytes));
        }

        static private Collection<HuffmanNode<Byte>> fromBytes(byte[] bytes) {
            Stack<HuffmanNode<Byte>> nodes = new Stack<>();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            while(true) {
                int frequency = buffer.getInt();
                if (frequency == 0) break;
                byte symbol = buffer.get();
                nodes.push(new HuffmanNode<Byte>(frequency, symbol));
            }
            return nodes;
        }

        @Override
        public byte[] toByteArray() {
            ByteBuffer buffer = ByteBuffer.allocate(5 * originalNodes.size() + 4);
            for (HuffmanNode<Byte> node: originalNodes) {
                buffer.putInt(node.getFrequency());
                buffer.put(node.getSymbol());
            }
            buffer.putInt(0);
            return buffer.array();
        }
    }

    public byte[] toByteArray() {
        throw new AbstractMethodError();
    }

    public String toString() {
        String out = "<HuffmanTree:";
        out += " _" + padding + "_";
        for (HuffmanNode<S> node: this)
            out += " " + node;
        out += ">";
        return out;
    }
}
