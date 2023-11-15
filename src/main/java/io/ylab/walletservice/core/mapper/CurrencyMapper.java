package io.ylab.walletservice.core.mapper;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.dto.currency.CurrencyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency fromRequest(CurrencyRequest request);
}
