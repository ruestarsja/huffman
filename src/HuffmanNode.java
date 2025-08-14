package src;

abstract class HuffmanNode {

    protected long frequency;

    public long getFrequency() {
        return(this.frequency);
    }

    abstract String getHuffmanCode(int value);

}
