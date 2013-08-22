package huffman;

import java.io.*;

public class Compressor {
    protected HuffmanTree<Byte> tree;
    protected OutputStream outputStream;
    protected int bufferSize;

    public Compressor(OutputStream output) {
        // default buffer size of 4MB = 2**22B = 1<<22B
        this(output, 1 << 22);
    }

    public Compressor(OutputStream output, int size) {
        bufferSize = size;
        outputStream = new BufferedOutputStream(output, bufferSize);
    }

    public HuffmanTree<Byte> getTree() {
        return tree;
    }

    public void buildTree(InputStream inputStream) throws IOException {
        // analyze the byte frequency to build the tree
        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        // feed bytes into the analyzer
        analyzer.feed(new BufferedInputStream(inputStream, bufferSize));
        // build the tree
        tree = new HuffmanTree.ByteHuffmanTree(analyzer.nodeCollection());
        // write the tree
        outputStream.write(tree.toByteArray());
    }

    public void compress(InputStream inputStream) throws IOException {
        // make a compression stream to write on the stream above
        // compress every byte on inputStream
        try (
                HuffmanByteOutputStream compressStream = new HuffmanByteOutputStream(tree, outputStream);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, bufferSize)
        )
        {
            int c;
            while ((c = bufferedInputStream.read()) != -1)
                compressStream.write(c);
        }
    }
}
