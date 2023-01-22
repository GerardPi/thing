package io.github.gerardpi.thing;

import javafx.scene.Node;

import java.util.Set;
import java.util.StringJoiner;

public abstract class ContentController<C extends ContentContext> implements Comparable<ContentController> {
    private final ContentContext contentContext;

    protected ContentController(C contentContext) {
        this.contentContext = contentContext;
    }

    public ContentContext getContext() {
        return contentContext;
    }

    public abstract Set<Mode> getSupportedModes();

    public abstract void markSelected(boolean selected);

    public abstract boolean isSelected();

    public abstract void searchAndSelect(String regularExpression);

    public abstract Node getUi();

    public abstract void emulateKeys(ViKeyToCuaMapping viKeyToCuaMapping);

    public abstract void handle(ThingEvent te);

    @Override
    public String toString() {
        return new StringJoiner(", ", ContentController.class.getSimpleName() + "[", "]")
                .add("contentContext=" + contentContext)
                .toString();
    }
}
