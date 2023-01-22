package io.github.gerardpi.thing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingChecker {

    private static final Pattern RE_UNORDERED_LIST_RX = Pattern.compile( "^[ \\t]*(-|\\*+|\u2022)[ \t]+.*$");

    public static void main(String[] args) {
        AtomicInteger lineCounter = new AtomicInteger(0);
        Path path = Paths.get("/home/gjpiek/Documents/docs/clojure/conv.adoc");
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            reader.lines().forEach(line -> {
                Matcher matcher = RE_UNORDERED_LIST_RX.matcher(line);
                lineCounter.incrementAndGet();
            });
            System.out.println("Loaded " + lineCounter.get() + " lines");
//            int c = reader.read();
//            if (c == 0xfeff) {
//                System.out.println("File starts with a byte order mark.");
//            } else if (c >= 0) {
//                reader.transferTo(Writer.nullWriter());
//            }
        } catch (CharacterCodingException e) {
            System.out.println("Not a UTF-8 file.");
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
