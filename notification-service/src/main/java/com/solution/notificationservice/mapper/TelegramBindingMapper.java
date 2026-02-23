package com.solution.notificationservice.mapper;

import com.solution.notificationservice.dto.TelegramChatResponse;
import com.solution.notificationservice.entity.TelegramBinding;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TelegramBindingMapper {
    TelegramChatResponse toResponse(TelegramBinding entity);

    List<TelegramChatResponse> toResponseList(List<TelegramBinding> entities);
}
