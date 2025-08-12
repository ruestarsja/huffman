package src;

public class Symbol extends HuffmanNode {
    
    private byte value;

    public Symbol(byte value) {
        this(value, 0);
    }
    public Symbol(byte value, long frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public void count() {
        this.count(1);
    }
    public void count(long instances) {
        this.frequency += instances;
    }

    public byte getValue() {
        return(this.value);
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
