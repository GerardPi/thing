package io.github.gerardpi.ft.editor;

import java.util.Arrays;
import java.util.Optional;

public enum LineSeparator {
    LF("\n"),
    CR("\r"),
    CRLF("\r\n"),
    DOS(CRLF.code),
    UNIX(LF.code);
    private static final LineSeparator DEFAULT = LF;
    private final String code;

    LineSeparator(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Optional<LineSeparator> fromString(String string) {
        return Arrays.stream(values())
                .filter(lineSeparator -> lineSeparator.name().equals(string))
                .findFirst();
    }

    public String displayValue() {
        return name();
    }

    public static LineSeparator getDefault() {
        return DEFAULT;
    }
}
