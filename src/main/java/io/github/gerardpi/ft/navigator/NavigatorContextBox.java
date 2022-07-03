package io.github.gerardpi.ft.navigator;

import javafx.beans.value.ObservableObjectValue;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import static io.github.gerardpi.ft.ViewUtils.createLabel;
import static io.github.gerardpi.ft.ViewUtils.createTooltip;
import static javafx.scene.layout.Priority.ALWAYS;

public class NavigatorContextBox extends HBox {
    public static final String STYLE_SELECTED = "-fx-text-inner-color: white; -fx-background-color: #005797; -fx-background-insets: 0; -fx-background-radius: 1;";
    private final TextField current;
    private final Tooltip parent;
    private final TextField dirs;
    private final TextField files;
    private final TextField dirsSelected;
    private final TextField filesSelected;
    private final TextField fileSizeSelected;
    private final TextField fileSizeTotal;
    private final Tooltip dirsHidden;
    private final Tooltip filesHidden;
    private final String styleNotSelected;

    public NavigatorContextBox() {
        setFocusTraversable(false);
        GridPane gridPane = new GridPane();
        parent = createTooltip("");
        current =  createLabel(parent);
        dirsHidden = createTooltip("");
        filesHidden = createTooltip("");
        dirs = createLabel(dirsHidden);
        files = createLabel(filesHidden);
        dirsSelected = createLabel(createTooltip("number of selected directories"));
        filesSelected = createLabel(createTooltip("number of selected files"));
        fileSizeSelected = createLabel(createTooltip("total size of selected files"));
        fileSizeTotal = createLabel(createTooltip("total size of files in this directory"));
        gridPane.add(dirs, 0, 0);
        gridPane.add(files, 1, 0);
        gridPane.add(fileSizeTotal, 2, 0);
        gridPane.add(dirsSelected, 3, 0);
        gridPane.add(filesSelected, 4, 0);
        gridPane.add(fileSizeSelected, 5, 0);
        gridPane.add(current, 0, 1, 6, 1);
        gridPane.getColumnConstraints().addAll(
                createColumnConstraints(10),
                createColumnConstraints(10),
                createColumnConstraints(30),
                createColumnConstraints(10),
                createColumnConstraints(10),
                createColumnConstraints(30)
        );
        GridPane.setFillWidth(current, true);
        this.styleNotSelected = current.getStyle();
        setHgrow(current, ALWAYS);
        HBox.setHgrow(gridPane, ALWAYS);
        getChildren().add(gridPane);
    }

    private static ColumnConstraints createColumnConstraints(double percentWidth) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(percentWidth);
        return columnConstraints;
    }

    public NavigatorContextBox bindCurrentPath(ObservableObjectValue<String> property) {
        this.current.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindParentPath(ObservableObjectValue<String> property) {
        this.parent.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindFileCount(ObservableObjectValue<String> property) {
        this.files.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindDirCount(ObservableObjectValue<String> property) {
        this.dirs.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindHiddenDirCount(ObservableObjectValue<String> property) {
        this.dirsHidden.textProperty().bind(property);
        return this;
    }
    public NavigatorContextBox bindDirCountSelected(ObservableObjectValue<String> property) {
        this.dirsSelected.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindFileCountSelected(ObservableObjectValue<String> property) {
        this.filesSelected.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindHiddenFileCount(ObservableObjectValue<String> property) {
        this.filesHidden.textProperty().bind(property);
        return this;
    }
    public NavigatorContextBox bindFileSizeTotal(ObservableObjectValue<String> property) {
        this.fileSizeTotal.textProperty().bind(property);
        return this;
    }

    public NavigatorContextBox bindFileSizeTotalSelected(ObservableObjectValue<String> property) {
        this.fileSizeSelected.textProperty().bind(property);
        return this;
    }

    public void markSelected(boolean selected) {
        if (selected) {
            current.setStyle(STYLE_SELECTED);
        } else {
            current.setStyle(styleNotSelected);
        }
    }

}
