package io.github.gerardpi.ft;

public class EmptyContext implements ContentContext {
    private final String name;

    public EmptyContext(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
