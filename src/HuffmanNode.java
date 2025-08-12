package src;

abstract class HuffmanNode {
    /*
     * parent to both HuffmanMiddleNode and Symbol, allowing for either to be a child of a
     * HuffmanMiddleNode
     */

    protected long frequency;

    public long getFrequency() {
        return(this.frequency);
    }
    
}
