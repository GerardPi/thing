package io.github.gerardpi.thing.navigator;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import static io.github.gerardpi.thing.HumanReadableFormatting.formatSize;

public class SizeTableCellFactory implements Callback<TableColumn<Entry, Long>, TableCell<Entry, Long>> {


    @Override
    public TableCell<Entry, Long> call(final TableColumn<Entry, Long> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(final Long item, final boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item != null) {
                        setText(formatSize(item));
                        setAlignment(Pos.BASELINE_RIGHT);
                    } else {
                        setText("?");
                    }
                }
            }
        };
    }
}
