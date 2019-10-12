package org.leplan73.outilssgdf.outils;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MarkableFileInputStream extends FilterInputStream {
	private FileChannel myFileChannel;
    private long mark = -1;

    public MarkableFileInputStream(FileInputStream fis) {
        super(fis);
        myFileChannel = fis.getChannel();
    }
    
    public MarkableFileInputStream(FileInputStream fis, int amark) {
        super(fis);
        myFileChannel = fis.getChannel();
        mark(amark);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void mark(int readlimit) {
        try {
            mark = myFileChannel.position();
        } catch (IOException ex) {
            mark = -1;
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        if (mark == -1) {
            throw new IOException("not marked");
        }
        myFileChannel.position(mark);
    }
}
