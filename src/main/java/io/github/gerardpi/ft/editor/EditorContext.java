package io.github.gerardpi.ft.editor;

import com.google.common.base.MoreObjects;
import io.github.gerardpi.ft.ContentContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class EditorContext implements ContentContext {
    public static final Path NO_PATH = Paths.get("##NONE##");
    public static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static LineSeparator DEFAULT_LINE_SEPARATOR = LineSeparator.LF;
    private final ObjectProperty<Path> filePath;
    private final ObjectProperty<Charset> encoding;
    private final ObjectProperty<LineSeparator> lineSeparator;

    public EditorContext(Path fullFilePath, Charset encoding, LineSeparator lineSeparator) {
        filePath = new SimpleObjectProperty<>(fullFilePath);
        this.lineSeparator = new SimpleObjectProperty<>(lineSeparator);
        this.encoding = new SimpleObjectProperty<>(encoding);
    }

    public EditorContext(Path fullFilePath) {
        this(fullFilePath, DEFAULT_CHARSET, DEFAULT_LINE_SEPARATOR);
    }

    public EditorContext() {
        this(NO_PATH, DEFAULT_CHARSET, DEFAULT_LINE_SEPARATOR);
    }

    public String getName() {
        return filePath.get().getFileName().toString();
    }

    public Optional<Path> getFilePath() {
        if (NO_PATH.equals(filePath.get())) {
            return Optional.empty();
        }
        return Optional.of(filePath.get());
    }

    public ObjectProperty<Path> filePathProperty() {
        return filePath;
    }

    public Charset getEncoding() {
        return encoding.get();
    }

    public ObjectProperty<Charset> encodingProperty() {
        return encoding;
    }

    public LineSeparator getLineSeparator() {
        return lineSeparator.get();
    }

    public ObjectProperty<LineSeparator> lineSeparatorProperty() {
        return lineSeparator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getName())
                .toString();
    }
}
