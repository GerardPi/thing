package io.github.gerardpi.thing.editor;

import io.github.gerardpi.thing.*;

import java.util.Set;

public class EditorController extends ContentController<EditorContext> {
    private final View view;

    public EditorController(EditorContext editorContext) {
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
    public void handle(ThingEvent te) {

    }

    @Override
    public int compareTo(ContentController contentController) {
        return 0;
    }

    public View getUi() {
        return view;
    }
}
