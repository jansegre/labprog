package huffman;

import java.io.IOException;
import java.io.InputStream;

public class HuffmanByteInputStream extends InputStream {
    HuffmanTree<Byte> tree;
    InputStream data;
    int currentBit;
    int currentByte;

    public HuffmanByteInputStream(HuffmanTree<Byte> huffmanTree, InputStream inputData) throws IOException{
        tree = huffmanTree;
        data = inputData;
        int padding = data.read();
        currentBit = 1 << (7 - padding);
        currentByte = data.read();
    }

    @Override
    public int read() throws IOException {
        HuffmanNode<Byte> node = tree.getRoot();
        while (!node.isLeaf()) {
            if (currentBit == 0) {
                if ((currentByte = data.read()) == -1)
                    return data.read();
                currentBit = 1 << 7;
            }
            boolean bit = (currentByte & currentBit) == currentBit;
            node = bit? node.right_child : node.left_child;
            currentBit >>= 1;
        }
        return node.symbol;
    }
}
