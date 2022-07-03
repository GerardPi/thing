package io.github.gerardpi.ft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandController {
    private static final Logger LOG = LoggerFactory.getLogger(CommandController.class);
    private final Controller fnController;

    public CommandController(Controller fnController) {
        this.fnController = fnController;
    }

    public void execute(String input) {
        ViCommand.parse(input)
                .ifPresentOrElse(
                        match -> {
                            switch (match.getCommand()) {
                                case NEW_EDITOR -> fnController.addNewEditorPane();
                                case NEW_NAVIGATOR -> fnController.addNewFileNavigatorPane();
                                case QUIT_NOW -> System.exit(0);
                                case CLEAR_LOG -> fnController.clearLog();
                                case LOG_SHELL_RESULT -> fnController.executeAndLog(match.getExtracted());
                                case IMPORT_SHELL_RESULT -> fnController.executeAndImport(match.getExtracted());
                                case WRAP -> fnController.setWrap(!match.getExtracted(0).startsWith("no"));
                                default -> LOG.info("Don't know what to do with command match '{}'", match);
                            }
                        },
                        () -> LOG.info("Can not process '{}'", input));
        fnController.clearCommandInput();
    }
}
