package io.github.gerardpi.thing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandController {
    private static final Logger LOG = LoggerFactory.getLogger(CommandController.class);
    private final MainController fnMainController;

    public CommandController(MainController fnMainController) {
        this.fnMainController = fnMainController;
    }

    public void execute(String input) {
        ViCommand.parse(input)
                .ifPresentOrElse(
                        match -> {
                            switch (match.getCommand()) {
                                case NEW_EDITOR -> fnMainController.addNewEditorPane();
                                case NEW_NAVIGATOR -> fnMainController.addNewFileNavigatorPane();
                                case QUIT_NOW -> System.exit(0);
                                case CLEAR_LOG -> fnMainController.clearLog();
                                case LOG_SHELL_RESULT -> fnMainController.executeAndLog(match.getExtracted());
                                case IMPORT_SHELL_RESULT -> fnMainController.executeAndImport(match.getExtracted());
                                case WRAP -> fnMainController.setWrap(!match.getExtracted(0).startsWith("no"));
                                default -> LOG.info("Don't know what to do with command match '{}'", match);
                            }
                        },
                        () -> LOG.info("Can not process '{}'", input));
        fnMainController.clearCommandInput();
    }
}
