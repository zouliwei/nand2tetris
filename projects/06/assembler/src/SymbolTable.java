import java.util.HashMap;
import java.util.Map;

/** Keeps a correspondence between symbolic labels and numeric addresses. */
class SymbolTable {

    private Map<String, Integer> map;

    /** Creates a new empty symbol table. */
    SymbolTable() {
        map = new HashMap<>();
        map.put("SP",   0);
        map.put("LCL",  1);
        map.put("ARG",  2);
        map.put("THIS", 3);
        map.put("THAT", 4);
        map.put("SCREEN", 16384);
        map.put("KBD",    24576);
        for (int i = 0; i < 16; i++) {
            map.put("R" + i, i);
        }
    }

    /** Adds the pair (symbol, address) to the table. */
    void put(String symbol, int address) {
        map.put(symbol, address);
    }

    /** Does the symbol table contain the given symbol? */
    boolean contains(String symbol) {
        return map.containsKey(symbol);
    }

    /** Returns the address associated with the symbol. */
    int get(String symbol) {
        return map.get(symbol);
    }
}
