package io.ylab.walletservice.core.mapper;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.dto.account.AccountCreateDto;
import io.ylab.walletservice.core.dto.account.AccountResponse;
import io.ylab.walletservice.core.dto.account.AccountUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountUpdateDto toUpdateDto(Account account);
    Account fromUpdateDto(AccountUpdateDto dto);
    Account fromCreateDto(AccountCreateDto dto);

    Account fromResponse(AccountResponse account);

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);

    Account fromRequest(AccountCreateDto request);

    AccountCreateDto toRequest(Account account);
}
