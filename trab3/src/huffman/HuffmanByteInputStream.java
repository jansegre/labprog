package huffman;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HuffmanByteInputStream extends FilterInputStream {
    HuffmanTree<Byte> tree;
    int currentBit;
    int currentByte;

    public HuffmanByteInputStream(HuffmanTree<Byte> huffmanTree, InputStream inputStream) throws IOException {
        super(inputStream);
        tree = huffmanTree;
        int padding = in.read();
        currentBit = 1 << (7 - padding);
        currentByte = in.read();
    }

    @Override
    public int read() throws IOException {
        HuffmanNode<Byte> node = tree.getRoot();
        while (!node.isLeaf()) {
            if (currentBit == 0) {
                if ((currentByte = in.read()) == -1)
                    return in.read();
                currentBit = 1 << 7;
            }
            boolean bit = (currentByte & currentBit) == currentBit;
            node = bit? node.getRightChild() : node.getLeftChild();
            currentBit >>= 1;
        }
        return node.getSymbol();
    }
}
