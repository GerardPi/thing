package io.github.gerardpi.thing;

import static java.util.Objects.requireNonNull;

public final class Try<SV> extends Result<SV, Throwable> {
    Try(SV successValue, Throwable throwable) {
        super(successValue, throwable);
    }

    public static <SV> Try<SV> success(SV successValue) {
        return new Try<>(requireNonNull(successValue), null);
    }

    public static <SV> Try<SV> failure(Throwable failureValue) {
        return new Try<>(null, requireNonNull(failureValue));
    }
}

