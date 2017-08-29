/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *  
 * 
 * @author cpd
 */
public class LineNumberedFiles {

    // https://stackoverflow.com/questions/29878981/java-8-equivalent-to-getlinenumber-for-streams
    /**
     * 
     * @param p
     * @return
     * @throws IOException 
     */
    public static Stream<NumberedLine> lines(Path p) throws IOException {
        BufferedReader b = Files.newBufferedReader(p);

        Spliterator<NumberedLine> sp = new Spliterators.AbstractSpliterator<NumberedLine>(
                Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL) {
                    int line;

                    @Override
                    public boolean tryAdvance(Consumer<? super NumberedLine> action) {
                        String s;
                        try {
                            s = b.readLine();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }

                        if (s == null) {
                            return false;
                        }

                        action.accept(new NumberedLine(++line, s));

                        return true;
                    }
                };
        
        return StreamSupport.stream(sp, false).onClose(() -> {
            try {
                b.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
    
