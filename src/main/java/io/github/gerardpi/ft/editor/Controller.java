package io.github.gerardpi.ft.editor;

import io.github.gerardpi.ft.*;

import java.util.Set;

public class Controller extends ContentController<EditorContext> {
    private final View view;

    public Controller(EditorContext editorContext) {
        super(editorContext);
        this.view = new View();
    }

    @Override
    public Set<Mode> getSupportedModes() {
        return Mode.ALL_MODES;
    }

    @Override
    public void markSelected(boolean selected) {

    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void searchAndSelect(String regularExpression) {

    }

    @Override
    public void emulateKeys(ViKeyToCuaMapping viKeyToCuaMapping) {

    }

    @Override
    public void handle(FnEvent te) {

    }

    @Override
    public int compareTo(ContentController contentController) {
        return 0;
    }

    public View getUi() {
        return view;
    }
}
