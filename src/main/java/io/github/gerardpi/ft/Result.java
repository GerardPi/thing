package io.github.gerardpi.ft;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Result<SV, IV> {
    private final SV successValue;
    private final IV infoValue;

    protected Result(SV successValue, IV infoValue) {
        this.successValue = successValue;
        this.infoValue = infoValue;
    }

    public static <SV, IV> Result<SV, IV> ready(SV successValue, IV infoValue) {
        Preconditions.checkArgument(successValue != null || infoValue != null,
                "Both the success value and the info value are null. At least one should hold a value.");
        return new Result<>(successValue, infoValue);
    }

    public static <SV, IV> Result<SV, IV> ready(SV successValue) {
        return new Result<>(requireNonNull(successValue), null);
    }

    public static <SV, IV> Result<SV, IV> of(IV infoValue) {
        return new Result<>(null, requireNonNull(infoValue));
    }

    public SV successValue() {
        return requireNonNull(successValue);
    }

    public IV getInfoValue() {
        return requireNonNull(infoValue);
    }

    public String getInfoValueMessage(String messagePrefix, String defaultMessage) {
        if (infoValue instanceof Throwable) {
            return messagePrefix + ((Throwable) infoValue).getMessage();
        }
        return defaultMessage;
    }

    public IV getInfoValue(IV defaultValue) {
        return infoValue == null ? defaultValue : infoValue;
    }

    public boolean successful() {
        return successValue != null;
    }

    public boolean of() {
        return successValue == null;
    }

    public boolean hasInfoValue() {
        return infoValue != null;
    }

    public <X extends Throwable> SV orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (successValue != null) {
            return successValue;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> SV orElseThrow(Function<IV, ? extends X> exceptionTransformer) throws X {
        if (successValue != null) {
            return successValue;
        } else {
            throw exceptionTransformer.apply(infoValue);
        }
    }

    public SV orElse(SV other) {
        return successValue != null ? successValue : other;
    }

    public SV orElseGet(Supplier<? extends SV> other) {
        return successValue != null ? successValue : other.get();
    }
    public void ifPresentOrElse(Consumer<SV> svConsumer, Consumer<IV> ivConsumer) {
        if (successValue != null) {
            svConsumer.accept(successValue);
        } else if (infoValue != null) {
            ivConsumer.accept(infoValue);
        }
    }

    public <U> Optional<U> map(Function<? super SV, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return successValue == null ? Optional.empty() : Optional.ofNullable(mapper.apply(successValue));
    }
}

