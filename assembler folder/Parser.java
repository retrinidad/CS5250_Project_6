import java.io.*;
import java.util.*;

public class Parser {
    private List<String> commands;
    private String currentCommand;
    private int currentLine;

    public Parser(String filePath) throws IOException {
        this.commands = new ArrayList<>();
        this.currentCommand = null;
        this.currentLine = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    // Remove comments
                    String[] parts = line.split("//");
                    line = parts[0].trim();
                    if (!line.isEmpty()) {
                        commands.add(line);
                    }
                }
            }
        }
    }

    public boolean hasMoreCommands() {
        return currentLine < commands.size();
    }

    public void advance() {
        if (hasMoreCommands()) {
            currentCommand = commands.get(currentLine);
            currentLine++;
        }
    }

    public String commandType() {
        String command = currentCommand.trim();
        if (command.startsWith("(") && command.endsWith(")")) {
            return "L_COMMAND";
        } else if (command.startsWith("@")) {
            return "A_COMMAND";
        } else {
            return "C_COMMAND";
        }
    }

    public String symbol() {
        if (commandType().equals("A_COMMAND")) {
            return currentCommand.substring(1);
        } else if (commandType().equals("L_COMMAND")) {
            return currentCommand.substring(1, currentCommand.length() - 1);
        }
        return null;
    }

    public String dest() {
        if (currentCommand.contains("=")) {
            return currentCommand.split("=")[0];
        }
        return "null";
    }

    public String comp() {
        String command;
        if (currentCommand.contains("=")) {
            command = currentCommand.split("=")[1];
        } else {
            command = currentCommand;
        }
        if (command.contains(";")) {
            return command.split(";")[0];
        }
        return command;
    }

    public String jump() {
        if (currentCommand.contains(";")) {
            return currentCommand.split(";")[1];
        }
        return "null";
    }
}