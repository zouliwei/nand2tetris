import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Assembler {

    private static final Charset ENCODING = StandardCharsets.US_ASCII;

    private SymbolTable symbolTable = new SymbolTable();
    private int RAM_Address;
//  private int ROM_Address;

    private Parser parser;
    private Writer writer;
//  private Coder coder;

    public void assemble(String asmFile) throws IOException {
        firstPass(asmFile);
        secondPass(asmFile);
    }

    /** First pass: handle L-command (pseudo). */
    private void firstPass(String asmFile) throws IOException {
        parser = new Parser(asmFile, ENCODING);

        int ROM_Address = 0;
        while (parser.hasMoreCommands()) {
            parser.advance();
            if (parser.commandType() == Parser.CommandType.L_COMMAND) {
                symbolTable.put(parser.symbol(), ROM_Address);
            } else {
                ROM_Address++;
            }
        }

        parser.close();
    }

    /** Second pass: handle A-command and C-command. */
    private void secondPass(String asmFile) throws IOException {
        String hackFile = toHackFile(asmFile);
        parser = new Parser(asmFile, ENCODING);
        writer = new Writer(hackFile, ENCODING);

        RAM_Address = 16;
        while (parser.hasMoreCommands()) {
            parser.advance();
            if (parser.commandType() == Parser.CommandType.A_COMMAND) {
                handle_A_COMMAND();
            }
            if (parser.commandType() == Parser.CommandType.C_COMMAND) {
                handle_C_COMMAND();
            }
        }

        parser.close();
        writer.close();
        System.out.println(asmFile + " -> " + hackFile);
    }

    /** Subroutine of second pass: handle A-command. */
    private void handle_A_COMMAND() throws IOException {
        String symbol = parser.symbol();

        int address;
        if (Character.isDigit(symbol.charAt(0))) {
            address = Integer.parseInt(symbol);
        } else {
            if (!symbolTable.contains(symbol)) {
                symbolTable.put(symbol, RAM_Address++);
            }
            address = symbolTable.get(symbol);
        }

        writer.write("0");
        writer.write(Coder.toBinary(address));
        writer.newLine();
    }

    /** Subroutine of second pass: handle C-command. */
    private void handle_C_COMMAND() throws IOException {
        writer.write("111");
        writer.write(Coder.comp(parser.comp()));
        writer.write(Coder.dest(parser.dest()));
        writer.write(Coder.jump(parser.jump()));
        writer.newLine();
    }

    /** filename.asm -> filename.hack */
    private String toHackFile(String asmFile) {
        if (!asmFile.endsWith(".asm")) {
            throw new IllegalArgumentException();
        }
        return asmFile.substring(0, asmFile.length() - 3) + "hack";
    }

    public static void main(String[] args) throws IOException {
        Assembler assembler = new Assembler();
        for (String asmFile : args) {
            assembler.assemble(asmFile);
        }
    }
}
