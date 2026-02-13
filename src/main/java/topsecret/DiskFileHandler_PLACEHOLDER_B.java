package topsecret;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class DiskFileHandler_PLACEHOLDER_B implements FileHandler {

    private final Path dataDir;

    public DiskFileHandler_PLACEHOLDER_B(String dataDirName) {
        this.dataDir = Paths.get(dataDirName);
    }

    @Override
    public List<String> listDataFiles() throws IOException {
        List<String> out = new ArrayList<>();
        if (!Files.exists(dataDir) || !Files.isDirectory(dataDir)) return out;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir, "*.txt")) {
            for (Path p : stream) out.add(p.getFileName().toString());
        }
        return out;
    }

    @Override
    public String readFile(String filename) throws IOException {
        return Files.readString(dataDir.resolve(filename), StandardCharsets.UTF_8);
    }
}
