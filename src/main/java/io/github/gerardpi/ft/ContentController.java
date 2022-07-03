package io.github.gerardpi.ft;

import com.google.common.base.MoreObjects;
import javafx.scene.Node;

import java.util.Set;

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

    public abstract void handle(FnEvent te);

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("contentContext", contentContext)
                .toString();
    }
}
