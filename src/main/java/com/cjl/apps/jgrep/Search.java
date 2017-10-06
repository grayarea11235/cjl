/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.apps.jgrep;

import com.cjl.streams.NumberedLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * TODO : Add a call back event for when result is found
 * 
 * @author ciaran
 */
public class Search {
    private final List<NumberedLine> results = new ArrayList<>();
    private final String path;
    private final String glob;
    private final String pattern;

    public Search(String path, String glob, String pattern) {
        this.path = path;
        this.glob = glob;
        this.pattern = pattern;
    }
    
    public List<NumberedLine> getResults() {
        return results;
    }
    
    public void run(boolean printResult) throws IOException {
        PathMatcher globMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        Pattern regPattern = Pattern.compile(pattern);

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(globMatcher::matches)
                    .forEach(p -> {
                        try (Stream<NumberedLine> stream = this.lines(p)) {
                            stream
                                .filter(line -> regPattern.matcher(line.getLine()).matches())
                                .forEach(l -> { 
                                    if (printResult) {
                                        System.out.println(l);
                                    }
                                    results.add(l);
                                });
                        } catch (IOException ex) {
                            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
        }
    }

    // https://stackoverflow.com/questions/29878981/java-8-equivalent-to-getlinenumber-for-streams
    // TODO : This could go into a utility class that could be common
    public Stream<NumberedLine> lines(Path p) throws IOException {
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

                        action.accept(new NumberedLine(++line, s, p));

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
