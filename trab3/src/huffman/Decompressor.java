package huffman;

import java.io.*;
import java.util.Stack;

public class Decompressor {
    protected HuffmanTree<Byte> tree;
    protected InputStream inputStream;

    public Decompressor(InputStream input) {
        inputStream = input;
    }

    public HuffmanTree<Byte> getTree() {
        return tree;
    }

    public void buildTree() throws IOException {
        Stack<HuffmanNode<Byte>> nodes = new Stack<>();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        while(true) {
            int frequency = dataInputStream.readInt();
            if (frequency == 0) break;
            byte symbol = dataInputStream.readByte();
            nodes.push(new HuffmanNode<Byte>(frequency, symbol));
        }
        tree = new HuffmanTree.ByteHuffmanTree(nodes);
    }

    public void decompress(OutputStream outputStream) throws IOException {
        // make a compression stream to write on the stream above
        // compress every byte on inputStream
        try (HuffmanByteInputStream decompressStream = new HuffmanByteInputStream(tree, inputStream))
        {
            int c;
            while ((c = decompressStream.read()) != -1)
                outputStream.write(c);
        }
    }
}
