package io.github.gerardpi.thing.navigator;

import com.google.common.base.Strings;
import io.github.gerardpi.thing.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.input.KeyEvent.ANY;

public class NavigatorController extends ContentController<NavigatorContext> implements ChangeListener<String> {
    private static final Logger LOG = LoggerFactory.getLogger(NavigatorController.class);

    private final AtomicBoolean selected;
    private final View view;
    private final ObservableList<Entry> entriesList;
    private final FileSystemService fileSystemService;
    private final AtomicBoolean showHidden;
    private final AtomicReference<EntriesContext> entriesContext;

    public NavigatorController(NavigatorContext context) {
        super(context);
        this.entriesContext = new AtomicReference<>();
        this.showHidden = new AtomicBoolean(false);
        this.fileSystemService = new FileSystemService(FileSystems.getDefault());
        this.selected = new AtomicBoolean(false);
        this.view = new View();
        //bindFocusChanges(view, this);
        this.entriesList = FXCollections.observableArrayList(Collections.emptyList());
        reloadEntries(Paths.get(context.currentDirectoryProperty().getValue()));
        bind(view, getContext());
        view.getTableView().setItems(entriesList);
    }

    public void addAnyEventFilter(EventHandler<KeyEvent> eventHandler) {
        view.getTableView().addEventFilter(ANY, eventHandler);
    }

    private static void bind(View view, NavigatorContext navigatorContext) {
        NavigatorContextBox box = view.getFileManagerContextBox();
        box.bindCurrentPath(navigatorContext.currentDirectoryProperty())
                .bindParentPath(navigatorContext.parentDirectoryProperty())
                .bindDirCount(navigatorContext.dirCountProperty())
                .bindFileCount(navigatorContext.fileCountProperty())
                .bindHiddenDirCount(navigatorContext.dirCountHiddenProperty())
                .bindHiddenFileCount(navigatorContext.fileCountHiddenProperty())
                .bindDirCountSelected(navigatorContext.dirCountSelectedProperty())
                .bindFileCountSelected(navigatorContext.fileCountSelectedProperty())
                .bindFileSizeTotal(navigatorContext.fileSizeTotalProperty())
                .bindFileSizeTotalSelected(navigatorContext.fileSizeTotalSelectedProperty());
    }

    public void bindFocusChanges(ChangeListener<Boolean> changeListener) {
//        view.getFocusedProperty().addListener((observable, oldValue, newValue) -> controller.focusChanged(oldValue, newValue));
        view.getFocusedProperty().addListener(changeListener);
    }
    private void focusChanged(Boolean oldValue, Boolean newValue) {
        LOG.info("focusChanged... NOT IMPLEMENTED");
        //     getContentController().contentFocusChange(this, oldValue, newValue);
    }

    @Override
    public NavigatorContext getContext() {
        return (NavigatorContext) super.getContext();
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        searchAndSelect(newValue);
    }

    @Override
    public void searchAndSelect(String regularExpression) {
        if (!Strings.isNullOrEmpty(regularExpression)) {
            Result<Pattern, PatternSyntaxException> reResult = compile(regularExpression);
            if (reResult.successful()) {
                Pattern re = reResult.successValue();
                OptionalInt index = IntStream.range(0, entriesList.size())
                        .filter(i -> re.matcher(entriesList.get(i).getName().toLowerCase()).matches())
                        .findFirst();
                if (index.isPresent()) {
                    view.getTableView().getSelectionModel().select(index.getAsInt());
                    view.getTableView().scrollTo(index.getAsInt());
                    // https://stackoverflow.com/questions/26159224/tableview-visible-rows
                } else {
                    view.getTableView().getSelectionModel().clearSelection();
                }
            } else {
                LOG.warn("Can not compile regular expression '{}'. Message={}; descr={}", regularExpression,
                        reResult.getInfoValue().getMessage(),
                        reResult.getInfoValue().getDescription());
            }
        }
    }


    private Result<Pattern, PatternSyntaxException> compile(String reRaw) {
        String regExp = (reRaw.startsWith("^") ? "" : ".*")
                + reRaw +
                (reRaw.endsWith("$") ? "" : ".*");
        try {
            return Result.ready(Pattern.compile(regExp));
        } catch (PatternSyntaxException e) {
            return Result.of(e);
        }
    }

    private void reloadEntries(Path path) {
        int selectedEntryIndex = view.getSelectedEntry();
        Entries entries = getLoadedEntries(path);
        entriesList.clear();
        entriesList.addAll(entries.getEntryList(this.showHidden.get()));
        getContext().update(entries.getContext());
        entriesContext.set(entries.getContext());
        updateTotals();
        if (selectedEntryIndex < entriesList.size()) {
            view.getTableView().getSelectionModel().select(selectedEntryIndex);
        } else {
            view.getTableView().getSelectionModel().select(entriesList.size() - 1);
        }
    }

    private Entries getLoadedEntries(Path path) {
        Result<Entries, NavigatorException> loadedEntries = fileSystemService.readEntries(path);
        if (loadedEntries.of()) {
            LOG.warn("Can not read entries from filesystem: '{}' '{}'",
                    loadedEntries.getInfoValue().getType(),
                    loadedEntries.getInfoValue().getMessage());
            return Entries.EMPTY;
        }
        return loadedEntries.successValue();
    }

    @Override
    public void handle(FnEvent fnEvent) {
        ViKeyDef.findMatch(fnEvent).ifPresentOrElse(
                viKeyDef -> {
                    switch (viKeyDef) {
                        case FM_GO_TO_PARENT_DIRECTORY -> {
                            if (entriesContext.get().hasParent()) {
                                Path nextDir = Paths.get(entriesContext.get().getParentDirectory());
                                reloadEntries(nextDir);
                            }
                        }
                        case FM_SELECT_AND_GO_TO_NEXT -> {
                            updateSelection();
                            updateTotals();
                        }
                        case FM_GO_INTO_CHILD_DIRECTORY -> {
                            Entry selectedEntry = entriesList.get(view.getSelectedEntry());
                            if (selectedEntry.isDirectory()) {
                                Path nextDir = Paths.get(entriesContext.get().getCurrentDirectory(), selectedEntry.getName());
                                reloadEntries(nextDir);
                            }
                            LOG.info("selectedEntry = {}", selectedEntry);
                        }
                        default -> LOG.info("Don't know what to do with {} match '{}'", ViKeyDef.class.getSimpleName(), viKeyDef);
                    }
                },
                () -> {
                    LOG.info("No {} found for {} {}", ViKeyDef.class.getSimpleName(), FnEvent.class.getSimpleName(), fnEvent);
                });
    }


    private void updateSelection() {
        int selectedEntry = view.getSelectedEntry();
        Entry entry = entriesList.get(selectedEntry).toggleSelected();
        entriesList.set(view.getSelectedEntry(), entry);
        selectNextIndex(selectedEntry);
    }

    private void updateTotals() {
        long totalFileSize = totalFileSize();
        int selectedDirCount = (int) entriesList.stream().filter(e -> e.isDirectory() && e.isSelected()).count();
        List<Entry> selectedFiles = entriesList.stream().filter(e -> !e.isDirectory() && e.isSelected()).collect(Collectors.toList());
        int selectedFileCount = selectedFiles.size();
        long totalFileSizeSelected = selectedFiles.stream().mapToLong(Entry::getSize).sum();
        getContext().updateSelectionCounts(selectedFileCount, selectedDirCount, totalFileSizeSelected, totalFileSize);
    }

    private long totalFileSize() {
        List<Entry> allFiles = entriesList.stream().filter(e -> !e.isDirectory()).collect(Collectors.toList());
        return allFiles.stream().mapToLong(Entry::getSize).sum();
    }

    private void selectNextIndex(int currentEntryIndex) {
        int nextIndex = currentEntryIndex + 1;
        int maxIndex = entriesList.size() - 1;
        view.getTableView().getSelectionModel().select(Math.min(nextIndex, maxIndex));
    }

    @Override
    public Set<Mode> getSupportedModes() {
        return Mode.ALL_MODES_EXCEPT_INSERT;
    }

    @Override
    public void markSelected(boolean selected) {
        view.markSelected(selected);
        this.selected.set(selected);
    }

    @Override
    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public Node getUi() {
        return view;
    }

    @Override
    public int compareTo(ContentController otherContentController) {
        return this.getContext().compareTo(otherContentController.getContext());
    }

    @Override
    public void emulateKeys(ViKeyToCuaMapping viKeyToCuaMapping) {
        viKeyToCuaMapping.getKeyCodeCombinations().forEach(kcc ->
                KeyEventGeneration.emulateKey(kcc, view.getTableView()));
    }
}
