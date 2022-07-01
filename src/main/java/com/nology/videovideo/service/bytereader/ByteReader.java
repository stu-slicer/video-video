package com.nology.videovideo.service.bytereader;

import java.io.IOException;

public interface ByteReader {

    byte[] readByteRange(String filename, long start, long end) throws IOException;

}
