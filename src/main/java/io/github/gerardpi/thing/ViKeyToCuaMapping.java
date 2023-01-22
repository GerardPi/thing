package io.github.gerardpi.thing;

import com.google.common.collect.ImmutableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum ViKeyToCuaMapping {
    LEFT(ViKeyDef.LEFT, codeC(KeyCode.LEFT)),
    RIGHT(ViKeyDef.RIGHT, codeC(KeyCode.RIGHT)),
    UP(ViKeyDef.UP, codeC(KeyCode.UP)),
    DOWN(ViKeyDef.DOWN, codeC(KeyCode.DOWN)),
    START_OF_LINE(ViKeyDef.START_OF_LINE, codeC(KeyCode.HOME)),
    END_OF_LINE(ViKeyDef.END_OF_LINE, codeC(KeyCode.END), codeC(KeyCode.LEFT)),
    END_OF_FILE(ViKeyDef.END_OF_FILE, codeC(KeyCode.END, KeyCombination.CONTROL_DOWN)),
    CHAR_DELETE(ViKeyDef.CHAR_DELETE, codeC(KeyCode.DELETE)),
    CHAR_BACKSPACE_1(ViKeyDef.CHAR_BACKSPACE_1, codeC(KeyCode.BACK_SPACE)),
    CHAR_BACKSPACE_2(ViKeyDef.CHAR_BACKSPACE_2, codeC(KeyCode.BACK_SPACE)),
    PAGE_DOWN(ViKeyDef.PG_DOWN, codeC(KeyCode.PAGE_DOWN)),
    PAGE_UP(ViKeyDef.PG_UP, codeC(KeyCode.PAGE_UP)),
    SWITCH_TO_INSERT_MODE_AFTER(ViKeyDef.SWITCH_TO_INSERT_MODE_AFTER, codeC(KeyCode.RIGHT)),
    SWITCH_TO_INSERT_MODE_END_LINE(ViKeyDef.SWITCH_TO_INSERT_MODE_END_LINE, codeC(KeyCode.END)),
    SWITCH_TO_INSERT_MODE_BEGIN_LINE(ViKeyDef.SWITCH_TO_INSERT_MODE_BEGIN_LINE, codeC(KeyCode.HOME)),
    OPEN_LINE_ABOVE(ViKeyDef.OPEN_LINE_ABOVE, codeC(KeyCode.HOME), codeC(KeyCode.ENTER), codeC(KeyCode.UP)),
    OPEN_LINE_UNDER(ViKeyDef.OPEN_LINE_UNDER, codeC(KeyCode.END), codeC(KeyCode.ENTER)),
    DELETE_TO_EOL(ViKeyDef.DELETE_TO_EOL, codeC(KeyCode.END, KeyCombination.SHIFT_DOWN), codeC(KeyCode.DELETE)),
    ENTER(ViKeyDef.ENTER, codeC(KeyCode.ENTER));

    private static final List<KeyCodeCombination> NOTHING = Collections.emptyList();
    private final ViKeyDef viKeyDef;
    private final List<KeyCodeCombination> keyCodeCombinations;

    ViKeyToCuaMapping(ViKeyDef viKeyDef, KeyCodeCombination... keyCodeCombination) {
        this.viKeyDef = viKeyDef;
        this.keyCodeCombinations = ImmutableList.copyOf(keyCodeCombination);
    }

    public static Optional<ViKeyToCuaMapping> matches(ViKeyDef viKeyDef) {
        return Stream.of(values())
                .filter(vkd -> vkd.viKeyDef == viKeyDef)
                .findAny();
    }

    public static Optional<ViKeyToCuaMapping> matchesKeyRelease(FnEvent thingEvent) {
        return Stream.of(values())
                .filter(vkd -> thingEvent.matchesKeyRelease(vkd.viKeyDef))
                .findAny();
    }

    public static Optional<ViKeyToCuaMapping> matchesKeyTyped(FnEvent thingEvent) {
        return Stream.of(values())
                .filter(vkd -> thingEvent.matchesKeyTyped(vkd.viKeyDef))
                .findAny();
    }

    public static KeyCodeCombination codeC(KeyCode keyCode, KeyCombination.Modifier... modifiers) {
        return new KeyCodeCombination(keyCode, modifiers);
    }
    public List<KeyCodeCombination> getKeyCodeCombinations() {
        return keyCodeCombinations;
    }

    public ViKeyDef getViKeyDef() {
        return viKeyDef;
    }
}
