package io.github.gerardpi.thing;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class State {
    private static final Logger LOG = LoggerFactory.getLogger(State.class);


    private final SimpleObjectProperty<Mode> modeProperty;
    private final ObservableList<FnEvent> thingEventSequence;

    private boolean capsLockOn;

    public State() {
        this.modeProperty = new SimpleObjectProperty<>(Mode.NORMAL);
        this.thingEventSequence = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    }

    private static Mode logAndReturnNewMode(Mode oldMode, Mode newMode) {
        LOG.info("Left mode {}. New mode is {}", oldMode, newMode);
        return newMode;
    }

    public Mode getMode() {
        return modeProperty.get();
    }

    public SimpleObjectProperty<Mode> modeProperty() {
        return modeProperty;
    }

    public synchronized void leaveCommandMode() {
        if (modeProperty.get().isCommandMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.NORMAL));
        }
    }

    public synchronized void enterCommandMode() {
        if (!modeProperty.get().isCommandMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.COMMAND));
        }
    }

    public synchronized void leaveInsertMode() {
        if (modeProperty.get().isInsertMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.NORMAL));
        }
    }

    public synchronized void enterInsertMode() {
        if (!modeProperty.get().isInsertMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.INSERT));
        }
    }

    public synchronized void enterNormalMode() {
        if (!modeProperty.get().isNormalMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.NORMAL));
        }
    }

    public synchronized void enterSearchForwardInputMode() {
        modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.SEARCH_FORWARD));
    }

    public synchronized void enterSearchReverseInputMode() {
        if (!modeProperty.get().isNormalMode()) {
            modeProperty.set(logAndReturnNewMode(modeProperty.get(), Mode.SEARCH_REVERSE));
        }
    }

    public boolean isInNormalMode() {
        return getMode().isNormalMode();
    }

    public void addToSequence(FnEvent te) {
        thingEventSequence.add(te);
    }

    public void clearSequence() {
        thingEventSequence.clear();
    }

    public List<FnEvent> getThingEventSequence() {
        return ImmutableList.copyOf(thingEventSequence.iterator());
    }

    public boolean waitingForSequenceToComplete() {
        return thingEventSequence.size() > 0;
    }

    public State setCapsLockOn(boolean capsLockOn) {
        this.capsLockOn = capsLockOn;
        return this;
    }

    public boolean isCapsLockOn() {
        return capsLockOn;
    }

    public boolean modeIs(Mode mode) {
        return mode.equals(mode);
    }

}
