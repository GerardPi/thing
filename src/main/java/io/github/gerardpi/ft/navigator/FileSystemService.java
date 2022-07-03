package io.github.gerardpi.ft.navigator;


import io.github.gerardpi.ft.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import static io.github.gerardpi.ft.navigator.NavigatorException.Operation.READ_DIRECTORY_ENTRIES;
import static io.github.gerardpi.ft.navigator.NavigatorException.Type.*;

public class FileSystemService {
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemService.class);

    private final FileSystem fileSytem;

    public FileSystemService(FileSystem fileSytem) {
        this.fileSytem = fileSytem;
    }

    private Path resolvePath(Path path) {
        return Paths.get(path.toFile().getAbsolutePath());
    }

    private Result<Entries, NavigatorException> readEntries(Path requestedPath, DirectoryStream.Filter<Path> pathFilter, String glob) {
        Path path = resolvePath(requestedPath);
        if (Files.exists(path)) {
            try {
                SortedSet<Entry> dirEntries = new TreeSet<>();
                SortedSet<Entry> fileEntries = new TreeSet<>();
                try (DirectoryStream<Path> directoryStream =
                             pathFilter == null
                                     ? Files.newDirectoryStream(path, glob)
                                     : Files.newDirectoryStream(path, pathFilter)) {
                    for (Path childPath : directoryStream) {
                        Entry entry = Entry.of(childPath);
                        if (entry.isDirectory()) {
                            dirEntries.add(entry);
                        } else {
                            fileEntries.add(entry);
                        }
                    }
                }
                return Result.ready(new Entries(dirEntries, fileEntries, path.toString(), getParentPath(path)));
            } catch (SecurityException e) {
                return Result.of(new NavigatorException(READ_DIRECTORY_ENTRIES, SECURITY_ERROR, path.toString(), e));
            } catch (NotDirectoryException e) {
                return Result.of(new NavigatorException(READ_DIRECTORY_ENTRIES, NOT_DIRECTORY, path.toString(), e));
            } catch (PatternSyntaxException e) {
                return Result.of(new NavigatorException(READ_DIRECTORY_ENTRIES, PATTERN_SYNTAX_INVALID, path.toString(), e));
            } catch (IOException e) {
                return Result.of(new NavigatorException(READ_DIRECTORY_ENTRIES, IO_ERROR, path.toString(), e));
            }
        } else {
            return Result.of(new NavigatorException(READ_DIRECTORY_ENTRIES, ENTRY_NOT_FOUND, path.toString()));
        }
    }


    public Result<Entries, NavigatorException> readEntries(String path) {
        return readEntries(getPath(path));
    }

    public Result<Entries, NavigatorException> readEntries(Path path) {
        return readEntries(path, entry -> true);
    }

    public Result<Entries, NavigatorException> readEntries(Path path, DirectoryStream.Filter<Path> pathFilter) {
        return readEntries(path, Objects.requireNonNull(pathFilter), null);
    }

    public Result<Entries, NavigatorException> readEntries(Path path, String glob) {
        return readEntries(path, null, Objects.requireNonNull(glob));
    }

    public Entry readEntry(Path path) {
        return Entry.of(path);
    }

    public Path getPath(String aPathPart, String... morePathParts) {
        return fileSytem.getPath(aPathPart, morePathParts);
    }

    private String getParentPath(Path path) {
        return Optional
                .ofNullable(path.getParent())
                .map(Path::toString)
                .orElse("");
    }

    public void createSymbolicLink(Path target, Path link) {
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
