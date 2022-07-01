package com.nology.videovideo.service.bytereader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RandomAccessByteReader extends AbstractByteReader implements ByteReader {

    @Override
    public byte[] readByteRange(String filename, long start, long end) throws IOException {

        Path path = Paths.get(getFilePath(), filename);
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            int length = (int) ((end - start) + 1);
            raf.seek(start);
            byte[] toRead = new byte[length];
            raf.read(toRead, 0, length);
            return toRead;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
