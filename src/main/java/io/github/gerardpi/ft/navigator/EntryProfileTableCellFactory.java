package io.github.gerardpi.ft.navigator;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryProfileTableCellFactory implements Callback<TableColumn<Entry, String>, TableCell<Entry, String>> {
    private static final Logger LOG = LoggerFactory.getLogger(EntryProfileTableCellFactory.class);

    @Override
    public TableCell<Entry, String> call(final TableColumn<Entry, String> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(final String item, final boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Entry entry = getTableRow().getItem();
                    if (entry != null) {
                        LOG.info("Entry: '{}'", entry);
                        entry.getProblemMessage().ifPresent(tooltip -> setTooltip(new Tooltip(tooltip)));
                    } else {
                        LOG.info("Item: '{}'", item);
                    }
                    setText(item);
                }
            }
        };
    }
}
