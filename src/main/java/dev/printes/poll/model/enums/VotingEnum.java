package dev.printes.poll.model.enums;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotingEnum {
    YES("SIM"),
    NO("NAO");

    private final String value;

    public static boolean isOption(String option) {
        return Stream.of(VotingEnum.values()).anyMatch(votingOption -> votingOption.value.equals(option));
    }

    public static VotingEnum getOption(String option) {
        return Stream.of(VotingEnum.values()).filter(votingOption -> votingOption.value.equals(option)).findAny().orElseThrow();
    }
}
