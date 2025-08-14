package src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        if (args.length < 1) {
            throw new Error("too few arguments");
        } else if (args.length > 1) {
            throw new Error("too many arguments");
        } else { // args.length == 1

            // Reference for reading in a file byte-by-byte:
            // https://www.codejava.net/java-se/file-io/how-to-read-and-write-binary-files-in-java

            String fileName = args[0];

            try {

                BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(Path.of(fileName)));
                BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Path.of(fileName + ".mcmp")));
                FileWriter logWriter = new FileWriter(new File(fileName + ".log"));

                HuffmanNode huffmanTree = generateHuffmanTree(inputStream);
                generateAndWriteHeader(outputStream, logWriter, huffmanTree);

                // Reset input stream to start from beginning again
                inputStream.close();
                inputStream = new BufferedInputStream(Files.newInputStream(Path.of(fileName)));

                encodeFile(inputStream, outputStream, logWriter, huffmanTree);

                inputStream.close();
                outputStream.close();
                logWriter.close();
            
            } catch (FileNotFoundException ex) {
                throw new Error("no such file: " + fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    
    public static String byteToBinaryString(int b) {
        return(byteToBinaryString(b, true));
    }
    public static String byteToBinaryString(int b, boolean prefix) {
        return(
            (prefix ? "0b" : "") +
            String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')
        );
    }


    private static HuffmanNode generateHuffmanTree(
        BufferedInputStream inputStream
    ) throws IOException {
        SymbolTable symbolTable = generateSymbolTable(inputStream);
        HuffmanNode huffmanTree = HuffmanTreeBuilder.symbolTableToTree(symbolTable);
        return(huffmanTree);
    }


    private static SymbolTable generateSymbolTable(
        BufferedInputStream inputStream
    ) throws IOException {
        SymbolTable symbolTable = new SymbolTable();
        int byteRead = -1;
        while ((byteRead = inputStream.read()) != -1) {
            symbolTable.countSymbol(byteRead);
        }
        return(symbolTable);
    }


    private static void generateAndWriteHeader(
        BufferedOutputStream outputStream,
        FileWriter logWriter,
        HuffmanNode huffmanTree
    ) throws IOException {
        ArrayList<Integer> headerLength = new ArrayList<Integer>();
        ArrayList<Integer> topographyLength = new ArrayList<Integer>();
        ArrayList<Integer> topography = new ArrayList<Integer>();
        ArrayList<Integer> leaves = new ArrayList<Integer>();

        populateTopographyAndLeaves(topography, leaves, huffmanTree);
        populateTopographyLength(topographyLength, topography);
        populateHeaderLength(topographyLength, topography, leaves, headerLength);
        writeHeader(logWriter, headerLength, topographyLength, topography, leaves, outputStream);
    }


    private static void populateTopographyAndLeaves(
        ArrayList<Integer> topography,
        ArrayList<Integer> leaves,
        HuffmanNode huffmanTree
    ) {

        String topographyBitBuffer = "";

        // Depth-first search to construct topography and list leaves

        ArrayList<HuffmanNode> nodeStack = new ArrayList<HuffmanNode>();
        nodeStack.add(huffmanTree);

        while(nodeStack.size() > 0) {
            HuffmanNode node = nodeStack.removeLast();
            if (node instanceof HuffmanMiddleNode) {
                HuffmanMiddleNode midNode = (HuffmanMiddleNode) node; 
                nodeStack.add(midNode.getRight());
                nodeStack.add(midNode.getLeft());
                topographyBitBuffer += "0";
            } else { // node instanceof Symbol
                Symbol symNode = (Symbol) node;
                leaves.add(symNode.getValue());
                topographyBitBuffer += "1";
            }
            if (topographyBitBuffer.length() == 8) {
                int topographyBit = Integer.parseUnsignedInt(topographyBitBuffer, 2);
                topographyBitBuffer = "";
                topography.add(topographyBit);
            }
        }

        // Write out a possible incomplete byte by padding the end/right with zeros

        if (topographyBitBuffer.length() > 0) {
            while (topographyBitBuffer.length() < 8) {
                topographyBitBuffer += "0";
            }
            int topographyBit = Integer.parseUnsignedInt(topographyBitBuffer, 2);
            topographyBitBuffer = "";
            topography.add(topographyBit);
        }

    }

    
    private static void populateTopographyLength(
        ArrayList<Integer> topographyLength,
        ArrayList<Integer> topography
    ) {
        int topLengthInt = topography.size();
        int lastSevenBits = topLengthInt % (0b1 << 7);
        topLengthInt >>>= 7;
        topographyLength.add(0, lastSevenBits);
        while (topLengthInt > 0) {
            int lastByte = topLengthInt % (0b1 << 7) + (0b1 << 7);
            topLengthInt >>>= 7;
            topographyLength.add(0, lastByte);
        }
    }


    private static void populateHeaderLength(
        ArrayList<Integer> topographyLength,
        ArrayList<Integer> topography,
        ArrayList<Integer> leaves,
        ArrayList<Integer> headerLength
    ) {
        int headerLengthInt = topographyLength.size() + topography.size() + leaves.size();
        int lastSevenBits = headerLengthInt % (0b1 << 7);
        headerLengthInt >>>= 7;
        headerLength.add(0, lastSevenBits);
        while (headerLengthInt > 0) {
            int lastByte = headerLengthInt % (0b1 << 7) + (0b1 << 7);
            headerLengthInt >>>= 7;
            headerLength.add(0, lastByte);
        }
    }


    private static void writeHeader(
        FileWriter logWriter,
        ArrayList<Integer> headerLength,
        ArrayList<Integer> topographyLength,
        ArrayList<Integer> topography,
        ArrayList<Integer> leaves,
        BufferedOutputStream outputStream
    ) throws IOException {

        logWriter.write("WRITING HEADER\n");

        logWriter.write("\nWriting Header Length\n");
        logWriter.write("Total Bytes: " + headerLength.size() + "\n\n");
        for (int outByte: headerLength) {
            logWriter.write("  Writing byte " + Main.byteToBinaryString(outByte) + "\n");
            outputStream.write(outByte);
        }

        logWriter.write("\nWriting Topography Length\n");
        logWriter.write("Total Bytes: " + topographyLength.size() + "\n\n");
        for (int outByte: topographyLength) {
            logWriter.write("  Writing byte " + Main.byteToBinaryString(outByte) + "\n");
            outputStream.write(outByte);
        }

        logWriter.write("\nWriting Topography\n");
        logWriter.write("Total Bytes: " + topography.size() + "\n\n");
        for (int outByte: topography) {
            logWriter.write("  Writing byte " + Main.byteToBinaryString(outByte) + "\n");
            outputStream.write(outByte);
        }

        logWriter.write("\nWriting Leaves\n");
        logWriter.write("Total Bytes: " + leaves.size() + "\n\n");
        for (int outByte: leaves) {
            logWriter.write("  Writing byte " + Main.byteToBinaryString(outByte) + " (" + (char) outByte + ")\n");
            outputStream.write(outByte);
        }

    }


    private static void encodeFile(
        BufferedInputStream inputStream,
        BufferedOutputStream outputStream,
        FileWriter logWriter,
        HuffmanNode huffmanTree
    ) throws IOException {

        // Swap each byte from the input stream with its huffman code; output one byte at a time

        String bitBuffer = "";
        int byteRead = -1;
        logWriter.write("\nENCODING FILE\n");
        while ((byteRead = inputStream.read()) != -1) {
            String huffmanCode = huffmanTree.getHuffmanCode(byteRead);
            logWriter.write("\nConverted character '" + (char)byteRead + "' to Huffman Code " + huffmanCode + ".\n");
            bitBuffer += huffmanCode;
            logWriter.write("Added Huffman Code to bit buffer; bit buffer is now '" + bitBuffer + "'.\n\n");
            while (bitBuffer.length() >= 8) {
                String outByteString = bitBuffer.substring(0, 8);
                logWriter.write("  Extracted byte '" + outByteString + "' from byte buffer.\n");
                int outByte = Integer.parseUnsignedInt(outByteString, 2);
                logWriter.write("  Converted byte string to byte of value " + outByte + ".\n");
                outputStream.write(outByte);
                logWriter.write("  Output byte of value " + (outByte) + " to output stream.\n");
                bitBuffer = bitBuffer.substring(8);
                logWriter.write("  Updated bit buffer to remove previous byte.\n");
            }
        }

        // Write out possible incomplete byte, and trash bit counter

        int trashBitCount = 0;
        if (bitBuffer.length() > 0) {
            while (bitBuffer.length() < 8) {
                bitBuffer += "0";
                ++trashBitCount;
            }
            int outByte = Integer.parseUnsignedInt(bitBuffer, 2);
            outputStream.write(outByte);
            bitBuffer = "";
        }
        outputStream.write(trashBitCount);

    }

}
