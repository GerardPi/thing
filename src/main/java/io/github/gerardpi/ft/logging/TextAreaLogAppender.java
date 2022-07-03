package io.github.gerardpi.ft.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.WarnStatus;
import javafx.scene.control.TextArea;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;


public class TextAreaLogAppender<E> extends OutputStreamAppender<E> {
    private static final String TARGET_NAME_STDOUT = "System.out";
    private static final String TARGET_NAME_STDERR = "System.err";
    public static final String LOG_PATTERN_FIXED_LEVEL_DATE_MSG_THREAD = "%.-1level|%date{yyyyMMddHHmmssSSS}|%-160.160msg{160}|%-20.20logger{20}%n";
    private final TextAreaLogTarget stdoutTarget;
    private final TextAreaLogTarget stderrTarget;
    private OutputStream target;

    public TextAreaLogAppender(TextArea textArea) {
        this.stdoutTarget = new TextAreaLogTarget(textArea);
        this.stderrTarget = new TextAreaLogTarget(textArea);
        this.target = stdoutTarget;
    }


    /**
     * Sets the value of the <b>Target</b> option. Recognized values are
     * "System.out" and "System.err". Any other value will be ignored.
     */
    public void setTarget(String value) {
        if (TARGET_NAME_STDOUT.equals(value)) {
            target = stdoutTarget;
        } else if (TARGET_NAME_STDERR.equals(value)) {
            target = stderrTarget;
        } else {
            targetWarn(value);
        }
    }

    private void targetWarn(String val) {
        Status status = new WarnStatus("[" + val + "] should be either " +
                TARGET_NAME_STDOUT + " or " + TARGET_NAME_STDERR, this);
        status.add(new WarnStatus("Using previously set target, " + TARGET_NAME_STDOUT +
                " by default.", this));
        addStatus(status);
    }

    @Override
    public void start() {
        setOutputStream(target);
        super.start();
    }

    @Override
    protected void writeOut(E event) throws IOException {
        super.writeOut(event);
    }

    public static void configureLogger(TextArea logTextArea, Class<?> contextClass) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

//        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        //ple.setPattern("%-4relative [%thread] %-5level %logger{35} - %msg %n");
//        ple.setPattern("%.-1level%date{yyyyMMddHHmmssSSS}[%thread{10}]%logger{10} - %msg %n");
        ple.setPattern(LOG_PATTERN_FIXED_LEVEL_DATE_MSG_THREAD);
        ple.setContext(lc);
        ple.start();
        TextAreaLogAppender<ILoggingEvent> textAreaAppender = new TextAreaLogAppender<>(logTextArea);
        textAreaAppender.setEncoder(ple);
        textAreaAppender.setContext(lc);
        textAreaAppender.start();

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)
                LoggerFactory.getLogger(contextClass.getPackage().getName());
        logger.addAppender(textAreaAppender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */
        logger.info("test");
    }
}
