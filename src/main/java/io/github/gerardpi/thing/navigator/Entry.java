package io.github.gerardpi.thing.navigator;

import io.github.gerardpi.thing.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Entry implements Comparable<Entry> {
    private static final LocalDateTime NO_DATE_TIME = LocalDateTime.MIN;
    private static final Logger LOG = LoggerFactory.getLogger(Entry.class);
    public static final String NAME_THIS_DIR = ".";
    public static final String NAME_PARENT_DIR = "..";
    public static final String PROPNAME_NAME = "name";
    public static final String PROPNAME_SIZE = "size";
    public static final String PROPNAME_PROFILE = "profile";
    public static final String PROPNAME_CREATED = "created";
    public static final String PROPNAME_MODIFIED = "modified";
    public static final String PROPNAME_ACCESSED = "accessed";
    public static final String PROPNAME_HIDDEN = "hidden";
    public static final String PROPNAME_SELECTED = "selected";
    private final String name;
    private final long size;
    private final String profile;
    private final LocalDateTime created;
    private final LocalDateTime modified;
    private final LocalDateTime accessed;
    private final boolean hidden;
    private final boolean selected;
    public static final String PROPNAME_PROBLEM_MESSAGE = "problemMessage";
    public static final String PROPNAME_NAVIGATOR_EXCEPTION = "navigatorException";
    private final NavigatorException navigatorException;


    protected Entry(String name, long size, String profile, LocalDateTime created, LocalDateTime modified, LocalDateTime accessed, boolean hidden, boolean selected, NavigatorException navigatorException) {
        this.name = name;
        this.size = size;
        this.profile = profile;
        this.created = created;
        this.modified = modified;
        this.accessed = accessed;
        this.hidden = hidden;
        this.selected = selected;
        this.navigatorException = navigatorException;
    }

    public Entry toggleSelected() {
        return new Entry(name, size, profile, created, modified, accessed, hidden, !selected, navigatorException);
    }

    public boolean isSelected() {
        return selected;
    }


    public Optional<String> getProblemMessage() {
        return navigatorException == null
                ? Optional.empty()
                : Optional.of(navigatorException.getMessage());
    }

    public Optional<NavigatorException> getNavigatorException() {
        return Optional.ofNullable(navigatorException);
    }

    public static Builder of(Entry entry) {
        Builder builder = new Builder();
        builder.setName(entry.name);
        builder.setSize(entry.getSize());
        builder.setProfile(entry.profile);
        builder.setCreated(entry.created);
        builder.setModified(entry.modified);
        builder.setAccessed(entry.accessed);
        builder.setHidden(entry.hidden);
        builder.setSelected(entry.selected);
        builder.setNavigatorException(entry.navigatorException);
        return builder;
    }

    private static boolean isHidden(Entry entry) {
        return entry.hidden ||
                (!(NAME_PARENT_DIR.equals(entry.name) || NAME_THIS_DIR.equals(entry.name))
                        && (entry.name != null && entry.name.startsWith(".")));
    }

    public static Entry of(Path requestedPath) {
        try {
            LOG.info("requestedPath='{}'", requestedPath);
            Path path = Files.isSymbolicLink(requestedPath) ? Files.readSymbolicLink(requestedPath) : requestedPath;

            Result<BasicFileAttributes, NavigatorException> attributesResult = FileSystemUtils.getFileAttributes(path);
            long size = attributesResult.map(BasicFileAttributes::size).orElse(0L);
            String profile = attributesResult.map(attributes -> FileSystemUtils.getProfile(attributes, path)).orElse("!---");
            Result<Boolean, NavigatorException> hiddenResult = FileSystemUtils.isHidden(path);
            NavigatorException navigatorException = Stream.of(attributesResult, hiddenResult)
                    .filter(Result::hasInfoValue)
                    .map(Result::getInfoValue)
                    .findAny()
                    .orElse(null);
            return new Entry(FileSystemUtils.pathToString(path),
                    size,
                    profile,
                    attributesResult.map(attributes -> FileSystemUtils.toLocalDateTime(attributes.creationTime())).orElse(NO_DATE_TIME),
                    attributesResult.map(attributes -> FileSystemUtils.toLocalDateTime(attributes.lastModifiedTime())).orElse(NO_DATE_TIME),
                    attributesResult.map(attributes -> FileSystemUtils.toLocalDateTime(attributes.lastAccessTime())).orElse(NO_DATE_TIME),
                    hiddenResult.orElse(false),
                    false, navigatorException);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getProfile() {
        return profile;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public LocalDateTime getAccessed() {
        return accessed;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isDirectory() {
        return profile.charAt(0) == 'd';
    }

    public boolean isSymbolicLink() {
        return profile.charAt(0) == 'l';
    }

    public boolean isRegular() {
        return profile.charAt(0) == '-';
    }

    @Override
    public String toString() {
        return createToStringJoiner().toString();
    }

    protected StringJoiner createToStringJoiner() {
        return new StringJoiner(", ", Entry.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("size=" + size)
                .add("profile='" + profile + "'")
                .add("created=" + created)
                .add("modified=" + modified)
                .add("accessed=" + accessed)
                .add("hidden=" + hidden)
                .add("selected=" + selected)
                .add("navigatorException=" + navigatorException);
    }

    @Override
    public int compareTo(Entry o) {
        return String.CASE_INSENSITIVE_ORDER.compare(name, o.name);
    }

    public boolean hasProblem() {
        return this.navigatorException != null;
    }


    public static class Builder {
        protected String name;
        protected long size;
        protected String profile;
        protected LocalDateTime created;
        protected LocalDateTime modified;
        protected LocalDateTime accessed;
        protected boolean hidden;
        protected boolean selected = false;

        protected NavigatorException navigatorException;

        public Builder setNavigatorException(NavigatorException navigatorException) {
            this.navigatorException = navigatorException;
            return this;
        }

        public final Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public final Builder setSize(final long size) {
            this.size = size;
            return this;
        }

        public final Builder setProfile(final String profile) {
            this.profile = requireNonNull(profile);
            return this;
        }

        public final Builder setModified(final LocalDateTime modified) {
            this.modified = requireNonNull(modified);
            return this;
        }

        public final Builder setHidden(final boolean hidden) {
            this.hidden = requireNonNull(hidden);
            return this;
        }

        public final Builder setSelected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public Entry build() {
            return new Entry(name, size, profile, created, modified, accessed, hidden, selected, navigatorException);
        }

        public void setCreated(LocalDateTime created) {
            this.created = created;
        }

        public void setAccessed(LocalDateTime accessed) {
            this.accessed = accessed;
        }
    }
}
