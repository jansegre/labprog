package huffman;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

// this class is a generic huffman tree of nodes with symbol S
public class HuffmanTree<S> implements Iterable<HuffmanNode<S>> {
    protected HuffmanNode<S> root;
    protected Set<HuffmanNode<S>> leaves;
    protected Map<S, boolean[]> paths;

    // cached input
    protected Collection<HuffmanNode<S>> originalNodes;

    // mount the huffman tree from a given collection of nodes
    // which are pair of symbols and frequencies
    public HuffmanTree(Collection<HuffmanNode<S>> nodes) {
        // make a copy of the given nodes to use it on the serialization
        originalNodes = new Vector<>(nodes);

        // append the null terminator, used to indicate end of stream, with lowest priority
        nodes.add(new HuffmanNode<S>(0, null));

        // build the tree as detailed here: http://en.wikipedia.org/wiki/Huffman_coding#Compression
        PriorityQueue<HuffmanNode<S>> queue = new PriorityQueue<>(nodes);
        while (queue.size() > 1)
            queue.add(queue.poll().add(queue.poll()));
        root = queue.poll();

        // cache some useful information to allow faster access
        // we predict a size of 256 plus the null terminator, and will only grow if it hits 100%
        paths = new HashMap<>(257, 1.0f);
        leaves = new HashSet<>(257, 1.0f);
        cacheLeaves(root, new boolean[0]);
    }

    // used internally to cache the leaves and each path to it
    private void cacheLeaves(HuffmanNode<S> node, boolean[] path) {
        // if we haven't hit a leaf yet
        if (!node.isLeaf()) {
            // recurse by copying forking the path
            // appended of a false to the left
            boolean[] leftPath = Arrays.copyOf(path, path.length + 1);
            leftPath[path.length] = false;
            cacheLeaves(node.getLeftChild(), leftPath);
            // and true to the right
            boolean[] rightPath = Arrays.copyOf(path, path.length + 1);
            rightPath[path.length] = true;
            cacheLeaves(node.getRightChild(), rightPath);
        } else {
            // found it, cache it
            leaves.add(node);
            // and its path
            paths.put(node.getSymbol(), path);
        }
    }

    // array if trues (rights) and falses (lefts) that lead to that symbol
    public boolean[] pathFor(S symbol) {
        return paths.get(symbol);
    }

    public Collection<S> getSymbols() {
        return paths.keySet();
    }

    public Collection<HuffmanNode<S>> getLeaves() {
        return leaves;
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

    // this implementation is useful when dealing with bytes
    static public class ByteHuffmanTree extends HuffmanTree<Byte> {
        public ByteHuffmanTree(Collection<HuffmanNode<Byte>> nodes) {
            super(nodes);
        }

        // construct by reading from a stream
        public ByteHuffmanTree(InputStream inputStream) throws IOException {
            this(fromBytes(inputStream));
        }

        // internal helper to run code prior to calling super/this
        static private Collection<HuffmanNode<Byte>> fromBytes(InputStream inputStream) throws IOException {
            Stack<HuffmanNode<Byte>> nodes = new Stack<>();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            while(true) {
                // basically read the frequencies
                int frequency = dataInputStream.readInt();
                // until zero frequency is found
                if (frequency == 0) break;
                // otherwise read the symbol that follows that frequency
                byte symbol = dataInputStream.readByte();
                nodes.push(new HuffmanNode<>(frequency, symbol));
            }
            return nodes;
        }

        // implementation of how to serialize a HuffmanTree<Byte>
        // this is kind of a workaround since java doesn't have specializations
        @Override
        public byte[] toByteArray() {
            // the basic idea is to store the nodes as we received it so when
            // we deserialize them the constructor build the tree in the same manner
            // as it was originally built
            ByteBuffer buffer = ByteBuffer.allocate(5 * originalNodes.size() + 4);
            for (HuffmanNode<Byte> node: originalNodes) {
                buffer.putInt(node.getFrequency());
                buffer.put(node.getSymbol());
            }
            // denote the end with a frequency of zero
            buffer.putInt(0);
            return buffer.array();
        }
    }

    // provide a fake abstract method to allow virtual calling of
    // it on special implementations such as ByteHuffmanTree
    public byte[] toByteArray() {
        throw new AbstractMethodError();
    }

    // will print <HuffmanTree: node node node leaf {symbol} node leaf {symbol} ... leaf {symbol}>
    public String toString() {
        String out = "<HuffmanTree:";
        for (HuffmanNode<S> node: this)
            out += " " + node;
        out += ">";
        return out;
    }
}
