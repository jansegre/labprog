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
        int padding;
        padding = in.read();
        currentBit = 1 << (7 - padding);
        currentByte = in.read();
    }

    @Override
    public int read() throws IOException {
        HuffmanNode<Byte> node = tree.getRoot();
        while (!node.isLeaf()) {
            if (currentBit == 0) {
                int b = in.read();
                if (b != -1) currentByte = b;
                else return b;
                currentBit = 1 << 7;
            }
            boolean bit = (currentByte & currentBit) == currentBit;
            node = bit? node.getRightChild() : node.getLeftChild();
            currentBit >>= 1;
        }
        // the following is a hack to prevent the integer value of return
        // from being -1, since 0x100 is greater than any byte the sum as an
        // integer will always be positive, the error that happened before
        // was when a byte 0xff was read from the symbol the return value was -1
        // meaning the end of input even though it wasn't
        return 0x100 + node.getSymbol();
    }
}
