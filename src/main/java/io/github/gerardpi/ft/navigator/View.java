package io.github.gerardpi.ft.navigator;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDateTime;

import static javafx.scene.layout.Priority.ALWAYS;


public class View extends VBox {

    private static final double COLWIDTH_PROFILE = 70;
    private static final double COLWIDTH_MODIFIED = 120;
    private static final double COLWIDTH_SIZE = 100;
    private static final double COLWIDTH_FIXED_PART = COLWIDTH_MODIFIED + COLWIDTH_PROFILE + COLWIDTH_SIZE;
    private static final double COLWITH_MIN_NAME = 100;
    private static final Callback<TableColumn<Entry, String>, TableCell<Entry, String>> PROFILE_CELL_FACTORY = new EntryProfileTableCellFactory();
    private static final Callback<TableColumn<Entry, Long>, TableCell<Entry, Long>> SIZE_CELL_FACTORY = new SizeTableCellFactory();
    private static final Callback<TableColumn<Entry, LocalDateTime>, TableCell<Entry, LocalDateTime>> DATE_TIME_CELL_FACTORY = new DateTimeTableCellFactory();
    private final TableView<Entry> tableView;
    private final NavigatorContextBox navigatorContextBox;

    public View() {
        this.tableView = createTableView();
        this.navigatorContextBox = new NavigatorContextBox();
        GridPane.setHgrow(navigatorContextBox, ALWAYS);
        getChildren().addAll(tableView, navigatorContextBox);
    }

    private static TableView<Entry> createTableView() {
        TableView<Entry> tableView = new TableView<>();
        tableView.getColumns().add(createProfileColumn());
        tableView.getColumns().add(createNameColumn(tableView));
        tableView.getColumns().add(createSizeColumn());
        tableView.getColumns().add(createModifiedColumn());
        tableView.setStyle("-fx-font-family: 'monospaced'; -fx-font-size: 12px;");
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, ALWAYS);
        return tableView;
    }

    private static TableColumn<Entry, String> createNameColumn(TableView<Entry> tableView) {
        TableColumn<Entry, String> column = new TableColumn<>(Entry.PROPNAME_NAME);
        column.setCellValueFactory(new PropertyValueFactory<Entry, String>(Entry.PROPNAME_NAME) {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry, String> param) {
                return new SimpleStringProperty(param.getValue().getName() +
                        (param.getValue().isDirectory() ? "/" : ""));
            }
        });
        column.setResizable(true);
        column.setMinWidth(COLWITH_MIN_NAME);
        column.prefWidthProperty().bind(tableView.widthProperty().subtract(COLWIDTH_FIXED_PART));
        return column;
    }


    private static TableColumn<Entry, String> createProfileColumn() {
        TableColumn<Entry, String> column = new TableColumn<>(Entry.PROPNAME_PROFILE);
        column.setCellFactory(PROFILE_CELL_FACTORY);
        column.setCellValueFactory(new PropertyValueFactory<Entry, String>(Entry.PROPNAME_PROFILE) {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry, String> param) {
                return new SimpleStringProperty(
                        param.getValue().getProfile() +
                        (param.getValue().isSelected() ? "[X]" : ""));
            }
        });
        column.setResizable(false);
        column.setPrefWidth(COLWIDTH_PROFILE);
        column.setMaxWidth(COLWIDTH_PROFILE);
        return column;
    }

    private static TableColumn<Entry, LocalDateTime> createModifiedColumn() {
        TableColumn<Entry, LocalDateTime> column = new TableColumn<>(Entry.PROPNAME_MODIFIED);
        column.setCellFactory(DATE_TIME_CELL_FACTORY);
        column.setCellValueFactory(new PropertyValueFactory<>(Entry.PROPNAME_MODIFIED));
        column.setResizable(false);
        column.setPrefWidth(COLWIDTH_MODIFIED);
        column.setMaxWidth(COLWIDTH_MODIFIED);
        return column;
    }

    private static TableColumn<Entry, Long> createSizeColumn() {
        TableColumn<Entry, Long> column = new TableColumn<>(Entry.PROPNAME_SIZE);
        column.setCellFactory(SIZE_CELL_FACTORY);
        column.setCellValueFactory(new PropertyValueFactory<>(Entry.PROPNAME_SIZE));
        column.setResizable(false);
        column.setPrefWidth(COLWIDTH_SIZE);
        column.setMaxWidth(COLWIDTH_SIZE);
        return column;
    }


    public void markSelected(boolean selected) {
        navigatorContextBox.markSelected(selected);
    }

    public ReadOnlyBooleanProperty getFocusedProperty() {
        return tableView.focusedProperty();
    }

    public TableView<Entry> getTableView() {
        return tableView;
    }
    public int getSelectedEntry() {
        return tableView.getSelectionModel().getSelectedIndex();
    }

    @Override
    public void requestFocus() {
        tableView.requestFocus();
    }

    public NavigatorContextBox getFileManagerContextBox() {
        return navigatorContextBox;
    }

}
