package huffman;

import java.io.*;
import java.util.Stack;

public class Decompressor {
    protected HuffmanTree<Byte> tree;
    protected InputStream inputStream;
    protected int bufferSize;

    public Decompressor(InputStream input) {
        // default buffer size of 4MB = 2**22B = 1<<22B
        this(input, 1 << 22);
    }

    public Decompressor(InputStream input, int size) {
        bufferSize = size;
        inputStream = new BufferedInputStream(input, bufferSize);
    }

    public HuffmanTree<Byte> getTree() {
        return tree;
    }

    public void buildTree() throws IOException {
        tree = new HuffmanTree.ByteHuffmanTree(inputStream);
    }

    public void decompress(OutputStream outputStream) throws IOException {
        // make a compression stream to write on the stream above
        // compress every byte on inputStream
        try (
                HuffmanByteInputStream decompressStream = new HuffmanByteInputStream(tree, inputStream);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize)
        )
        {
            int c;
            while ((c = decompressStream.read()) != -1)
                bufferedOutputStream.write(c);
        }
    }
}
