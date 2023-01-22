package io.github.gerardpi.thing;

public enum Preconditions {
    ;
    public static void checkArgument(boolean expectedValue, String message) {
        if (!expectedValue) {
            throw new IllegalArgumentException(message);
        }
    }
}
