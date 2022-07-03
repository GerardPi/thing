package io.github.gerardpi.ft;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static javafx.scene.layout.Priority.ALWAYS;

public class Clock extends TextField {
    private static final Font FONT = Font.font("Monospaced", 10.0);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("YYYY-mm-dd HH:MM:ss");
    public Clock() {
        this.setAlignment(Pos.BASELINE_LEFT);
        setPrefColumnCount(20);
        setEditable(false);
        setFont(FONT);
        createTimeline();
        VBox.setVgrow(this, ALWAYS);

 //       setContextMenu(createContextMenu());
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyAllMenuItem = new MenuItem("Copy all");
//        copyAllMenuItem.addEventHandler(this);
        contextMenu.getItems().add(copyAllMenuItem);
        return contextMenu;
    }

    private void createTimeline() {
        Timeline timeline = createTimeLine();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private Timeline createTimeLine() {
        return new Timeline(
                new KeyFrame(Duration.seconds(0), e -> this.setText(LocalDateTime.now().format(DATE_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));
    }
}
