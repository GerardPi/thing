package io.github.gerardpi.ft.editor;

import io.github.gerardpi.ft.ViewUtils;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class View extends VBox {
    private final TextArea content;

    public View() {
        this.content = ViewUtils.createContentTextArea();
        getChildren().setAll(this.content);
    }
}
