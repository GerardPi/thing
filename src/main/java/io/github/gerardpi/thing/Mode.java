package io.github.gerardpi.thing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public enum Mode {
    NORMAL("N"),
    VISUAL("NV"),
    VISUAL_LINE("NVL"),
    VISUAL_BLOCK("NVB"),
    INSERT("I"),
    COMMAND("C:"),
    SEARCH_REVERSE("C?"),
    SEARCH_FORWARD("C/");
    public static final Set<Mode> ALL_MODES = ImmutableSet.copyOf(Mode.values());
    public static final Set<Mode> ALL_MODES_EXCEPT_INSERT = allModesExcept(Mode.INSERT);
    private final String code;
    Mode(String code) {
        this.code = code;
    }

    private static Set<Mode> allModesExcept(Mode... modesToExclude) {
        Set<Mode> modes = Sets.newHashSet(Mode.values());
        Stream.of(modesToExclude).forEach(modes::remove);
        return Collections.unmodifiableSet(modes);
    }

    public boolean isCommandMode() {
        return code.charAt(0) == 'C';
    }

    public boolean isNormalMode() {
        return code.charAt(0) == 'N';
    }

    public boolean isNotInsertMode() {
        return !isInsertMode();
    }

    public boolean isInsertMode() {
        return code.charAt(0) == 'I';
    }

    public String getDisplayValue() {
        return code;
    }
}
