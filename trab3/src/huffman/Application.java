package huffman;

import java.io.*;
import java.util.Vector;

public class Application {
    static BufferedReader reader;

    static String input() throws IOException {
        return reader.readLine();
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
        // analyze the byte frequency to build the tree
        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        // feed bytes into the analyzer
        analyzer.feed(new ByteArrayInputStream(input.getBytes()));
        // build the tree
        HuffmanTree<Byte> tree = analyzer.toHuffmanTree();
        // make an stream for that tree and input and read it byte per byte
        InputStream data = new ByteArrayInputStream(input.getBytes());
        // make an stream to output the compressed bytes
        ByteArrayOutputStream ocompressed = new ByteArrayOutputStream();
        // make a compression stream to write on the stream above
        HuffmanByteOutputStream ostream = new HuffmanByteOutputStream(tree, ocompressed);
        // compress every byte on istream
        try {
            int c;
            while ((c = data.read()) != -1)
                ostream.write(c);
        } finally {
            ostream.close();
            ocompressed.close();
        }
        // decompress every byte on output
        ByteArrayInputStream icompressed = new ByteArrayInputStream(ocompressed.toByteArray());
        HuffmanByteInputStream istream = new HuffmanByteInputStream(tree, icompressed);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            int c;
            while ((c = istream.read()) != -1)
                bout.write(c);
        } finally {
            istream.close();
            icompressed.close();
        }
        return bout.toString();
    }

    static String bitString(String input) {
        String out = "";
        for (byte b: input.getBytes())
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
        // since a break inside the switch will not break the while loop
        // we use a boolean flag to indicate whether we should terminate de main loop or not

        String test = "j'aime aller sur le bord de l'eau les jeudis ou les jours impairs";

        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        analyzer.feed(new ByteArrayInputStream(test.getBytes()));

        //HuffmanTree<Byte> tree = new HuffmanTree<>(stack);
        HuffmanTree<Byte> tree = new HuffmanTree<>(analyzer.nodeCollection());

        boolean quit = false;
        while(!quit) {
            // the main loop consists basically of this steps:
            // read input from the user (read with a nice > prompt)
            System.out.print("> ");
            switch(input()) {
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
                    System.out.println(tree);
                    break;
                case "i":
                case "iter":
                    for (HuffmanNode node: tree) {
                        String symbol = node.symbol == null? "" : ("[" + node.symbol + "]");
                        System.out.println(node.frequency + " " + symbol);
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
