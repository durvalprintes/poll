package dev.printes.poll.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    TIE("EMPATE"),
    NO_VOTING("SEM VOTOS");

    private final String value;
}
