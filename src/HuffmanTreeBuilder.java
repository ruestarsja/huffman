package src;

import java.util.ArrayList;

public class HuffmanTreeBuilder {

    // TEMP BELOW!

    // public static void main(String[] args) {
    //     SymbolTable st = new SymbolTable();
    //     for (int i = 0; i < 1_000_000; ++i) {
    //         st.countSymbol((byte)(256*Math.random()));
    //     }
    //     st.printTable();
    //     HuffmanNode huffmanTree = symbolTableToTree(st);
    //     ArrayList<HuffmanNode> bfQueue = new ArrayList<HuffmanNode>();
    //     bfQueue.add(huffmanTree);
    //     while (bfQueue.size() != 0) {
    //         HuffmanNode node = bfQueue.removeFirst();
    //         if (node instanceof HuffmanMiddleNode) {
    //             HuffmanMiddleNode midNode = (HuffmanMiddleNode) node; // OK because it's already a HuffmanMiddleNode
    //             System.out.println("*");
    //             bfQueue.add(midNode.getLeft());
    //             bfQueue.add(midNode.getRight());
    //         } else if (node instanceof Symbol) {
    //             Symbol symbol = (Symbol) node; // OK because it's already a Symbol
    //             System.out.println(SymbolTable.byteToBinaryString(symbol.getValue()) + " : " + symbol.getFrequency());
    //         }
    //     }
    // }

    // TEMP ABOVE!
    
    public static HuffmanNode symbolTableToTree(SymbolTable symbolTable) {
        Symbol[] symbolArray = symbolTable.getSymbolsByFrequency();
        ArrayList<HuffmanNode> treeList = new ArrayList<HuffmanNode>();
        for (Symbol symbol: symbolArray) {
            treeList.add(symbol);
        }
        while (treeList.size() > 1) {
            HuffmanNode lowest = treeList.removeLast();
            HuffmanNode secondLowest = treeList.removeLast();
            HuffmanNode newTree = new HuffmanMiddleNode(secondLowest, lowest);
            insertTree(newTree, treeList);
        }
        return(treeList.get(0));
    }

    public static ArrayList<HuffmanNode> insertTree(HuffmanNode tree, ArrayList<HuffmanNode> treeList) {
        int i = 0;
        while (i < treeList.size() && treeList.get(i).getFrequency() < tree.getFrequency()) {
            ++i;
        }
        treeList.add(i, tree);
        return(treeList);
    }

}
