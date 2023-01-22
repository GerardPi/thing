package io.github.gerardpi.thing;

import com.google.common.base.MoreObjects;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


import static io.github.gerardpi.thing.KeyComb.*;

public enum ViKeyDef {
    ESCAPE_1(comb(KeyCode.ESCAPE)),
    ESCAPE_2(combCtrlDown(KeyCode.OPEN_BRACKET)),
    UP(comb(KeyCode.K, "k"), true),
    DOWN(comb(KeyCode.J, "j"), true),
    LEFT(comb(KeyCode.H, "h"), true),
    RIGHT(comb(KeyCode.L, "l"), true),
    WORD_FORWARD(comb(KeyCode.W, "w"), true),
    WORD_BACKWARD(comb(KeyCode.B, "b"), true),
    WORD_FORWARD_2(combShiftDown(KeyCode.W, "W"), true),
    WORD_BACKWARD_2(combShiftDown(KeyCode.B, "B"), true),
    PG_DOWN(combCtrlDown(KeyCode.F, "f")),
    PG_UP(combCtrlDown(KeyCode.B, "b")),
    SWITCH_TO_INSERT_MODE(comb(KeyCode.I, "i")),
    SWITCH_TO_INSERT_MODE_AFTER(comb(KeyCode.A, "a")),
    SWITCH_TO_INSERT_MODE_BEGIN_LINE(combShiftDown(KeyCode.I, "I")),
    SWITCH_TO_INSERT_MODE_END_LINE(comb(KeyCode.A, "A")),
    SWITCH_TO_COMMAND_INPUT(combShiftDown(KeyCode.SEMICOLON, ":")),
    SWITCH_TO_SEARCH_INPUT(comb(KeyCode.SLASH, "/")),
    SWITCH_TO_BACKWARD_SEARCH_INPUT(combShiftDown(KeyCode.SLASH, "?")),
    FOCUS_TO_NEXT_CONTENT(combCtrlDown(KeyCode.W)),
    SOME_DIGIT(someDigit()),
    SOME_MOTION(someViMotion()),
    SOME_OPERATION(someViOperation()),
    SOME_MOTION_OPERATION(someViOperationWithMotion()),
    REPLACE_CHAR(comb(KeyCode.R, "r")),
    START_OF_LINE(comb(KeyCode.DIGIT0, "0")),
    END_OF_LINE(combShiftDown(KeyCode.DIGIT4, "$")), // $
    END_OF_FILE(combShiftDown(KeyCode.G, "G"), true),
    GO_TO_LINE(comb(KeyCode.G, "g"), true), // twice!!
    CHAR_DELETE(comb(KeyCode.X, "x")),
    DELETE(comb(KeyCode.D, "d")),
    CHAR_BACKSPACE_1(combShiftDown(KeyCode.X, "X")),
    CHAR_BACKSPACE_2(comb(KeyCode.BACK_SPACE)),
    FM_GO_TO_PARENT_DIRECTORY(comb(KeyCode.BACK_SPACE)),
    DELETE_TO_EOL(combShiftDown(KeyCode.D, "D")),
    OPEN_LINE_ABOVE(combShiftDown(KeyCode.O, "O")),
    OPEN_LINE_UNDER(comb(KeyCode.O, "o")),
    TOGGLE_CASE(combShiftDown(KeyCode.BACK_QUOTE, "~"), true),
    LINE_YANK(comb(KeyCode.Y, "y"), true),
    SEARCH_WORD_UNDER_CURSOR_FORWARD(combShiftDown(KeyCode.DIGIT8, "*")),
    SEARCH_WORD_UNDER_CURSOR_BACKWARD(combShiftDown(KeyCode.DIGIT4, "#")),
    LINE_DELETE(comb(KeyCode.D, "d"), true), // Combined with digit
    START_SELECT(comb(KeyCode.V, "v")),
    START_SELECT_LINE(combShiftDown(KeyCode.V, "v")),
    TAB(comb(KeyCode.TAB)),
    FM_SELECT_AND_GO_TO_NEXT(comb(KeyCode.INSERT)),
    FM_GO_INTO_CHILD_DIRECTORY(comb(KeyCode.ENTER)),
    ENTER(comb(KeyCode.ENTER));


    private static final Logger LOG = LoggerFactory.getLogger(ViKeyDef.class);

    private static final List<ViKeyDef> ESCAPE_MODES = Arrays.asList(ESCAPE_1, ESCAPE_2);
    private static final List<ViKeyDef> TO_INSERT_MODES = Arrays.asList(
            SWITCH_TO_INSERT_MODE,
            SWITCH_TO_INSERT_MODE_AFTER,
            SWITCH_TO_INSERT_MODE_BEGIN_LINE,
            SWITCH_TO_INSERT_MODE_END_LINE,
            OPEN_LINE_ABOVE,
            OPEN_LINE_UNDER
    );
    private static final Set<ViKeyDef> MOTION_KEY_DEFS = java.util.Set.of(ViKeyDef.RIGHT, ViKeyDef.LEFT, ViKeyDef.UP, ViKeyDef.DOWN);
    private static Set<ViKeyDef> TO_INSERT_MODE_KEY_DEFS = java.util.Set.of(
            SWITCH_TO_INSERT_MODE,
            SWITCH_TO_INSERT_MODE_BEGIN_LINE,
            SWITCH_TO_INSERT_MODE_END_LINE,
            SWITCH_TO_INSERT_MODE_AFTER
    );
    private static Set<ViKeyDef> TO_COMMAND_MODE_KEY_DEFS = java.util.Set.of(SWITCH_TO_COMMAND_INPUT);
    private final KeyComb keyComb;
    private final boolean mayBePrefixedByNumber;

    private ViKeyDef(KeyComb keyComb, boolean mayBePrefixedByNumber) {
        this.keyComb = keyComb;
        this.mayBePrefixedByNumber = mayBePrefixedByNumber;
    }

    ViKeyDef(KeyComb keyComb) {
        this(keyComb, false);
    }

    public static Optional<ViKeyDef> findMatch(FnEvent thingEvent) {
        return Stream.of(values())
                .filter(kd -> kd.keyComb.match(thingEvent.getKeyEvent()))
                .findAny();
    }

    public static boolean isViMotion(KeyEvent keyEvent) {
        return MOTION_KEY_DEFS.stream().anyMatch(viKeyDef -> viKeyDef.matchKeyEvent(keyEvent));
    }

    public static boolean isViMotion(ViKeyDef viKeyDef) {
        return MOTION_KEY_DEFS.stream().anyMatch(motionViKeyDef -> motionViKeyDef.equals(viKeyDef));
    }

    public static boolean isViOperation(KeyEvent keyEvent) {
        return "~xXnN".contains(keyEvent.getCharacter());
        // ~ toggle case
        // x delete forward char(s)
        // X delete previous char(s)
        // n next
    }

    public static boolean isViOperationWithMotion(KeyEvent keyEvent) {
        return "dDcCrR".contains(keyEvent.getCharacter());
    }

    public static Optional<ViKeyDef> switchToInsertMode(KeyEvent keyEvent) {
        return TO_INSERT_MODE_KEY_DEFS.stream()
                .filter(viKeyDef -> viKeyDef.keyComb.match(keyEvent))
                .findFirst();
    }

    public static Optional<ViKeyDef> switchToCommandMode(KeyEvent keyEvent) {
        return TO_COMMAND_MODE_KEY_DEFS.stream()
                .filter(viKeyDef -> viKeyDef.keyComb.match(keyEvent))
                .findFirst();
    }

    public static boolean isEscape(KeyEvent keyEvent) {
        return ESCAPE_1.keyComb.match(keyEvent) || ESCAPE_2.keyComb.match(keyEvent);
    }

    public static boolean isEnter(KeyEvent keyEvent) {
        return ENTER.keyComb.match(keyEvent);
    }

    public static boolean isTab(KeyEvent keyEvent) {
        return TAB.keyComb.match(keyEvent);
    }

    public boolean matchThingEvent(FnEvent fnEvent) {
        return keyComb.match(fnEvent.getKeyEvent());
    }

    public boolean matchKeyEvent(KeyEvent keyEvent) {
        return keyComb.match(keyEvent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("keyComb", keyComb)
                .add("#px", mayBePrefixedByNumber)
                .toString();
    }

    public boolean isEscape() {
        return ESCAPE_MODES.contains(this);
    }

    public boolean isToInsertMode() {
        return TO_INSERT_MODES.contains(this);
    }
}
