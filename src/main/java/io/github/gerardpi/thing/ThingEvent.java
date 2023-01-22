package io.github.gerardpi.thing;

import javafx.scene.input.KeyEvent;

import java.util.StringJoiner;

public class ThingEvent {
    public static final String EMULATED_KEY_EVENT_TEXT = "emulated";
    private final boolean fromCommandInput;
    private final KeyEvent keyEvent;
    private final boolean keyReleased;
    private final boolean keyTyped;

    ThingEvent(boolean fromCommandInput, KeyEvent keyEvent) {
        this.fromCommandInput = fromCommandInput;
        this.keyEvent = keyEvent;
        this.keyReleased = KeyEvent.KEY_RELEASED.equals(keyEvent.getEventType());
        this.keyTyped = KeyEvent.KEY_TYPED.equals(keyEvent.getEventType());
    }

    public ThingEvent(KeyEvent keyEvent) {
        this(false, keyEvent);
    }

    public static String getModifiersString(KeyEvent keyEvent) {
        return (keyEvent.isAltDown() ? "A" : "a") +
                (keyEvent.isControlDown() ? "C" : "c") +
                (keyEvent.isMetaDown() ? "M" : "m") +
                (keyEvent.isShiftDown() ? "S" : "s");
    }

    public boolean isFromCommandInput() {
        return fromCommandInput;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public boolean isConsumed() {
        return keyEvent.isConsumed();
    }

    public boolean isNotConsumed() {
        return !keyEvent.isConsumed();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThingEvent.class.getSimpleName() + "[", "]")
                .add("fromCommandInput=" + fromCommandInput)
                .add("keyEvent=" + keyEvent)
                .add("keyReleased=" + keyReleased)
                .add("keyTyped=" + keyTyped)
                .add("modifiersString=" + getModifiersString(keyEvent))
                .toString();
    }

    public boolean isEmulated() {
        return EMULATED_KEY_EVENT_TEXT.equals(keyEvent.getText());
    }

    public boolean isNotEmulated() {
        return !EMULATED_KEY_EVENT_TEXT.equals(keyEvent.getText());
    }

    public boolean isEscape() {
        return keyReleased && ViKeyDef.isEscape(keyEvent);
    }

    public boolean isKeyReleased() {
        return keyReleased;
    }

    public boolean isKeyTyped() {
        return keyTyped;
    }

    public void consume() {
        keyEvent.consume();
    }

    public boolean isSwitchToInsertMode() {
        return keyReleased && ViKeyDef.switchToInsertMode(keyEvent).isPresent();
    }

    public boolean isSwitchToCommandMode() {
        return keyReleased && ViKeyDef.switchToCommandMode(keyEvent).isPresent();
    }

    public boolean matchesAny(ViKeyDef keyDef) {
        return keyDef.matchKeyEvent(keyEvent);
    }

    /**
     * @return Only matches when keyReleased is true.
     */
    public boolean matchesKeyRelease(ViKeyDef keyDef) {
        return keyReleased && keyDef.matchKeyEvent(keyEvent);
    }

    /**
     * @return Only matches when keyTyped is true.
     */
    public boolean matchesKeyTyped(ViKeyDef keyDef) {
        return keyTyped && keyDef.matchKeyEvent(keyEvent);
    }


}
