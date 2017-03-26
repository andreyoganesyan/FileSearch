package model;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.*;

import static java.lang.Thread.yield;

public class Searcher implements Callable<List<SearchResult>> {

    private File file;
    private String target;
    private String mask = "";


    public Searcher(File file, String target) {
        this.file = file;
        this.target=target;
    }


    public Searcher(File file,String target, String mask) {
        this(file,target);
        this.mask = mask;
    }

    public List<SearchResult> call() {
        if (file.isDirectory()) {
            LinkedList<SearchResult> innerSearchResults = new LinkedList<>();
            ExecutorService threadPool = Executors.newCachedThreadPool();
            List<Future<List<SearchResult>>> futureSearchResultLists = new LinkedList<>();
            for (File innerFile : file.listFiles()) {
                futureSearchResultLists.add(threadPool.submit(new Searcher(innerFile, target, mask)));
            }
            for(Future<List<SearchResult>> threadSearchResult : futureSearchResultLists){
                while(!threadSearchResult.isDone()) yield();
                try {
                    innerSearchResults.addAll(threadSearchResult.get());
                } catch (Exception e){
                    System.err.println(e);
                }
            }
            return innerSearchResults;
        } else {
            TextSearcher textSearcher = new TextSearcher(file);
            return textSearcher.find(target);
        }
    }

}
