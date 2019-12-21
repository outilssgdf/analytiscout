package org.leplan73.analytiscout.outils;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ResetableFileInputStream extends FilterInputStream {
	private FileChannel myFileChannel;
    private long mark = 0;

    public ResetableFileInputStream(FileInputStream fis) {
        super(fis);
        myFileChannel = fis.getChannel();
        mark(0);
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
