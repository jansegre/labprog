package huffman;

import java.util.*;

// this class is a generic huffman tree of nodes with symbol S
public class HuffmanTree<S> implements Iterable<HuffmanNode<S>> {
    protected HuffmanNode<S> root;
    protected Set<HuffmanNode<S>> leaves;
    protected Map<S, Vector<Boolean>> paths;
    protected int padding;

    // mount the huffman tree from a given collection of nodes
    // which are pair of symbols and frequencies
    public HuffmanTree(Collection<HuffmanNode<S>> nodes) {
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
            paths.put(node.symbol, prev);
            padding = (padding + node.frequency * prev.size()) % 8;
        } else {
            Vector<Boolean> left_list = new Vector<>(prev);
            left_list.add(false);
            cacheLeafs(node.left_child, left_list);
            Vector<Boolean> right_list = new Vector<>(prev);
            right_list.add(true);
            cacheLeafs(node.right_child, right_list);
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
                if (current.right_child != null)
                    stack.push(current.right_child);
                next = current.left_child;
                return current;
            }
        };
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
