import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Assembler {
    public static void assemble(String inputFile, String outputFile) throws IOException {
        // First pass
        Parser parser = new Parser(inputFile);
        SymbolTable symbolTable = new SymbolTable();
        int instructionCounter = 0;

        while (parser.hasMoreCommands()) {
            parser.advance();
            String commandType = parser.commandType();

            if (commandType.equals("L_COMMAND")) {
                String symbol = parser.symbol();
                symbolTable.addEntry(symbol, instructionCounter);
            } else {
                instructionCounter++;
            }
        }

        // Second pass
        parser = new Parser(inputFile);
        BinaryCode code = new BinaryCode();
        List<String> binaryInstructions = new ArrayList<>();

        while (parser.hasMoreCommands()) {
            parser.advance();

            if (parser.commandType().equals("A_COMMAND")) {
                String symbol = parser.symbol();
                int value;
                if (symbol.matches("\\d+")) {
                    value = Integer.parseInt(symbol);
                } else {
                    value = symbolTable.addVariable(symbol);
                }
                String binary = String.format("%16s", Integer.toBinaryString(value))
                                    .replace(' ', '0');
                binaryInstructions.add(binary);

            } else if (parser.commandType().equals("C_COMMAND")) {
                String comp = code.comp(parser.comp());
                String dest = code.dest(parser.dest());
                String jump = code.jump(parser.jump());
                String binary = "111" + comp + dest + jump;
                binaryInstructions.add(binary);
            }
        }

        // Write output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < binaryInstructions.size(); i++) {
                writer.write(binaryInstructions.get(i));
                if (i < binaryInstructions.size() - 1) {
                    writer.write("\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            String inputFile = "pong/Pong.asm";
            String outputFile = "Pong2.hack";
            assemble(inputFile, outputFile);
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            System.exit(1);
        }
    }
}
