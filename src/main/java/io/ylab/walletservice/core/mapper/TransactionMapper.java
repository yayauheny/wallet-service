package io.ylab.walletservice.core.mapper;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.transaction.TransactionResponse;
import io.ylab.walletservice.core.dto.transaction.TransactionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionResponse toResponse(Transaction transaction);

    List<TransactionResponse> toResponseList(List<Transaction> transaction);

    Transaction fromRequest(TransactionRequest request);
}
