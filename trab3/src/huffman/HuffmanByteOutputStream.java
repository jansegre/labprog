package huffman;

import java.io.IOException;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class HuffmanByteOutputStream extends FilterOutputStream {
    HuffmanTree<Byte> tree;
    int currentBit;
    int currentByte;
    //int counter;

    public HuffmanByteOutputStream(HuffmanTree<Byte> huffmanTree, OutputStream outputStream) throws IOException {
        super(outputStream);
        tree = huffmanTree;
        out.write(tree.getPadding());
        currentByte = 0;
        currentBit = 1 << (7 - tree.getPadding());
        //counter = tree.getPadding();
    }

    @Override
    public void write(int b) throws IOException {
        for (boolean bit: tree.pathFor((byte)b)) {
            if (bit) currentByte |= currentBit;
            currentBit >>= 1;
            if (currentBit == 0) {
                out.write(currentByte);
                currentByte = 0;
                currentBit = 1 << 7;
            }
            /*currentByte <<= 1;
            currentByte = bit? currentByte + 1 : currentByte;
            counter++;
            if (counter == 8) {
                out.write(currentByte);
                currentByte = 0;
                counter = 0;
            }*/
        }
    }
}
