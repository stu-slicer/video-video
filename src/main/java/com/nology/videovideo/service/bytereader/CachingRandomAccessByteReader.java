package com.nology.videovideo.service.bytereader;

import lombok.extern.log4j.Log4j2;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class CachingRandomAccessByteReader extends AbstractByteReader implements ByteReader {

    private Map<String, RandomAccessFile> files = new HashMap<>();

    @Override
    public byte[] readByteRange(String filename, long start, long end) throws IOException {

        RandomAccessFile file = getOpenFile(Paths.get(getFilePath(), filename));

        int length = (int) ((end - start) + 1);
        file.seek(start);
        byte[] toRead = new byte[length];
        file.read(toRead, 0, length);
        return toRead;
    }

    private RandomAccessFile getOpenFile(Path path) throws FileNotFoundException {
        RandomAccessFile raf = this.files.get(path.toString());
        if( raf == null ) {
            raf = new RandomAccessFile(path.toFile(), "r");
            this.files.put( path.toString(), raf);
        }
        return raf;
    }

    @PreDestroy
    protected void closeDown() {
        log.info("Closing them down!");
        this.files.values().stream()
                .forEach( f -> {
                    try {
                        f.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
