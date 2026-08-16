package com.snl.swing.game2026.dataStructure;

public class Bits {
    long bits[] = {0L};

    public Bits() {
    }

    public Bits(int nbits) {checkCapacity(nbits >>> 6);}

    private void checkCapacity(int length) {
        if (length >= bits.length) {
            long[] newBits = new long[length + 1];
            System.arraycopy(bits,0,newBits,0,bits.length);
            bits = newBits;
        }
    }
}
