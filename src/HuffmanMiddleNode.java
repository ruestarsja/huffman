package src;

public class HuffmanMiddleNode extends HuffmanNode {
    
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;

    public HuffmanMiddleNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public HuffmanNode getLeft() {
        return(this.leftChild);
    }
    
    public HuffmanNode getRight() {
        return(this.rightChild);
    }

}
