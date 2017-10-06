package com.cjl.streams;

/**
 *  A class that contains a String line and a number associated with 
 * it - usually a line number
 * 
 * @author cpd
 */

import java.nio.file.Path;

/**
 *
 * @author cpd
 */
public class NumberedLine {
    private final int number;
    private final String line;
    private final Path path;
    
    public NumberedLine(int number, String line, Path path) {
        this.number = number;
        this.line = line;
        this.path = path;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getLine() {
        return line;
    }

    public Path getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        return path.getFileName() + ":" + number + ":\t" + line;
    }
}
