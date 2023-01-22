package io.github.gerardpi.thing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


public class Main extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String LOGO_FILENAME = "logo.png";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        LOG.info("javaVersion: '{}'; javafxVersion: '{}'", javaVersion, javafxVersion);
        MainController mainController = new MainController();
        Scene scene = new Scene(mainController.getUi(), 600, 200);
        scene.getStylesheets().add(IoUtils.getUrl(getClass(), "thing.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        loadLogo().ifPresent(logo -> stage.getIcons().add(logo));
        stage.setMaximized(true);
        stage.titleProperty().bind(mainController.contextProperty());
    }

    private static Optional<Image> loadLogo() {
        try (InputStream inputStream = Main.class.getResourceAsStream(LOGO_FILENAME)) {
            if (inputStream != null) {
                return Optional.of(new Image(inputStream));
            }
            throw new IOException("Logo file not found");
        } catch (IOException e) {
            LOG.error("Could not load logo file '" + LOGO_FILENAME + "'", e);
        }
        return Optional.empty();
    }
}
