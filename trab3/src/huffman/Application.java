package huffman;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Application {
    static BufferedReader reader;
    static Scanner scanner;

    static String input() throws IOException {
        //return reader.readLine();
        return scanner.next();
    }

    static String pathToString(Vector<Boolean> path) {
        String out = "";
        if (path == null) {
            System.err.println("null path found");
            return "";
        }
        for (Boolean b: path)
            out += b ? "1" : "0";
        return out;
    }

    static String byteToString(byte b) {
        String out = "";
        for (int i = 1 << 7; i != 0; i >>= 1)
            out += ((int)b & i) == i? "1" : "0";
        return out;
    }

    static String dummyCompress(String input) throws IOException {
        // analyze the byte frequency to build the tree
        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        // feed bytes into the analyzer
        analyzer.feed(new ByteArrayInputStream(input.getBytes()));
        // build the tree
        HuffmanTree<Byte> tree = analyzer.toHuffmanTree();
        // start initially with a representation of the padding
        String out = byteToString((byte)tree.getPadding());
        // pad so we always have an integer number of bytes (no missing bits)
        // XXX remember to read the padding and ignore the padded bits
        for (int i = tree.getPadding(); i > 0; --i)
            out += "0";
        // start writing each bit from the path
        for (byte b: input.getBytes())
            out += pathToString(tree.pathFor(b));
        return out;
    }

    static String betterCompress(String input) throws IOException {
        // analyze the byte frequency to build the tree
        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        // feed bytes into the analyzer
        analyzer.feed(new ByteArrayInputStream(input.getBytes()));
        // build the tree
        HuffmanTree<Byte> tree = analyzer.toHuffmanTree();
        // make an stream for that tree and input and read it byte per byte
        InputStream istream = new ByteArrayInputStream(input.getBytes());
        // make an stream to output the compressed bytes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // make a compression stream to write on the stream above
        HuffmanByteOutputStream ostream = new HuffmanByteOutputStream(tree, output);
        // compress every byte on istream
        try {
            int c;
            while ((c = istream.read()) != -1)
                ostream.write(c);
        } finally {
            ostream.close();
            istream.close();
        }
        // make the output string
        String out = "";
        for (byte b: output.toByteArray())
            out += byteToString(b);
        return out;
    }

    static String compAndDecomp(String input) throws IOException {
        // make an stream to output the compressed bytes
        ByteArrayOutputStream ocompressed = new ByteArrayOutputStream();

        Compressor compressor = new Compressor(ocompressed);
        compressor.buildTree(new ByteArrayInputStream(input.getBytes()));

        // make an stream for that tree and input and read it byte per byte
        InputStream data = new ByteArrayInputStream(input.getBytes());

        // compress
        compressor.compress(new ByteArrayInputStream(input.getBytes()));

        // create an input stream from the compressed data
        InputStream compressedStream = new ByteArrayInputStream(ocompressed.toByteArray());

        Decompressor decompressor = new Decompressor(compressedStream);
        decompressor.buildTree();

        // finally somewhere to write the fresh data
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // decompress
        decompressor.decompress(output);

        return output.toString();
    }

    static String bitString(String input) {
        return bitString(input.getBytes());
    }

    static String bitString(byte[] bytes) {
        String out = "";
        for (byte b: bytes)
            out += byteToString(b);
        return out;
    }

    static String colonify(String input) {
        // the following is merely to place ';' every 8 bits so we can better distinguish bytes
        // we could return out if we want the plain string of bits
        String out = "";
        int i = 1;
        for (char c: input.toCharArray()) {
            out += c;
            if (i == 8) {
                out += ";";
                i = 0;
            }
            i++;
        }
        return out;
    }

    // IOException may be thrown by BufferedReader and is left untreated
    // as such it has to be explicit after main signature
    public static void main(String[] args) throws IOException {
        // as well as a single BufferedReader to read lines from stdin
        reader = new BufferedReader(new InputStreamReader(System.in));
        scanner = new Scanner(reader);
        // since a break inside the switch will not break the while loop
        // we use a boolean flag to indicate whether we should terminate de main loop or not

        //String test = "j'aime aller sur le bord de l'eau les jeudis ou les jours impairs";
        String test = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Integer nunc lectus, tincidunt vitae porttitor at, fermentum id erat.";

        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        analyzer.feed(new ByteArrayInputStream(test.getBytes()));

        //HuffmanTree<Byte> tree = new HuffmanTree<>(stack);
        HuffmanTree<Byte> tree = new HuffmanTree.ByteHuffmanTree(analyzer.nodeCollection());

        boolean quit = false;
        while(!quit) {
            // the main loop consists basically of this steps:
            // read input from the user (read with a nice > prompt)
            System.out.print("> ");
            switch(input()) {
                case "c":
                case "comp":
                case "compress": {
                    String inputFilename = input();
                    String outputFilename = input();
                    FileInputStream inputStream = new FileInputStream(inputFilename);
                    OutputStream outputStream = new FileOutputStream(outputFilename);
                    Compressor compressor = new Compressor(outputStream);
                    compressor.buildTree(inputStream);
                    inputStream.close();
                    inputStream = new FileInputStream(inputFilename);
                    compressor.compress(inputStream);
                    outputStream.close();
                    break;
                }
                case "d":
                case "decomp":
                case "decompress": {
                    String inputFilename = input();
                    String outputFilename = input();
                    FileInputStream inputStream = new FileInputStream(inputFilename);
                    OutputStream outputStream = new FileOutputStream(outputFilename);
                    Decompressor decompressor = new Decompressor(inputStream);
                    decompressor.buildTree();
                    decompressor.decompress(outputStream);
                    inputStream.close();
                    outputStream.close();
                    break;
                }
                case "b":
                case "bits":
                    System.out.println(colonify(bitString(test)));
                    break;
                case "s":
                case "symbols":
                    for (byte s: tree.getSymbols())
                        System.out.println((char)s + ": " + pathToString(tree.pathFor(s)));
                    break;
                case "t":
                case "test":
                    System.out.println(colonify(dummyCompress(test)));
                    System.out.println(colonify(betterCompress(test)));
                    System.out.println(test);
                    System.out.println(compAndDecomp(test));
                    break;
                case "p":
                case "print":
                    System.out.println(colonify(bitString(tree.toByteArray())));
                    System.out.println(tree);
                    System.out.println(new HuffmanTree.ByteHuffmanTree(tree.toByteArray()));
                    break;
                case "i":
                case "iter":
                    for (HuffmanNode node: tree) {
                        String symbol = node.getSymbol() == null? "" : ("[" + node.getSymbol() + "]");
                        System.out.println(node.getFrequency() + " " + symbol);
                    }
                    break;
                case "q":
                case "quit":
                    quit = true;
                    System.out.println("quitting... bye!");
                    break;
                default:
                    System.out.println("unknown command");
                    break;
            }
            //System.out.println("---");
        }
    }
}
