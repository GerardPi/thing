package io.github.gerardpi.thing.navigator;

import io.github.gerardpi.thing.ContentContext;
import io.github.gerardpi.thing.Strings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;

import java.nio.file.Path;
import java.util.StringJoiner;

import static io.github.gerardpi.thing.HumanReadableFormatting.formatSize;


public class NavigatorContext implements ContentContext {
    private static final String FORMAT_SUMMARY = "current=%s/ parent=%s/ dirs=%s files=%s dirs-hidden=%s files-hidden=%s";
    private final SimpleObjectProperty<String> currentDirectory;
    private final SimpleObjectProperty<String> parentDirectory;
    private final SimpleObjectProperty<String> dirCount;
    private final SimpleObjectProperty<String> fileCount;
    private final SimpleObjectProperty<String> fileCountHidden;
    private final SimpleObjectProperty<String> dirCountHidden;
    private final SimpleObjectProperty<String> dirCountSelected;
    private final SimpleObjectProperty<String> fileCountSelected;
    private final SimpleObjectProperty<String> fileSizeTotal;
    private final SimpleObjectProperty<String> fileSizeTotalSelected;

    public NavigatorContext(Path currentDirectory) {
        this.currentDirectory = new SimpleObjectProperty<>(currentDirectory.toString());
        parentDirectory = new SimpleObjectProperty<>("");
        dirCount = new SimpleObjectProperty<>("");
        fileCount = new SimpleObjectProperty<>("");
        dirCountHidden = new SimpleObjectProperty<>("");
        fileCountHidden = new SimpleObjectProperty<>("");
        fileCountSelected = new SimpleObjectProperty<>("0");
        dirCountSelected = new SimpleObjectProperty<>("0");
        fileSizeTotal = new SimpleObjectProperty<>("0");
        fileSizeTotalSelected = new SimpleObjectProperty<>("0");
    }

    public void update(EntriesContext entriesContext) {
        currentDirectory.set(entriesContext.getCurrentDirectory());
        parentDirectory.set(getParentDirectoryOrElse(entriesContext, "No parent directory available. Already at root."));
        dirCount.set("d:" + entriesContext.getDirCount());
        fileCount.set("f:" + entriesContext.getFileCount());
        dirCountHidden.set("hidden dirs: " + entriesContext.getDirCountHidden());
        fileCountHidden.set("hidden files: " + entriesContext.getFileCountHidden());
    }

    public String getFileSizeTotal() {
        return fileSizeTotal.get();
    }

    public SimpleObjectProperty<String> fileSizeTotalProperty() {
        return fileSizeTotal;
    }

    public String getFileSizeTotalSelected() {
        return fileSizeTotalSelected.get();
    }

    public SimpleObjectProperty<String> fileSizeTotalSelectedProperty() {
        return fileSizeTotalSelected;
    }

    public void updateSelectionCounts(int selectedFileCount, int selectedDirCount, long totalFileSizeSelected, long totalFileSize) {
        dirCountSelected.set("ds:" + selectedDirCount);
        fileCountSelected.set("fs:" + selectedFileCount);
        fileSizeTotal.set("ft:" + formatSize(totalFileSize) + " (" + totalFileSize + ")");
        fileSizeTotalSelected.set("fts:" + formatSize(totalFileSizeSelected) + " (" + totalFileSizeSelected + ")");
    }

    public String getDirCountSelected() {
        return dirCountSelected.get();
    }

    public SimpleObjectProperty<String> dirCountSelectedProperty() {
        return dirCountSelected;
    }

    public String getFileCountSelected() {
        return fileCountSelected.get();
    }

    public SimpleObjectProperty<String> fileCountSelectedProperty() {
        return fileCountSelected;
    }

    private static String getParentDirectoryOrElse(EntriesContext entriesContext, String defaultText) {
        String parent = entriesContext.getParentDirectory();
        if (Strings.isNullOrEmpty(parent)) {
            return defaultText;
        }
        return parent;
    }

    public static StringConverter<NavigatorContext> createNameConverter() {
        return new StringConverter<NavigatorContext>() {
            @Override
            public String toString(NavigatorContext navigatorContext) {
                return navigatorContext.getName();
            }

            @Override
            public NavigatorContext fromString(String string) {
                return null;
            }
        };
    }

    public static StringConverter<NavigatorContext> createSummaryConverter() {
        return new StringConverter<NavigatorContext>() {
            @Override
            public String toString(NavigatorContext navigatorContext) {
                // FORMAT_SUMMARY = "current=%s parent=%s dirs=%s files=%s dirs-hidden=%s files-hidden=%s";
                return String.format(FORMAT_SUMMARY,
                        navigatorContext.getCurrentDirectory(),
                        navigatorContext.getParentDirectory(),
                        navigatorContext.getDirCount(),
                        navigatorContext.getFileCount(),
                        navigatorContext.getDirCountHidden(),
                        navigatorContext.getFileCountHidden());
            }

            @Override
            public NavigatorContext fromString(String string) {
                return null;
            }
        };
    }

    @Override
    public String getName() {
        return currentDirectory.get();
    }

    public String getCurrentDirectory() {
        return currentDirectory.get();
    }

    public SimpleObjectProperty<String> currentDirectoryProperty() {
        return currentDirectory;
    }

    public String getParentDirectory() {
        return parentDirectory.get();
    }

    public SimpleObjectProperty<String> parentDirectoryProperty() {
        return parentDirectory;
    }

    public String getDirCount() {
        return dirCount.get();
    }

    public SimpleObjectProperty<String> dirCountProperty() {
        return dirCount;
    }

    public String getFileCount() {
        return fileCount.get();
    }

    public SimpleObjectProperty<String> fileCountProperty() {
        return fileCount;
    }

    public String getFileCountHidden() {
        return fileCountHidden.get();
    }

    public SimpleObjectProperty<String> fileCountHiddenProperty() {
        return fileCountHidden;
    }

    public String getDirCountHidden() {
        return dirCountHidden.get();
    }

    public SimpleObjectProperty<String> dirCountHiddenProperty() {
        return dirCountHidden;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NavigatorContext.class.getSimpleName() + "[", "]")
                .add("name=" + getName())
                .toString();
    }
}
