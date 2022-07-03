package io.github.gerardpi.ft.navigator;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Entries {
    public static final Entries EMPTY = new Entries(
            Collections.emptySortedSet(), Collections.emptySortedSet(), "", "");
    private final SortedSet<Entry> dirEntries;
    private final SortedSet<Entry> fileEntries;
    private final EntriesContext context;

    public Entries(final SortedSet<Entry> dirEntries, final SortedSet<Entry> fileEntries, final String currentDirectory, final String parentDirectory) {
        this.context = new EntriesContext(dirEntries.size(), fileEntries.size(),
                countHiddenEntries(dirEntries), countHiddenEntries(fileEntries),
                parentDirectory, currentDirectory);
        this.dirEntries = dirEntries;
        this.fileEntries = fileEntries;
    }

    private static int countHiddenEntries(Collection<Entry> entries) {
        return (int) entries.stream().filter(entry -> entry.isHidden()).count();
    }

    public long getTotalFileSize() {
        return fileEntries.stream()
                .mapToLong(Entry::getSize)
                .sum();
    }

    public int getDirCount() {
        return context.getDirCount();
    }

    public int getFileCount() {
        return context.getFileCount();
    }

    public int getHiddenFileCount() {
        return context.getFileCountHidden();
    }

    public int getHiddenDirCount() {
        return context.getDirCountHidden();
    }

    public SortedSet<Entry> getDirEntries() {
        return dirEntries;
    }

    public SortedSet<Entry> getFileEntries() {
        return fileEntries;
    }

    public List<Entry> getFileEntriesList() {
        return new ArrayList<>(fileEntries);
    }
    public List<Entry> getDirEntriesList() {
        return new ArrayList<>(dirEntries);
    }

    public List<Entry> getEntryList(boolean includeHidden) {
        List<Entry> entryList = new ArrayList<>(dirEntries.size() + fileEntries.size());
        entryList.addAll(dirEntries.stream()
                .filter(entry -> !entry.isHidden() || includeHidden)
                .collect(Collectors.toList()));
        entryList.addAll(fileEntries.stream()
                .filter(entry -> !entry.isHidden() || includeHidden)
                .collect(Collectors.toList()));
        return entryList;
    }

    public List<Entry> getEntryList() {
        return getEntryList(true);
    }

    public String getCurrentDirectory() {
        return context.getCurrentDirectory();
    }

    public String getParentDirectory() {
        return context.getParentDirectory();
    }

    private static Set<String> pathsToEntrySet(Set<Path> paths) {
        return paths.stream()
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toSet());
    }

    public String getTimeZoneId() {
        return context.getTimeZoneId();
    }

    public String getTimeZoneName() {
        return context.getTimeZoneName();
    }

    public int getOffset() {
        return context.getOffset();
    }

    public EntriesContext getContext() {
        return context;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Entries.class.getSimpleName() + "[", "]")
                .add("context=" + context)
                .add("dirEntries=" + dirEntries)
                .add("fileEntries=" + fileEntries)
                .toString();
    }
}
