package model;

import java.io.File;

public class SearchResult {
    private File file;
    private int line;
    private int pos;

    public SearchResult(File file, int line, int pos) {
        this.file = file;
        this.line = line;
        this.pos = pos;
    }

    public File getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }
}
