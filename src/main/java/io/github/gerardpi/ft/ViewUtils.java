package io.github.gerardpi.ft;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static javafx.scene.layout.Priority.ALWAYS;

public final class ViewUtils {
    private ViewUtils() {
        // No instantation allowed.
    }

    private static final Font FONT_DEFAULT = Font.font("Monospaced", 12.0);
    private static final Font FONT_LOG = Font.font("Monospaced", 10.0);

    public static TextField createCommandInput() {
        TextField commandInput = createLabel(createTooltip("command input"));
        HBox.setHgrow(commandInput, ALWAYS);
        return commandInput;
    }

    private static TextArea createTextArea(Font font, boolean editable) {
        TextArea log = new TextArea();
        log.setFont(font);
        log.setEditable(editable);
        VBox.setVgrow(log, ALWAYS);
        return log;
    }

    public static TextArea createLogTextArea() {
        return createTextArea(FONT_LOG, false);
    }

    public static TextArea createContentTextArea() {
        return createTextArea(FONT_DEFAULT, true);
    }

    public static TextField createLabel(Tooltip tooltip) {
        TextField label = createTextField(null, tooltip);
        label.setEditable(false);
        return label;
    }

    public static Node setMonospacedFont(Node node) {
        node.setStyle("-fx-font-family: 'monospaced'; -fx-font-size: 12px;");
        return node;
    }

    public static TextField createTextField(Pos alignment, Tooltip tooltip) {
        TextField textField = new TextField();
        if (alignment != null) {
            textField.setAlignment(alignment);
        }
        textField.setTooltip(tooltip);
        setMonospacedFont(textField);
        return textField;
    }

    public static TextField createLabel(Pos alignment, Tooltip tooltip) {
        TextField label = createTextField(alignment, tooltip);
        label.setEditable(false);
        return label;
    }

    public static TextField createLabel(Pos alignment, Tooltip tooltip, int preferredWidth) {
        TextField label = createTextField(alignment, tooltip);
        label.setPrefColumnCount(preferredWidth);
        label.setEditable(false);
        return label;
    }


    public static Tooltip createTooltip(String text) {
        return new Tooltip(text);
    }

    public static SplitPane createSplitPane(Node... items) {
        SplitPane splitPane = createSplitPane(Orientation.VERTICAL, items);
        VBox.setVgrow(splitPane, ALWAYS);
        return splitPane;
    }

    public static SplitPane createSplitPane(Orientation orientation, Node... items) {
        //https://bugs.openjdk.java.net/browse/JDK-8088118
        SplitPane splitPane = new SplitPane(items);

        splitPane.setOrientation(orientation);
        SplitPane.setResizableWithParent(splitPane, true);
        return splitPane;
    }

    public static void setDividerPositions(double pos1, double pos2, SplitPane splitPane) {
        Platform.runLater(() -> {
            Timeline t = new Timeline(new KeyFrame(Duration.millis(100),
                    actionEvent -> splitPane.setDividerPositions(pos1, pos2)));
            t.play();
        });
    }

    public static void setDividerPosition(int index, double position, SplitPane splitPane) {
        Platform.runLater(() -> {
            Timeline t = new Timeline(new KeyFrame(Duration.millis(100),
                    actionEvent -> splitPane.setDividerPosition(index, position)));
            t.play();
        });
    }
}
