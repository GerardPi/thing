package io.github.gerardpi.thing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class IoUtils {
    private static final Logger LOG = LoggerFactory.getLogger(IoUtils.class);

    private IoUtils() {
    }

    public static String loadTextFile(Class<?> aClass, String name) {
        return loadTextFile(getResourceOnClasspathToPath(aClass, name));
    }

    public static String loadTextFile(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            LOG.info("Reading {}", path.toString());
            return reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path getResourceOnClasspathToPath(Class<?> aClass, String name) {
        return Paths.get(getUri(aClass, name).getPath());
    }

    public static Optional<URI> fetchUri(Class<?> aClass, String name) {
        return fetchUrl(aClass, name).map(IoUtils::urlToUri);
    }
    public static URI getUri(Class<?> aClass, String name) {
        return fetchUri(aClass, name)
                .orElseThrow(() -> new IllegalStateException("Can not find '" + name + "' in the context of " + aClass.getName()));
    }

    private static URI urlToUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public static URL getUrl(Class<?> aClass, String name) {
        return fetchUrl(aClass, "thing.css")
                .orElseThrow(() -> new IllegalStateException("Can not find '" + name + "' in the context of " + aClass.getName()));
    }

    public static Optional<URL> fetchUrl(Class<?> aClass, String name) {
        URL url = aClass.getResource(name);
        if (url == null) {
            LOG.info("Could not find {} in the context of {}", name, aClass.getName());
        } else {
            LOG.info("Found {} in the context of {}: {}", name, aClass.getName(), url.getPath());
        }
        return Optional.ofNullable(url);
    }
}
