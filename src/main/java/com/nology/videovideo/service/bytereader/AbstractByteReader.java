package com.nology.videovideo.service.bytereader;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.URL;

import static com.nology.videovideo.constants.ApplicationConstants.VIDEO;

@Log4j2
public abstract class AbstractByteReader {

    private static final String DEFAULT_FILE_DIR = "D:\\_Development\\intellij\\workspace-nology\\video-video\\films";

    @Value("${film.directory}")
    private String filmDirectory;

    protected String getFilePath() {
        String path = new File(filmDirectory).getAbsolutePath();
        log.trace(path);
        return path;
    }


}
