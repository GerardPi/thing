package io.github.gerardpi.thing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum ViCommand {
    QUIT("q"),
    QUIT_NOW("q!"),
    CLEAR_LOG("clearlog"),
    NEW_EDITOR("enew"),
    NEW_NAVIGATOR("nnew"),
    LOG_SHELL_RESULT(Pattern.compile("!(.+)")),
    WRAP(Pattern.compile("(.*)wrap")),
    IMPORT_SHELL_RESULT(Pattern.compile("r!(.+)"));
    private static final Logger LOG = LoggerFactory.getLogger(ViCommand.class);
    private final String input;
    private final Pattern pattern;
    private static final List<String> NOTHING_EXTRACTED = Collections.emptyList();

    ViCommand(String input) {
        this.input = input;
        this.pattern = null;
    }

    ViCommand(Pattern pattern) {
        this.input = null;
        this.pattern = pattern;
    }

    public static Optional<ViCommandMatch> parse(String input) {
        return Stream.of(values())
                .map(command -> command.match(input))
                .filter(Objects::nonNull)
                .findAny();
    }

    private ViCommandMatch match(String input) {
        if (pattern != null) {
            var matcher = pattern.matcher(input);
            if (matcher.matches()) {
                return new ViCommandMatch(this, input, groupsToList(matcher));
            }
        } else {
            if (this.input.equals(input)) {
                return new ViCommandMatch(this, input);
            }
        }
        return null;
    }

    private static List<String> groupsToList(Matcher matcher) {
        // Only take extracted values, 0 = whole input
        return IntStream.rangeClosed(1, matcher.groupCount())
                .mapToObj(matcher::group)
                .collect(Collectors.toUnmodifiableList());
    }


    static class ViCommandMatch {

        private final String input;
        private final List<String> extracted;
        private ViCommand command;

        public ViCommandMatch(ViCommand command, String input, List<String> extracted) {
            this.command = command;
            this.input = input;
            this.extracted = extracted;
        }

        public ViCommandMatch(ViCommand command, String input) {
            this(command, input, NOTHING_EXTRACTED);
        }

        public List<String> getExtracted() {
            return extracted;
        }

        public String getExtracted(int index) {
            if (index < extracted.size()) {
                return extracted.get(index);
            }
            LOG.error("Extracted items '{}' has no index '{}' available.", extracted, index);
            return "";
        }

        public String getInput() {
            return input;
        }

        public ViCommand getCommand() {
            return command;
        }
    }
}
