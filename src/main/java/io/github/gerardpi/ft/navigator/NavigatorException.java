package io.github.gerardpi.ft.navigator;

public class NavigatorException extends RuntimeException {
    public static enum Operation {
        GET_ATTRIBUTES,
        GET_HIDDEN,
        READ_ALL_LINES,
        STREAM_LINES,
        READ_DIRECTORY_ENTRIES
    }
    public static enum Type {
        SECURITY_ERROR("A SecurityException occurred"),
        ENTRY_NOT_FOUND("Entry requested could not be found"),
        PATTERN_SYNTAX_INVALID("The pattern given is not valid"),
        IO_ERROR("An IOException occurred"),
        NO_READ_PERMISSION("No authorization to read"),
        NO_WRITE_PERMISSION("No authorization to write"),
        TOO_LARGE("The file is too large to be processed"),
        OPERATION_NOT_SUPPORTED("Can not perform this operation"),
        NOT_DIRECTORY("The entry was expected to be a directory but it is not."),
        NOT_HUMAN_READABLE("The file is not readable for humans");
        private final String message;

        private Type(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final Operation operation;
    private final Type type;
    private final String subject;

    public NavigatorException(Operation operation, Type type, String subject) {
        super(subject);
        this.operation = operation;
        this.type = type;
        this.subject = subject;
    }

    public NavigatorException(Operation operation, Type type, String subject, Throwable throwable) {
        super(subject, throwable);
        this.operation = operation;
        this.type = type;
        this.subject = subject;
    }

    public Type getType() {
        return type;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String getMessage() {
        return "Performing " + operation.name() + " on '" + subject + "' resulted in " + type.name() + ". " + type.getMessage() + ": " + super.getCause().getMessage();
    }
}
