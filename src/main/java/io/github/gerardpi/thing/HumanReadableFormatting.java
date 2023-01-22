package io.github.gerardpi.thing;

public final class HumanReadableFormatting {
    private HumanReadableFormatting() {
        // No instantiation.
    }
    public static String formatSize(Long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
}
