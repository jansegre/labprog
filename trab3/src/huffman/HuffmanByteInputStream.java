package huffman;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HuffmanByteInputStream extends FilterInputStream {
    HuffmanTree<Byte> tree;
    int currentMask;
    int currentByte;

    public HuffmanByteInputStream(HuffmanTree<Byte> huffmanTree, InputStream inputStream) throws IOException {
        super(inputStream);
        tree = huffmanTree;
        int padding;
        // we start with the first byte, and a fresh mask
        currentByte = in.read();
        currentMask = 1 << 7;
    }

    @Override
    public int read() throws IOException {
        // to read a symbol we start from the root
        HuffmanNode<Byte> node = tree.getRoot();
        // until we find a leaf
        while (!node.isLeaf()) {
            // remembering to get the next byte when the current is over
            if (currentMask == 0) {
                int b = in.read();
                // check if we have reached the end of our input
                if (b != -1) currentByte = b;
                else return -1;
                // and reset the mask since we have a new byte
                currentMask = 1 << 7;
            }
            // walk the node for the value of the current bit
            node = node.getNextChild((currentByte & currentMask) == currentMask);
            // and shift the mask to the next bit
            currentMask >>= 1;
            if (node == null)
                throw new IOException("Invalid node when walking the tree, possibly corrupted data.");
        }
        if (node.getSymbol() == null)
            return -1;
        // XXX the following is a hack to prevent the integer value of return
        // from being -1, since 0x100 is greater than any byte the sum as an
        // integer will always be positive, the error that happened before
        // was when a byte 0xff was read from the symbol the return value was -1
        // meaning the end of input even though it wasn't
        return 0x100 + node.getSymbol();
    }
}
