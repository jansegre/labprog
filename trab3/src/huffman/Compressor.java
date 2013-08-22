package huffman;

import java.io.*;

public class Compressor {
    protected HuffmanTree<Byte> tree;
    protected OutputStream outputStream;

    public Compressor(OutputStream output) {
        outputStream = output;
    }

    public HuffmanTree<Byte> getTree() {
        return tree;
    }

    public void buildTree(InputStream inputStream) throws IOException {
        // analyze the byte frequency to build the tree
        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        // feed bytes into the analyzer
        analyzer.feed(inputStream);
        // build the tree
        tree = new HuffmanTree.ByteHuffmanTree(analyzer.nodeCollection());
        // write the tree
        outputStream.write(tree.toByteArray());
    }

    public void compress(InputStream inputStream) throws IOException {
        // make a compression stream to write on the stream above
        // compress every byte on inputStream
        try (HuffmanByteOutputStream compressStream = new HuffmanByteOutputStream(tree, outputStream))
        {
            int c;
            while ((c = inputStream.read()) != -1) {
                compressStream.write(c);
            }
        }
    }
}
