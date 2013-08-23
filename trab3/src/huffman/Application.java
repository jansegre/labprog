package huffman;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Application {
    protected Scanner scanner;
    protected ByteArrayOutputStream buffer;
    protected Decompressor decompressor;
    protected Compressor compressor;

    String input() {
        return scanner.next();
    }

    FileInputStream openInputFile(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("file: '" + fileName + "' was not found.");
            System.out.print("please try another filename: ");
            return openInputFile(input());
        }
    }

    FileOutputStream openOutputFile(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("file: '" + fileName + "' was not found.");
            System.out.print("please try another filename: ");
            return openOutputFile(input());
        }
    }

    String pathToString(boolean[] path) {
        String out = "";
        if (path == null) {
            System.err.println("null path found");
            return "";
        }
        for (Boolean b: path)
            out += b ? "1" : "0";
        return out;
    }

    String codeBook(HuffmanTree<Byte> tree) {
        String out = "";
        for (byte s: tree.getSymbols())
            out += byteToString(s) + ": " + pathToString(tree.pathFor(s)) + '\n';
        return out;
    }

    String byteToString(byte b) {
        String out = "";
        for (int i = 1 << 7; i != 0; i >>= 1)
            out += ((int)b & i) == i? "1" : "0";
        return out;
    }

    String bitString(String input) {
        return bitString(input.getBytes());
    }

    String bitString(byte[] bytes) {
        String out = "";
        for (byte b: bytes)
            out += byteToString(b);
        return out;
    }

    String colonify(String input) {
        return colonify(input, 16);
    }

    String colonify(String input, int lineBreak) {
        // the following is merely to place ';' every 8 bits so we can better distinguish bytes
        // we could return out if we want the plain string of bits
        String out = "";
        int i = 1;
        int b = 1;
        for (char c: input.toCharArray()) {
            out += c;
            if (i == 8) {
                out += ';';
                i = 0;
                if (b == lineBreak) {
                    out += '\n';
                    b = 0;
                }
                b++;
            }
            i++;
        }
        return out;
    }

    String dummyCompress(String input) throws IOException {
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

    String betterCompress(String input) throws IOException {
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

    void interactiveConsole() throws IOException {
        String test = "j'aime aller sur le bord de l'eau les jeudis ou les jours impairs";
        //String test = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
        //        "Integer nunc lectus, tincidunt vitae porttitor at, fermentum id erat.";

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
                    FileInputStream inputStream = openInputFile(inputFilename);
                    OutputStream outputStream = openOutputFile(outputFilename);
                    compressor = new Compressor(outputStream);
                    compressor.buildTree(inputStream);
                    inputStream.close();
                    inputStream = new FileInputStream(inputFilename);
                    compressor.compress(inputStream);
                    outputStream.close();
                    inputStream.close();
                    System.out.println("compressed to file.");
                    break;
                }
                case "d":
                case "dec":
                case "decomp":
                case "decompress": {
                    String inputFilename = input();
                    String outputFilename = input();
                    FileInputStream inputStream = openInputFile(inputFilename);
                    OutputStream outputStream = openOutputFile(outputFilename);
                    decompressor = new Decompressor(inputStream);
                    decompressor.buildTree();
                    decompressor.decompress(outputStream);
                    inputStream.close();
                    outputStream.close();
                    System.out.println("decompressed to file.");
                    break;
                }
                case "mc":
                case "memcomp":
                case "memorycompress": {
                    String inputFilename = input();
                    FileInputStream inputStream = openInputFile(inputFilename);
                    compressor = new Compressor(buffer);
                    compressor.buildTree(inputStream);
                    inputStream.close();
                    inputStream = new FileInputStream(inputFilename);
                    compressor.compress(inputStream);
                    inputStream.close();
                    System.out.println("compressed to memory.");
                    break;
                }
                case "md":
                case "memdec":
                case "memdecomp":
                case "memorydecompress": {
                    String inputFilename = input();
                    FileInputStream inputStream = openInputFile(inputFilename);
                    decompressor = new Decompressor(inputStream);
                    decompressor.buildTree();
                    decompressor.decompress(buffer);
                    inputStream.close();
                    System.out.println("decompressed to memory.");
                    break;
                }
                case "p":
                case "print":
                    System.out.write(buffer.toByteArray());
                    break;
                case "-":
                case "clear":
                    buffer.close();
                    buffer = new ByteArrayOutputStream();
                    break;
                case "b":
                case "bits": {
                    System.out.println(colonify(bitString(buffer.toByteArray())));
                    break;
                }
                case "t":
                case "test":
                    System.out.println(colonify(dummyCompress(test)));
                    System.out.println("---");
                    System.out.println(colonify(betterCompress(test)));
                    break;
                case "ct":
                case "ctree":
                case "comptree":
                    if (compressor != null) {
                        System.out.println(compressor.getTree());
                        System.out.println(codeBook(compressor.getTree()));
                    } else {
                        System.out.println("no tree yet.");
                    }
                    break;
                case "dt":
                case "dtree":
                case "domptree":
                    if (decompressor != null) {
                        System.out.println(decompressor.getTree());
                        System.out.println(codeBook(decompressor.getTree()));
                    } else {
                        System.out.println("no tree yet.");
                    }
                    break;
                case "?":
                case "h":
                case "help":
                    showUsage();
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
        }
    }

    void showUsage() {
        System.out.println(
                "usage:\n" +
                "interactive: java -jar dist/huffman.jar\n" +
                "compress:    java -jar dist/huffman.jar -c file.txt file.txt.hz\n" +
                "decompress:  java -jar dist/huffman.jar -d file.txt.hz file2.txt");
    }

    void handleArguments(String[] args) {
        if (args.length != 3) {
            showUsage();
        } else {
            String operation = args[0];
            String inputFilename = args[1];
            String outputFilename = args[2];
            FileInputStream inputStream;
            OutputStream outputStream;
            try {
                inputStream = new FileInputStream(inputFilename);
                outputStream = new FileOutputStream(outputFilename);
                switch (operation) {
                    case "-c":
                        System.out.print("compressing '" + inputFilename + "' to '" + outputFilename + "'... ");
                        compressor = new Compressor(outputStream);
                        compressor.buildTree(inputStream);
                        inputStream.close();
                        inputStream = new FileInputStream(inputFilename);
                        compressor.compress(inputStream);
                        outputStream.close();
                        System.out.println("ok");
                        break;
                    case "-d":
                        System.out.print("decompressing '" + inputFilename + "' to '" + outputFilename + "'... ");
                        decompressor = new Decompressor(inputStream);
                        decompressor.buildTree();
                        decompressor.decompress(outputStream);
                        outputStream.close();
                        System.out.println("ok");
                        break;
                    default:
                        System.err.println("unrecognized operation '" + operation + "'.");
                        System.exit(1);
                        break;
                }
            } catch (FileNotFoundException e) {
                System.err.println("file not found: '" + e.getMessage() + "'.");
                System.exit(1);
            } catch(IOException e) {
                System.err.println("an I/O error occurred: '" + e.getMessage() + "'");
                System.exit(1);
            }
        }
    }

    public Application() {
        // as well as a single BufferedReader to read lines from stdin
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        scanner = new Scanner(reader);
        // since a break inside the switch will not break the while loop
        // we use a boolean flag to indicate whether we should terminate de main loop or not

        buffer = new ByteArrayOutputStream();

        //FrequencyAnalyzer<Byte> analyzer;// = new FrequencyAnalyzer.ByteFrequencyAnalyzer();
        //analyzer.feed(new ByteArrayInputStream(test.getBytes()));

        //HuffmanTree<Byte> tree = new HuffmanTree<>(stack);
        //HuffmanTree<Byte> tree;// = new HuffmanTree.ByteHuffmanTree(analyzer.nodeCollection());

        compressor = null;
        decompressor = null;
    }

    // IOException may be thrown by BufferedReader and is left untreated
    // as such it has to be explicit after main signature
    public static void main(String[] args) throws IOException {
        Application app = new Application();
        if (args.length > 0) {
            app.handleArguments(args);
        } else {
            app.interactiveConsole();
        }
    }
}
