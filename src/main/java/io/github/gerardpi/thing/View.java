package io.github.gerardpi.thing;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static io.github.gerardpi.thing.Converters.createModeStringConverter;
import static io.github.gerardpi.thing.Converters.createModeTooltipStringConverter;
import static io.github.gerardpi.thing.ViewUtils.*;

public class View extends VBox {

    private final TextField mode;
    private final Tooltip modeTooltip;
    private final TextField commandInput;
    private final SplitPane mainSplitPane;
    private final TextArea log;
    private final TabPane tabPane;

    private final Clock clock;

    public View() {
        this.log = createLogTextArea();
        this.commandInput = createCommandInput();
        this.clock = new Clock();
        this.modeTooltip = createTooltip("mode");
        this.mode = createLabel(Pos.BASELINE_CENTER, modeTooltip, 4);
        this.tabPane = new TabPane();
        this.mainSplitPane = createSplitPane(tabPane, new VBox(log, new HBox(mode, commandInput, clock)));
        getChildren().setAll(this.mainSplitPane);
        setDividerPositions(0.9, 0.1, mainSplitPane);
    }

    public TextField getMode() {
        return mode;
    }

    public Tooltip getModeTooltip() {
        return modeTooltip;
    }

    public TextField getCommandInput() {
        return commandInput;
    }

    public SplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    public TextArea getLog() {
        return log;
    }

    public void addKeyEventHandler(EventHandler<KeyEvent> keyEventEventHandler) {
        EventType<KeyEvent> anyKeyEvent = KeyEvent.ANY;
        commandInput.addEventHandler(anyKeyEvent, keyEventEventHandler);
        this.addEventHandler(anyKeyEvent, keyEventEventHandler);
        tabPane.addEventHandler(anyKeyEvent,keyEventEventHandler);
        mainSplitPane.addEventHandler(anyKeyEvent, keyEventEventHandler);
        mode.addEventHandler(anyKeyEvent, keyEventEventHandler);
        log.addEventHandler(anyKeyEvent, keyEventEventHandler);
    }

    public void bindMode(SimpleObjectProperty<Mode> modeProperty) {
        this.modeTooltip.textProperty()
                .bindBidirectional(modeProperty, createModeTooltipStringConverter());
        this.mode.textProperty()
                .bindBidirectional(modeProperty, createModeStringConverter());
    }

    public void focusToCommandInput() {
        commandInput.requestFocus();
        commandInput.clear();
        commandInput.setEditable(true);
    }

    public void focusToActiveTab() {
        tabPane.requestFocus();
    }

    public void setCapsLockIndicatorOn(boolean isOn) {
        if (isOn) {
            mode.setStyle("-fx-text-inner-color: white; -fx-background-color: red;");
        } else {
            mode.setStyle("-fx-text-inner-color: red; -fx-background-color: white;");
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
