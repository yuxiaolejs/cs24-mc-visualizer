package com.tianleyu.cs24mc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static List<String> RF(String filePath) throws Exception {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer C2I(char i) {
        if (i >= '0' && i <= '9') {
            return i - '0';
        } else if (i >= 'a' && i <= 'f') {
            return i - 'a' + 10;
        } else if (i >= 'A' && i <= 'F') { // Optional: handling uppercase hexadecimal characters
            return i - 'A' + 10;
        } else {
            throw new IllegalArgumentException("Invalid character: " + i);
        }
    }
}
