package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.command.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class FileSystemRepository {
    String IMAGE_RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath() + "/static/images/";

    public String save(Image image) throws Exception {
        Path newFile = Paths.get(IMAGE_RESOURCES_DIR + new Date().getTime() + "-" + image.getName());

        Files.createDirectories(newFile.getParent());

        Files.write(newFile,image.getContent());

        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource findInFileSystem(String location) {
        return new FileSystemResource(Paths.get(location));
    }

    public void deleteFileFromSystem(String location) throws Exception {
        Files.delete(Paths.get(location));
    }
}
