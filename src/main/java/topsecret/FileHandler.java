package topsecret;

import java.util.List;

public interface FileHandler {
    List<String> listDataFiles() throws Exception;
    String readFile(String filename) throws Exception;
}
