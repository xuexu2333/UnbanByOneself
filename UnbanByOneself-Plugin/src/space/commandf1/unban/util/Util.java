package space.commandf1.unban.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.util.Objects;

public final class Util {
    public static String toSHA256String(String string) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            String temp;
            for (byte aByte : messageDigest.digest()) {
                temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(temp);
            }
            encodeStr = stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public static long getFileCreatedTime(File file) {
        long time;
        FileTime t;
        try {
            t = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class).creationTime();
            time = t.toMillis();
        } catch (IOException e) {
            time = System.currentTimeMillis();
            e.printStackTrace();
        }
        return time;
    }

    public static void clean(File file) {
        File[] files = file.listFiles();
        if (Objects.nonNull(files)) {
            for (File f : files) {
                if (f.isDirectory()) {
                    clean(f);
                }
                f.delete();
            }
        }
    }
}
