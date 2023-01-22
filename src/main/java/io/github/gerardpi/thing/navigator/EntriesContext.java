package io.github.gerardpi.thing.navigator;

import java.util.StringJoiner;
import java.util.TimeZone;

public class EntriesContext {
    private final String currentDirectory;
    private final String parentDirectory;
    private final int dirCount;
    private final int dirCountHidden;
    private final int fileCount;
    private final int fileCountHidden;
    private final String timeZoneId;
    private final String timeZoneName;
    private final int offset;

    public EntriesContext(int dirCount, int fileCount, int dirCountHidden, int fileCountHidden, String parentDirectory, String currentDirectory) {
        this.dirCount = dirCount;
        this.fileCount = fileCount;
        this.dirCountHidden = dirCountHidden;
        this.fileCountHidden = fileCountHidden;
        this.currentDirectory = currentDirectory;
        this.parentDirectory = parentDirectory;
        TimeZone currentTimeZone = TimeZone.getDefault();
        this.timeZoneId = currentTimeZone.getID();
        this.timeZoneName = currentTimeZone.getDisplayName();
        this.offset = currentTimeZone.getRawOffset();
    }

    public int getDirCount() {
        return dirCount;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirCountHidden() {
        return dirCountHidden;
    }

    public int getFileCountHidden() {
        return fileCountHidden;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public String getParentDirectory() {
        return parentDirectory;
    }

    public boolean hasParent() {
        return parentDirectory != null && parentDirectory.length() != 0;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EntriesContext.class.getSimpleName() + "[", "]")
                .add("currentDirectory='" + currentDirectory + "'")
                .add("parentDirectory='" + parentDirectory + "'")
                .add("dirCount=" + dirCount)
                .add("dirCountHidden=" + dirCountHidden)
                .add("fileCount=" + fileCount)
                .add("fileCountHidden=" + fileCountHidden)
                .add("timeZoneId='" + timeZoneId + "'")
                .add("timeZoneName='" + timeZoneName + "'")
                .add("offset=" + offset)
                .toString();
    }
}
