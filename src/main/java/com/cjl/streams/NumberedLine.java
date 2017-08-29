package com.cjl.streams;

/**
 *  A class that contains a String line and a number associated with 
 * it - usually a line number
 * 
 * @author cpd
 */
public class NumberedLine {
    private final int number;
    private final String line;
    
    NumberedLine(int number, String line) {
        this.number = number;
        this.line = line;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getLine() {
        return line;
    }
    
    @Override
    public String toString() {
        return number + ":\t" + line;
    }
}
