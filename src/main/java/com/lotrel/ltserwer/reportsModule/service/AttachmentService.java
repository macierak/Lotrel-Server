package com.lotrel.ltserwer.reportsModule.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
public class AttachmentService {

    public File getFile(String path) throws FileNotFoundException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);

        if (Objects.isNull(resource)) {
           throw new FileNotFoundException("Nie można znaleźć pliku.");
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new FileNotFoundException("Nie można pobrać pliku.");
            }
        }
    }

    public OffsetDateTime getCreationTime(File file) throws IOException {
        try {
            final FileTime fileTime = (FileTime) Files.getAttribute(Path.of(file.getPath()), "creationTime");
            return OffsetDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            throw new IOException("Nie można pobrać daty utworzenia pliku.");
        }
    }

}
