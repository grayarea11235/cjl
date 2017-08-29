/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.utils;

import com.cjl.streams.LineNumberedFiles;
import com.cjl.streams.NumberedLine;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author ciaran
 */
public class GrepSearch {
    private final List<NumberedLine> results = new ArrayList<>();
    private final String path;
    private final String glob;
    private final String pattern;

    public GrepSearch(String path, String glob, String pattern) {
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
                        try (Stream<NumberedLine> stream = LineNumberedFiles.lines(p)) {
                            stream
                                .filter(line -> regPattern.matcher(line.getLine()).matches())
                                .forEach(l -> { 
                                    if (printResult) {
                                        System.out.println(l);
                                    }
                                    results.add(l);
                                });
                        } catch (IOException ex) {
                            Logger.getLogger(GrepSearch.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
        }
    }
}