package huffman;

import java.io.IOException;
import java.io.OutputStream;

public class HuffmanByteOutputStream extends OutputStream {
    HuffmanTree<Byte> tree;
    OutputStream output;
    int currentBit;
    int currentByte;

    public HuffmanByteOutputStream(HuffmanTree<Byte> huffmanTree, OutputStream outputStream) throws IOException {
        tree = huffmanTree;
        output = outputStream;
        output.write(tree.getPadding());
        currentBit = 1 << (7 - tree.getPadding());
        currentByte = 0;
    }

    @Override
    public void write(int b) throws IOException {
        for (boolean bit: tree.pathFor((byte)b)) {
            if (bit) currentByte |= currentBit;
            currentBit >>= 1;
            if (currentBit == 0) {
                output.write(currentByte);
                currentByte = 0;
                currentBit = 1 << 7;
            }
        }
    }
}
