package com.nology.videovideo.config;

import com.nology.videovideo.service.bytereader.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class ByteReaderConfig {

    @Value("${byte-reader.type}")
    private ByteReaderType readerType = ByteReaderType.cachingRandom;

    @Bean
    public ByteReader byteReader() {
        log.info(String.format("Byte read config: %s", readerType));
        switch (readerType) {
            case cachingRandom:
                return new CachingRandomAccessByteReader();
            case random:
                return new RandomAccessByteReader();
        }
        return new SimpleByteReader();
    }

}
