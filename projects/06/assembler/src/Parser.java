import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Encapsulates access to the input code. Reads an assembly language command,
 * parses it, and provides convenient access to the command’s components
 * (fields and symbols). In addition, removes all white space and comments.
 */

class Parser {

    private BufferedReader reader;
    private String curtCommand, nextCommand;
    private String symbol, dest, comp, jump;

    /** Opens the input file/stream and gets ready to parse it. */
    Parser(String path, Charset encoding) throws IOException {
        reader = Files.newBufferedReader(Paths.get(path), encoding);
        readNextCommand();
    }

    /** Are there more commands in the input? */
    boolean hasMoreCommands() {
        return nextCommand != null;
    }

    /**
     * Reads the next command from the input and makes it the current command.
     * Should be called only if hasMoreCommands() is true.
     * Initially there is no current command.
     */
    void advance() throws IOException {
        if (!hasMoreCommands()) {
            return;
        }

        curtCommand = nextCommand;
        parseCurtCommand();
        readNextCommand();
    }

    private void parseCurtCommand() {
        if (commandType() == CommandType.C_COMMAND) {
            parse_C_COMMAND();
        } else {
            parse_AL_COMMAND();
        }
    }

    private void readNextCommand() throws IOException {
        while (isBlank(nextCommand = toCommand(reader.readLine())));
    }

    private void parse_C_COMMAND() {
        if (!curtCommand.contains("=")) {
            curtCommand = "=" + curtCommand;
        }
        if (!curtCommand.contains(";")) {
            curtCommand = curtCommand + ";";
        }
        String[] tokens = curtCommand.split("[ =;]+", 3);
        dest = tokens[0]; comp = tokens[1]; jump = tokens[2];
    }

    private void parse_AL_COMMAND() {
        symbol = curtCommand.replaceAll("[@()]", "");
    }

    private boolean isBlank(String str) {
        return str != null && str.isEmpty();
    }

    private String toCommand(String str) {
        return str != null ? rmSpace(rmComment(str)) : null;
    }

    private String rmComment(String str) {
        return str.contains("//") ? str.substring(0, str.indexOf("//")) : str;
    }

    private String rmSpace(String str) {
        return str.replaceAll("\\s", "");
    }

    /**
     * @return the type of the current command:
     * A_COMMAND for @Xxx where Xxx is either a symbol or a decimal number
     * C_COMMAND for dest=comp;jump
     * L_COMMAND (actually, pseudo-command) for (Xxx) where Xxx is a symbol.
     */
    CommandType commandType() {
        if (curtCommand.startsWith("@")) {
            return CommandType.A_COMMAND;
        }
        if (curtCommand.startsWith("(")) {
            return CommandType.L_COMMAND;
        }
        return CommandType.C_COMMAND;
    }

    enum CommandType {
        A_COMMAND, L_COMMAND, C_COMMAND
    }

    /**
     * @return the symbol or decimal Xxx of the current command @Xxx or (Xxx).
     * Should be called only when commandType() is A_COMMAND or L_COMMAND.
     */
    String symbol() {
        if (commandType() == CommandType.C_COMMAND) {
            throw new RuntimeException();
        }
        return symbol;
    }

    /**
     * @return the comp mnemonic in the current C-command (28 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    String comp() {
        if (commandType() != CommandType.C_COMMAND) {
            throw new RuntimeException();
        }
        return comp;
    }

    /**
     * @return the dest mnemonic in the current C-command (8 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    String dest() {
        if (commandType() != CommandType.C_COMMAND) {
            throw new RuntimeException();
        }
        return dest;
    }

    /**
     * @return the jump mnemonic in the current C-command (8 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    String jump() {
        if (commandType() != CommandType.C_COMMAND) {
            throw new RuntimeException();
        }
        return jump;
    }

    void close() throws IOException {
        reader.close();
    }
}
