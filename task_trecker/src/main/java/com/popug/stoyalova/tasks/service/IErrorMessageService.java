package com.popug.stoyalova.tasks.service;


import com.popug.stoyalova.tasks.dto.ErrorMessageDto;

import java.util.List;

public interface IErrorMessageService {

    void save(ErrorMessageDto dto);

    List<ErrorMessageDto> getAllNotSend();

    void markAsSend(String publicId);
}
