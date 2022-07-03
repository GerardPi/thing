package io.github.gerardpi.ft;

import javafx.util.StringConverter;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Converters {

    public static final String NO_PATH_AVAILABLE = "[no name]";
    public static final String DEFAULT_APPLICATION_TITLE = "[no name] - JEditor";
    public static final String DEFAULT_ENCODING_PLACEHOLDER = "[?]";
    public static final String DEFAULT_LINE_SEPARATOR_PLACEHOLDER = "[?]";

    public static StringConverter<Mode> createModeStringConverter() {
        return new StringConverter<Mode>() {
            @Override
            public String toString(Mode object) {
                return object.getDisplayValue();
            }

            @Override
            public Mode fromString(String string) {
                return Mode.valueOf(string);
            }
        };
    }

    public static StringConverter<Mode> createModeTooltipStringConverter() {
        return new StringConverter<Mode>() {
            @Override
            public String toString(Mode object) {
                return Mode.class.getSimpleName() + ": " + object.getDisplayValue() + " (= " + object.name() + ")";
            }

            @Override
            public Mode fromString(String string) {
                return Mode.valueOf(string);
            }
        };
    }

    public static StringConverter<Path> createPathStringConverter() {
        return new StringConverter<Path>() {
            @Override
            public String toString(Path path) {
                if (path != null) {
                    return path.toString();
                }
                return NO_PATH_AVAILABLE;
            }

            @Override
            public Path fromString(String path) {
                return Paths.get(path);
            }
        };
    }

    public static StringConverter<Path> createTitleStringConverter() {
        return new StringConverter<Path>() {
            @Override
            public String toString(Path path) {
                if (path != null) {
                    return path.toString();
                }
                return DEFAULT_APPLICATION_TITLE;
            }

            @Override
            public Path fromString(String path) {
                return Paths.get(path);
            }
        };
    }

    public static StringConverter<Charset> createCharsetStringConverter() {
        return new StringConverter<Charset>() {
            @Override
            public String toString(Charset charset) {
                if (charset != null) {
                    return charset.toString();
                }
                return DEFAULT_ENCODING_PLACEHOLDER;
            }

            @Override
            public Charset fromString(String path) {
                return Charset.forName(path);
            }
        };
    }

    public static StringConverter<LineSeparator> lineSeparatorStringConverter() {
        return new StringConverter<LineSeparator>() {
            @Override
            public String toString(LineSeparator lineSeparator) {
                if (lineSeparator != null) {
                    return lineSeparator.displayValue();
                }
                return DEFAULT_LINE_SEPARATOR_PLACEHOLDER;
            }

            @Override
            public LineSeparator fromString(String string) {
                return LineSeparator.fromString(string).orElse(LineSeparator.getDefault());
            }
        };
    }

//    public static StringConverter<ContentContext> contextConverter() {
//        return new StringConverter<ContentContext>() {
//            @Override
//            public String toString(ContentContext context) {
//                return context.getName();
//            }
//
//            @Override
//            public ContentContext fromString(String string) {
//                return new ContentContext(string);
//            }
//        };
//    }
}

