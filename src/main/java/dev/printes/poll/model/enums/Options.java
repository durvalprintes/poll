package dev.printes.poll.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Options {
    YES("SIM"), NO("NAO");

    private final String value;
}
