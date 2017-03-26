package model;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSearcher {

    File file;

    public TextSearcher(File file) {
        this.file = file;
    }

    public List<SearchResult> find(String target) {
        if (file.isDirectory()) throw new RuntimeException("Using TextSearcher for directories is not allowed");
        List<SearchResult> results = new LinkedList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            try {
                String line;
                int lineCounter = 0;
                while ((line = br.readLine()) != null) {
                    lineCounter++;
                    Matcher matcher = Pattern.compile(target).matcher(line);
                    while (matcher.find()) {
                        results.add(new SearchResult(file, lineCounter, matcher.start()));
                    }
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            finally {
                br.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        return results;
    }
}
