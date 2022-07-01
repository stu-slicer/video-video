package com.nology.videovideo.service.bytereader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleByteReader extends AbstractByteReader implements ByteReader {

    @Override
    public byte[] readByteRange(String filename, long start, long end) throws IOException {
        Path path = Paths.get(getFilePath(), filename);
        byte[] data = Files.readAllBytes(path);
        byte[] result = new byte[(int) (end - start) + 1];
        System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
        return result;
    }

}
