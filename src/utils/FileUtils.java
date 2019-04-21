package utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    public static List<String> readFileByLines(File file) {
        BufferedReader reader;
        List<String> lines = new LinkedList<>();

        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static void writeToFile(String content, String filePath) throws IOException {
        writeToFile(content, new File(filePath));
    }

    public static void writeToFile(String content, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (String string : content.split("\n")) {
            writer.write(string);
            writer.newLine();
        }

        writer.close();
    }
}
