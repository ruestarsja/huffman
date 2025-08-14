package src;

import java.util.ArrayList;

public class SymbolTable {
    
    private ArrayList<Symbol> symbolList;

    public SymbolTable() {
        this.symbolList = new ArrayList<Symbol>();
    }

    public void countSymbol(int value) {
        int index = this.findSymbolIndex(value);
        if (index == -1) {
            this.insertSymbol(value);
        } else {
            this.symbolList.get(index).count();
        }
    }

    public void printTable() {
            System.out.println("Frequency : Value");
        int i = 0;
        while (i < this.symbolList.size()) {
            System.out.println(
                String.format("%9s", this.symbolList.get(i).getFrequency())
                    .replace(' ', '0')
                + " : "
                + Main.byteToBinaryString(this.symbolList.get(i).getValue())
            );
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

    private int findSymbolIndex(int value) {
        // Annoying that I had to look up pseudocode for binary search, I should know this :(
        // https://pseudoeditor.com/guides/binary-search
        int start = 0;
        int end = this.symbolList.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            int midSymbolValue = this.symbolList.get(mid).getValue();
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

    private int findInsertIndex(int value) {
        int i = 0;
        while (i < this.symbolList.size() && this.symbolList.get(i).getValue() < value) {
            ++i;
        }
        return(i);
    }

    private void insertSymbol(int value) {
        int index = this.findInsertIndex(value);
        this.symbolList.add(index, new Symbol(value, 1));
    }

}
