package parser;

import utils.FileUtils;

import java.io.File;
import java.util.List;

public class FileParser implements Parser<File, List<String>> {

    public FileParser() {
    }


    public List<String> parse(File file) {
        return FileUtils.readFileByLines(file);
    }
}
