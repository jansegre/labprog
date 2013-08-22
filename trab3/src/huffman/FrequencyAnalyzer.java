package huffman;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FrequencyAnalyzer<S> {
    Map<S, Integer> frequencies;

    public FrequencyAnalyzer() {
        frequencies = new HashMap<>();
    }

    public void feed(S symbol) {
        Integer f = frequencies.get(symbol);
        if (f == null)
            f = 0;
        frequencies.put(symbol, ++f);
    }

    public void feed(InputStream stream) throws IOException {
        throw new AbstractMethodError();
    }

    // used to feed by char with text streams
    static public class CharFrequencyAnalyzer extends FrequencyAnalyzer<Character> {
        @Override
        public void feed(InputStream stream) throws IOException {
            int c;
            while ((c = stream.read()) != -1)
                feed((char)c);
        }
    }

    // used to feed by byte with byte streams
    static public class ByteFrequencyAnalyzer extends FrequencyAnalyzer<Byte> {
        @Override
        public void feed(InputStream stream) throws IOException {
            try {
                int c;
                while ((c = stream.read()) != -1)
                    feed((byte)c);
            } finally {
                stream.close();
            }
        }
    }

    // convert the current evaluated frequency pairs to a collection of nodes
    public Collection<HuffmanNode<S>> nodeCollection() {
        Stack<HuffmanNode<S>> nodes = new Stack<>();
        for (S symbol: frequencies.keySet()) {
            nodes.push(new HuffmanNode<>(frequencies.get(symbol), symbol));
        }
        return nodes;
    }

    public HuffmanTree<S> toHuffmanTree() {
        return new HuffmanTree<>(nodeCollection());
    }

    public String toString() {
        String out = "<FrequencyAnalyzer:";
        for (HuffmanNode<S> node: nodeCollection())
            out += " " + node;
        out += ">";
        return out;
    }
}
