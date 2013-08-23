package huffman;

import java.io.IOException;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class HuffmanByteOutputStream extends FilterOutputStream {
    HuffmanTree<Byte> tree;
    int currentMask;
    int currentByte;
    //int counter;

    public HuffmanByteOutputStream(HuffmanTree<Byte> huffmanTree, OutputStream outputStream) throws IOException {
        super(outputStream);
        tree = huffmanTree;
        out.write(tree.getPadding());
        // we start with an all zero byte
        currentByte = 0;
        // and a shifted mask/counter
        currentMask = 1 << (7 - tree.getPadding());
        //counter = tree.getPadding();
    }

    @Override
    public void write(int b) throws IOException {
        for (boolean bit: tree.pathFor((byte)b)) {
            // strategy to write bit by bit
            // add the current mask if the bit is true
            if (bit) currentByte |= currentMask;
            // shift the mask to the right
            currentMask >>= 1;
            // when it's all zeros, the current byte is done
            if (currentMask == 0) {
                // write it
                out.write(currentByte);
                // and reset the mask and byte
                currentByte = 0;
                currentMask = 1 << 7;
            }
            /*
            // this is another possible strategy
            // some may find it more intuitive
            // instead of shifting the mask, we shift the byte
            currentByte <<= 1;
            // and write 1 or 0 (no write) to the last bit
            currentByte = bit? currentByte + 1 : currentByte;
            // keeping a count of how many bits we've read
            counter++;
            // when the counter hits 8, a byte has been made
            if (counter == 8) {
                // write it
                out.write(currentByte);
                // and reset the stuff
                currentByte = 0;
                counter = 0;
            }
            */
        }
    }
}
