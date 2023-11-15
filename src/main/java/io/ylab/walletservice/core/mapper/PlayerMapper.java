package io.ylab.walletservice.core.mapper;

import io.ylab.walletservice.api.PasswordHasher;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerUpdateDto;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse toResponse(Player player);

    List<PlayerResponse> toResponseList(List<Player> players);

    @Mapping(target = "hashedPassword", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", source = "role")
    Player fromRequest(PlayerUpdateDto request);

    @Mapping(target = "hashedPassword", source = "password", qualifiedByName = "hashPassword")

    Player fromPlayerCreateDto(PlayerCreateDto request);

    @Named("hashPassword")
    default byte[] hashPassword(String password) {
        return PasswordHasher.hashPassword(password);
    }
}
