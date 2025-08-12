package src;

public class HuffmanMiddleNode extends HuffmanNode {
    
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;

    public HuffmanMiddleNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.frequency = leftChild.getFrequency() + rightChild.getFrequency();
    }

    public HuffmanNode getLeft() {
        return(this.leftChild);
    }
    
    public HuffmanNode getRight() {
        return(this.rightChild);
    }

    public String getHuffmanCode(int value) {
        boolean right = false;
        String code = this.leftChild.getHuffmanCode(value);
        if (code == null) {
            right = true;
            code = this.rightChild.getHuffmanCode(value);
        }
        if (code != null) {
            code = (right ? "1" : "0") + code;
        }
        return(code);
    }

}
