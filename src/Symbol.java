package src;

public class Symbol extends HuffmanNode {
    
    private byte value;
    private long frequency;

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

    public long getFrequency() {
        return(this.frequency);
    }

    public byte getValue() {
        return(this.value);
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
