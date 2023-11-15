package io.ylab.walletservice.core.dto.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ylab.walletservice.core.domain.PlayerRole;

import java.time.LocalDate;

public record PlayerCreateDto(String username,
                              PlayerRole role,
                              @JsonProperty("birth_date")
                              LocalDate birthDate,
                              String password) {

}
