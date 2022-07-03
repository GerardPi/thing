package io.github.gerardpi;


public final class TestFunctions {

    private TestFunctions() {
        // No instantation allowed.
    }

    public static boolean matchesOrDoesNotMatch(final String isOrIsNotEqual) {
        return textToBoolean("matches", "does not match", isOrIsNotEqual);
    }

    public static boolean isOrIsNot(final String isOrIsNot) {
        return textToBoolean("is", "is not", isOrIsNot);
    }

    private static boolean textToBoolean(final String trueText, final String falseText, final String textToCheck) {
        if (trueText.equals(textToCheck)) {
            return true;
        }
        if (falseText.equals(textToCheck)) {
            return false;
        }
        throw new IllegalArgumentException("Invalid value '" + textToCheck + "'. Must be either '" + trueText + "' (=true) or '" + falseText + "' (=false).");
    }
}
