package dev.printes.poll.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    TIE("EMPATE"),
    NO_VOTING("SEM VOTOS"),
    WAITING("AGUARDANDO CALCULO");

    private final String value;
}
