package src;

import java.util.ArrayList;

public class SymbolTable {
    
    private ArrayList<Symbol> symbolList;

    // TEMP BELOW!

    // public static void main(String[] args) {
    //     SymbolTable st = new SymbolTable();
    //     for (int i = 0; i < 1_000_000; ++i) {
    //         st.countSymbol((byte)(256*Math.random()));
    //     }
    //     // st.printTable();
    //     Symbol[] symbolArray = st.getSymbolsByFrequency();
    //     for (Symbol symbol: symbolArray) {
    //         System.out.println(byteToBinaryString(symbol.getValue()) + " : " + symbol.getFrequency());
    //     }
    // }

    // TEMP ABOVE!

    public SymbolTable() {
        this.symbolList = new ArrayList<Symbol>();
    }

    public void countSymbol(byte value) {
        int index = this.findSymbolIndex(value);
        if (index == -1) {
            this.insertSymbol(value);
        } else {
            this.symbolList.get(index).count();
        }
    }

    private int findSymbolIndex(byte value) {
        // Annoying that I had to look up pseudocode for binary search, I should know this :(
        // https://pseudoeditor.com/guides/binary-search
        int start = 0;
        int end = this.symbolList.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            byte midSymbolValue = this.symbolList.get(mid).getValue();
            if (value == midSymbolValue) {
                return(mid);
            } else if (value < midSymbolValue) {
                end = mid - 1;
            } else { // value > midSymbolValue
                start = mid + 1;
            }
        }
        return(-1);
    }

    private int findInsertIndex(byte value) {
        int i = 0;
        while (i < this.symbolList.size() && this.symbolList.get(i).getValue() < value) {
            ++i;
        }
        return(i);
    }

    private void insertSymbol(byte value) {
        int index = this.findInsertIndex(value);
        this.symbolList.add(index, new Symbol(value, 1));
    }

    public void printTable() {
            System.out.println("Value        Frequency");
        int i = 0;
        while (i < this.symbolList.size()) {
            System.out.println(byteToBinaryString(this.symbolList.get(i).getValue()) + " : " + this.symbolList.get(i).getFrequency());
            ++i;
        }
    }

    public Symbol[] getSymbolsByFrequency() {
        ArrayList<Symbol> symbolListDuplicate = new ArrayList<Symbol>(this.symbolList);
        symbolListDuplicate.sort(
            (a, b) -> {
                return((int)(b.getFrequency() - a.getFrequency()));
            }
        );
        return(symbolListDuplicate.toArray(new Symbol[0]));
    }

    public static String byteToBinaryString(byte b) {
        return(byteToBinaryString(b, true));
    }
    public static String byteToBinaryString(byte b, boolean prefix) {
        return(
            (prefix ? "0b" : "") +
            String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(b))).replace(' ', '0')
        );
    }

}
