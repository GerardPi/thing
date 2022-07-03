package io.github.gerardpi.ft;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import static io.github.gerardpi.ft.FnEvent.EMULATED_KEY_EVENT_TEXT;


public final class KeyEventGeneration {
    private KeyEventGeneration() {
    }

    public static void emulateKey(KeyCodeCombination kcc, Node node) {
        KeyEvent keyPressedEvent = createKeyEvent(KeyEvent.KEY_PRESSED, kcc);
        node.fireEvent(keyPressedEvent);

        KeyEvent keyReleasedEvent = createKeyEvent(KeyEvent.KEY_RELEASED, kcc);
        node.fireEvent(keyReleasedEvent);
    }


    private static KeyEvent createKeyEvent(EventType<KeyEvent> eventType, KeyCodeCombination keyCodeCombination) {
        boolean shiftDown = keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN;
        boolean controlDown = keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN;
        boolean altDown = keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN;
        boolean metaDown = keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN;

        return new KeyEvent(eventType, null, EMULATED_KEY_EVENT_TEXT,
                keyCodeCombination.getCode(),
                shiftDown, controlDown, altDown, metaDown);
    }
}
