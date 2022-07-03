package io.github.gerardpi.ft.navigator;


import io.github.gerardpi.ft.OsDetector;
import io.github.gerardpi.ft.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static io.github.gerardpi.ft.navigator.NavigatorException.Operation.READ_ALL_LINES;
import static io.github.gerardpi.ft.navigator.NavigatorException.Type.IO_ERROR;

public final class FileSystemUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemUtils.class);

    private FileSystemUtils() {
        // No instantation allowed.
    }

    public static String getPermissions(String entryPath) {
        return getPermissions(Paths.get(entryPath).toFile());
    }

    public static String getPermissions(File file) {
        return (file.canRead() ? "r" : "-")
                + (file.canWrite() ? "w" : "-")
                + (file.canExecute() ? "x" : "-");
    }

    public static String getPermissions(Path path) {
        return (Files.isReadable(path) ? "r" : "-")
                + (Files.isWritable(path) ? "w" : "-")
                + (Files.isExecutable(path) ? "x" : "-");
    }

    public static String getProfile(BasicFileAttributes attributes, Path path) {
        return getFileType(attributes) + getPermissions(path);
    }

    public static String getFileType(BasicFileAttributes attributes) {
        if (attributes.isRegularFile()) {
            return "-";
        } else if (attributes.isDirectory()) {
            return "d";
        } else if (attributes.isSymbolicLink()) {
            return "l";
        } else if (attributes.isOther()) {
            return "o";
        }
        throw new IllegalStateException("None of the 4 expected flags was set. This should never happen.");
    }

    public static Path getClassPathResource(String name, Class<?> baseClass) {
        URL url = baseClass.getResource(name);
        if (url != null) {
            return Paths.get(url.getPath());
        }
        throw new IllegalStateException("Can not find resource with name '" + name + "' in the context of class '" + baseClass.getName() + "'");
    }

    public static InputStream getClassPathResourceInputStream(String name, Class<?> baseClass) {
        File inputFile = Paths.get(getClassPathResource(name, baseClass).toString()).toFile();
        try {
            return new BufferedInputStream(new FileInputStream(inputFile));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getFileContents(Path path, String encoding) {
        return String.join("", getFileLines(path, encoding));
    }

    private static final BiFunction<Path, Charset, String> TO_MESSAGE_FORMAT_PATH_AND_ENCODING = (path, encoding) ->
            String.format("%s with encoding %s", path, encoding).toString();

    public static List<String> getFileLines(Path path, String encodingStr) {
        Charset encoding = Charset.forName(encodingStr);
        try {
            return Files.readAllLines(path, encoding);
        } catch (IOException e) {
            throw new NavigatorException(READ_ALL_LINES, IO_ERROR,
                    TO_MESSAGE_FORMAT_PATH_AND_ENCODING.apply(path, encoding), e);
        }
    }

    public static long stream(Path path, OutputStream outputStream) {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(path.toFile()));
            return stream(inputStream, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stream<String> linesStream(Path path, String encodingStr) {
        Charset encoding = Charset.forName(encodingStr);
        try {
            return Files.lines(path, encoding);
        } catch (IOException e) {
            throw new NavigatorException(NavigatorException.Operation.STREAM_LINES, IO_ERROR,
                    TO_MESSAGE_FORMAT_PATH_AND_ENCODING.apply(path, encoding));
        }
    }

    // http://stackoverflow.com/questions/10142409/write-an-inputstream-to-an-httpservletresponse
    public static long stream(InputStream input, OutputStream output) {
        try (ReadableByteChannel inputChannel = Channels.newChannel(input);
             WritableByteChannel outputChannel = Channels.newChannel(output)) {

            ByteBuffer buffer = ByteBuffer.allocate(10240);
            long size = 0;

            while (inputChannel.read(buffer) != -1) {
                buffer.flip();
                size += outputChannel.write(buffer);
                buffer.clear();
            }

            return size;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Path getWorkingDirectory() {
        return new File(".").toPath();
    }

    public static Path getUserHomeDirectory() {
        return Paths.get(System.getProperty("user.home"));
    }

    public static Path getTempDirectory() {
        return Paths.get(System.getProperty("java.io.tmpdir", OsDetector.isWindows() ? "c:/temp" : "/tmp"));
    }

    public static long getSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Result<BasicFileAttributes, NavigatorException> getFileAttributes(Path path) {
        try {
            return Result.ready(Files.readAttributes(path, BasicFileAttributes.class));
        } catch (NoSuchFileException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_ATTRIBUTES, NavigatorException.Type.ENTRY_NOT_FOUND, path.toString(), e));
        } catch (UnsupportedOperationException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_ATTRIBUTES, NavigatorException.Type.OPERATION_NOT_SUPPORTED, path.toString(), e));
        } catch (SecurityException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_ATTRIBUTES, NavigatorException.Type.SECURITY_ERROR, path.toString(), e));
        } catch (IOException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_ATTRIBUTES, IO_ERROR, path.toString(), e));
        }
    }

    public static LocalDateTime toLocalDateTime(FileTime fileTime) {
        return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
    }

    public static Result<Boolean, NavigatorException> isHidden(Path path) {
        try {
            return Result.ready(Files.isHidden(path));
        } catch (SecurityException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_HIDDEN, NavigatorException.Type.SECURITY_ERROR, path.toString(), e));
        } catch (IOException e) {
            return Result.of(new NavigatorException(NavigatorException.Operation.GET_HIDDEN, IO_ERROR, path.toString(), e));
        }
    }

    public static boolean isDirectory(String profile) {
        return profile.charAt(0) == 'd';
    }

    public static boolean isSymbolicLink(String profile) {
        return profile.charAt(0) == 'l';
    }

    public static boolean isRegular(String profile) {
        return profile.charAt(0) == '-';
    }

    public static UserPrincipal getOwner(Path path) {
        try {
            return Files.getOwner(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String pathToString(Path path) {
        if (path == null) {
            return "";
        } else {
            Path fileName = path.getFileName();
            if (fileName == null) {
                return "/";
            } else {
                return fileName.toString();
            }
        }
    }
}
