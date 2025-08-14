package src;

import java.util.ArrayList;

public class HuffmanTreeBuilder {
    
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
        while (i < treeList.size() && treeList.get(i).getFrequency() > tree.getFrequency()) {
            ++i;
        }
        treeList.add(i, tree);
        return(treeList);
    }

}
