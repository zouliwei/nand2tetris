/** Translates Hack assembly language mnemonics into binary codes. */
class Coder {

    /** @return the 3-bits binary code of the dest mnemonic. */
    static String dest(String str) {
        String d1 = "0";
        String d2 = "0";
        String d3 = "0";

        if (str.contains("A")) {
            d1 = "1";
        }
        if (str.contains("D")) {
            d2 = "1";
        }
        if (str.contains("M")) {
            d3 = "1";
        }

        for (char c : str.toCharArray()) {
            if (c != 'A' && c != 'D' && c != 'M') {
                throw new IllegalArgumentException();
            }
        }

        return d1 + d2 + d3;
    }

    /** @return the 7-bits binary code of the comp mnemonic. */
    static String comp(String str) {

        String a = "0";
        if (str.contains("M")) {
            a = "1";
            str = str.replace("M", "A");
        }

        String c;
        switch (str) {
            case "0":   c = "101010"; break;
            case "1":   c = "111111"; break;
            case "-1":  c = "111010"; break;
            case "D":   c = "001100"; break;
            case "A":   c = "110000"; break;
            case "!D":  c = "001101"; break;
            case "!A":  c = "110001"; break;
            case "-D":  c = "001111"; break;
            case "-A":  c = "110011"; break;
            case "D+1":
            case "1+D": c = "011111"; break;
            case "A+1":
            case "1+A": c = "110111"; break;
            case "D-1": c = "001110"; break;
            case "A-1": c = "110010"; break;
            case "D+A":
            case "A+D": c = "000010"; break;
            case "D-A": c = "010011"; break;
            case "A-D": c = "000111"; break;
            case "D&A":
            case "A&D": c = "000000"; break;
            case "D|A":
            case "A|D": c = "010101"; break;
            default: throw new IllegalArgumentException();
        }

        return a + c;
    }

    /** @return the 3-bits binary code of the jump mnemonic. */
    static String jump(String str) {
        switch (str) {
            case "":    return "000";
            case "JGT": return "001";
            case "JEQ": return "010";
            case "JGE": return "011";
            case "JLT": return "100";
            case "JNE": return "101";
            case "JLE": return "110";
            case "JMP": return "111";
            default: throw new IllegalArgumentException();
        }
    }

    /** decimal -> 15-bit binary */
    static String toBinary(int decimal) {
        String binary = Integer.toBinaryString(decimal);
        return String.format("%1$15s", binary).replace(" ", "0");
    }
}
