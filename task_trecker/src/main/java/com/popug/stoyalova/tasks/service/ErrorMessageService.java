package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.ErrorMessageDto;
import com.popug.stoyalova.tasks.model.ErrorMessage;
import com.popug.stoyalova.tasks.repository.ErrorMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErrorMessageService implements IErrorMessageService{

    private final ErrorMessageRepository repository;

    @Override
    public List<ErrorMessageDto> getAllNotSend() {
        return repository.findAllBySendTimeIsNull().stream().map(this::entityMapper).collect(Collectors.toList());
    }

    private ErrorMessageDto entityMapper(ErrorMessage entity){
        return ErrorMessageDto.builder()
                .description(entity.getDescription())
                .eventName(entity.getEventName())
                .message(entity.getMessage())
                .publicId(entity.getPublicId())
                .topic(entity.getTopic())
                .build();
    }

    @Override
    public void markAsSend(String publicId) {
        Optional<ErrorMessage> errorMessage = repository.findByPublicId(publicId);
         if(errorMessage.isPresent()){
             ErrorMessage errorMessageSending = errorMessage.get();
             errorMessageSending.setSendTime( new Date());
             repository.save(errorMessageSending);
         }
    }

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
