package io.github.gerardpi;

import com.tngtech.jgiven.format.ArgumentFormatter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EntryListFormatter implements ArgumentFormatter<List<String>> {
    public EntryListFormatter() {
    }

    @Override
    public String format(List<String> list, String... strings) {
        final AtomicInteger index = new AtomicInteger(0);
        String format = "\t\t- %" + minimalIndexWidth(list) + "d: '%s'";
        return System.lineSeparator() +
                list.stream().map(name -> String.format(format, index.incrementAndGet(), name))
                        .collect(Collectors.joining(System.lineSeparator()));
    }

    private int minimalIndexWidth(List<String> list) {
        int size = list.size();
        if (size < 10) {
            return 1;
        }
        if (size < 100) {
            return 2;
        }
        if (size < 1000) {
            return 3;
        }
        if (size < 10000) {
            return 4;
        }
        return 8;
    }
}
