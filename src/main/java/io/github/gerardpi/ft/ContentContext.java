package io.github.gerardpi.ft;

public interface ContentContext extends Comparable<ContentContext> {
    @Override
    default int compareTo(ContentContext otherContentContext) {
        return String.CASE_INSENSITIVE_ORDER.compare(getName(), otherContentContext.getName());
    }

    String getName();
}
