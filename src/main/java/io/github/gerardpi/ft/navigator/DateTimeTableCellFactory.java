package io.github.gerardpi.ft.navigator;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTableCellFactory implements Callback<TableColumn<Entry, LocalDateTime>, TableCell<Entry, LocalDateTime>> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public TableCell<Entry, LocalDateTime> call(final TableColumn<Entry, LocalDateTime> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(final LocalDateTime item, final boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item != null) {
                        setText(item.format(DATE_TIME_FORMATTER_SHORT));
                        this.setTooltip(new Tooltip(item.format(DATE_TIME_FORMATTER)));
                    } else {
                        setText("?");
                    }
                }
            }
        };
    }
}
