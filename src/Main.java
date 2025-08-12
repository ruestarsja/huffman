package src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new Error("too few arguments");
        } else if (args.length == 1) {

            // https://www.codejava.net/java-se/file-io/how-to-read-and-write-binary-files-in-java

            String fileName = args[0];
            BufferedInputStream inputStream = null;
            BufferedOutputStream outputStream = null;
            FileWriter logWriter = null;

            try {

                SymbolTable symbolTable = new SymbolTable();
                inputStream = new BufferedInputStream(Files.newInputStream(Path.of(fileName)));
                outputStream = new BufferedOutputStream(Files.newOutputStream(Path.of(fileName + ".mcmp")));
                logWriter = new FileWriter(new File(fileName + ".log"));
                int byteRead = -1;
                while ((byteRead = inputStream.read()) != -1) {
                    symbolTable.countSymbol(byteRead);
                }
                inputStream.close();
                
                HuffmanNode huffmanTree = HuffmanTreeBuilder.symbolTableToTree(symbolTable);

                // TEMP BELOW!

                Symbol[] symbols = symbolTable.getSymbolsByFrequency();
                for (Symbol symbol: symbols) {
                    System.out.println(
                        huffmanTree.getHuffmanCode(symbol.getValue()) +
                        " : " +
                        ((char)symbol.getValue())
                    );
                }

                // TEMP ABOVE!

                // TODO: write size of huffman tree, followed by huffman tree itself, followed by trash bit count, to start of file

                String bitBuffer = "";
                inputStream = new BufferedInputStream(Files.newInputStream(Path.of(fileName)));
                while ((byteRead = inputStream.read()) != -1) {
                    String huffmanCode = huffmanTree.getHuffmanCode(byteRead);
                    logWriter.write("Converted character '" + (char)byteRead + "' to Huffman Code " + huffmanCode + ".\n");
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
                        logWriter.write("  Updated bit buffer to remove previous byte.\n\n");
                    }
                }

                inputStream.close();
                outputStream.close();
                logWriter.close();

            } catch (FileNotFoundException ex) {
                throw new Error("no such file: " + fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else { // args.length > 1
            throw new Error("too many arguments");
        }
    }

}
