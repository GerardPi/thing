package io.github.gerardpi.thing;

import com.google.common.base.MoreObjects;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyComb {
    private static final Logger LOG = LoggerFactory.getLogger(KeyComb.class);

    private final KeyCode code;
    private final String character;
    private final boolean shift;
    private final boolean ctrl;
    private final boolean alt;
    private final boolean meta;
    private final KeyCategory keyCategory;

    public KeyComb(KeyCode code, KeyCategory keyCategory, String character, boolean shift, boolean ctrl, boolean alt, boolean meta) {
        this.code = code;
        this.character = character;
        this.shift = shift;
        this.ctrl = ctrl;
        this.alt = alt;
        this.meta = meta;
        this.keyCategory = keyCategory;
    }

    public KeyComb(KeyCode code, String character) {
        this(code, KeyCategory.UNDEFINED, character, false, false, false, false);
    }

    public KeyComb(KeyCode code) {
        this(code, KeyCategory.UNDEFINED, null, false, false, false, false);
    }

    public static KeyComb comb(KeyCode code, String keyChar) {
        return new KeyComb(code, keyChar);
    }

    public static KeyComb comb(KeyCode code) {
        return new KeyComb(code);
    }

    public static KeyComb someArrow() {
        return new KeyComb(null, KeyCategory.ARROW, null, false, false, false, false);
    }

    public static KeyComb someDigit() {
        return new KeyComb(null, KeyCategory.DIGIT, null, false, false, false, false);
    }


    public static KeyComb someViMotion() {
        return new KeyComb(null, KeyCategory.VI_MOTION, null, false, false, false, false);
    }

    public static KeyComb someViOperation() {
        return new KeyComb(null, KeyCategory.VI_OPERATION, null, false, false, false, false);
    }

    public static KeyComb someViOperationWithMotion() {
        return new KeyComb(null, KeyCategory.VI_OPERATION_WITH_MOTION, null, false, false, false, false);
    }

    public static KeyComb someLetter() {
        return new KeyComb(null, KeyCategory.LETTER, null, false, false, false, false);
    }

    public static KeyComb combShiftDown(KeyCode keyCode) {
        return new KeyComb(keyCode, KeyCategory.UNDEFINED, null, true, false, false, false);
    }

    public static KeyComb combShiftDown(KeyCode keyCode, String keyChar) {
        return new KeyComb(keyCode, KeyCategory.UNDEFINED, keyChar, true, false, false, false);
    }

    public static KeyComb combCtrlDown(KeyCode keyCode) {
        return new KeyComb(keyCode, KeyCategory.UNDEFINED, null, false, true, false, false);
    }

    public static KeyComb combCtrlDown(KeyCode keyCode, String keyChar) {
        return new KeyComb(keyCode, KeyCategory.UNDEFINED, keyChar, false, true, false, false);
    }

    private boolean matchesClass(KeyEvent keyEvent, KeyCategory keyClass) {
        switch (keyClass) {
            case VI_MOTION:
                return ViKeyDef.isViMotion(keyEvent);
            case VI_OPERATION:
                return ViKeyDef.isViOperation(keyEvent);
            case DIGIT:
                return keyEvent.getCode().isDigitKey();
            case LETTER:
                return keyEvent.getCode().isLetterKey();
            case ARROW:
                return keyEvent.getCode().isArrowKey();
            default:
                LOG.warn("{} is not one of {}", keyEvent.getCode().getName(), KeyCategory.values());
                return false;
        }
    }

    boolean match(KeyEvent keyEvent) {
        boolean modifiersMatch = keyEvent.isAltDown() == alt &&
                keyEvent.isControlDown() == ctrl &&
                keyEvent.isShiftDown() == shift &&
                keyEvent.isMetaDown() == meta;
        if (modifiersMatch) {
            if (keyCategory != KeyCategory.UNDEFINED) {
                // Maybe do something when getCharacter == nothing
                return matchesClass(keyEvent, keyCategory);
            } else if (KeyCode.UNDEFINED == keyEvent.getCode() && strEquals(character, keyEvent.getCharacter())) {
                return true;
            }
            return code == keyEvent.getCode();
        }
        return false;
    }

    private boolean strEquals(String one, String other) {
        if (one == null) {
            return other == null;
        }
        return one.equals(other);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("keyCategory", keyCategory)
                .add("character", character)
                .add("modifiers", modifiersToString())
                .toString();
    }

    private String modifiersToString() {
        return (alt ? "a" : "-") +
                (ctrl ? "c" : "-") +
                (meta ? "m" : "-") +
                (shift ? "s" : "-");

    }

    public KeyCode getCode() {
        return code;
    }

    public String getCharacter() {
        return character;
    }

    public boolean isShift() {
        return shift;
    }

    public boolean isCtrl() {
        return ctrl;
    }

    public boolean isAlt() {
        return alt;
    }

    public boolean isMeta() {
        return meta;
    }

    public static enum KeyCategory {
        UNDEFINED,
        LETTER,
        DIGIT,
        ARROW,
        VI_MOTION,
        VI_OPERATION,
        VI_OPERATION_WITH_MOTION;
    }
}
