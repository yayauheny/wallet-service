package io.ylab.walletservice.core.dto.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ylab.walletservice.core.domain.PlayerRole;

import java.time.LocalDate;

public record PlayerResponse(Long id,
                             PlayerRole role,
                             String username,
                             @JsonProperty("birth_date")
                             LocalDate birthDate) {

}
