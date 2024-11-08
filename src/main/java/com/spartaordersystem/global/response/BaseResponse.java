package com.spartaordersystem.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.spartaordersystem.global.common.GlobalConst.TIME_ZONE_ID;
import static com.spartaordersystem.global.common.GlobalConst.TIME_ZONE_PATTERN;

@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private final String timeStamp = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID))
            .format(DateTimeFormatter.ofPattern(TIME_ZONE_PATTERN));
    private final String success;
    private final String message;
    private final Object data;

    public static BaseResponse toSuccessResponse(String message) {
        return BaseResponse.builder()
                .success("true")
                .message(message)
                .build();
    }

    public static BaseResponse toSuccessResponse(String message, Object data) {
        return BaseResponse.builder()
                .success("true")
                .message(message)
                .data(data)
                .build();
    }

    public static BaseResponse toErrorResponse(String errorMessage) {
        return BaseResponse.builder()
                .success("false")
                .message(errorMessage)
                .build();
    }
}
