package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.ErrorMessageDto;
import com.popug.stoyalova.accounts.model.ErrorMessage;
import com.popug.stoyalova.accounts.repository.ErrorMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ErrorMessageService implements IErrorMessageService{

    private final ErrorMessageRepository repository;

    @Override
    public void save(ErrorMessageDto dto) {
        repository.save(ErrorMessage.builder()
                .description(dto.getDescription())
                .eventName(dto.getEventName())
                .message(dto.getMessage())
                .publicId(dto.getPublicId())
                .topic(dto.getTopic())
                .addTime(new Date())
                .build());
    }
}
