package src;

public class Symbol extends HuffmanNode {
    
    private int value;

    public Symbol(int value) {
        this(value, 0);
    }
    public Symbol(int value, long frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public void count() {
        this.count(1);
    }
    public void count(long instances) {
        this.frequency += instances;
    }

    public int getValue() {
        return(this.value);
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public String getHuffmanCode(int value) {
        if (this.value == value) {
            return("");
        } else {
            return(null);
        }
    }

}
