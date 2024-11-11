package com.spartaordersystem.global.exception;

import static com.spartaordersystem.global.exception.ErrorCode.ILLEGAL_ARGUMENT_ERROR;

import com.spartaordersystem.global.dto.ExceptionResponse;
import com.spartaordersystem.global.dto.InvalidParameterResponse;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //@Valid 로 발생하는 에러
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidatedException(ConstraintViolationException e){
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        if(constraintViolations != null) {
            for(ConstraintViolation c : constraintViolations){
                String[] paths = c.getPropertyPath().toString().split("\\.");
                String path = paths.length > 0 ? paths[paths.length - 1] : "";
                message.append(path);
                message.append(" : ");
                message.append(c.getMessage());
                message.append(". ");
            }
        }
        return ResponseEntity.status(ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(
                ExceptionResponse.of(
                        ILLEGAL_ARGUMENT_ERROR.getHttpStatus(),
                        message.toString(),
                        null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse<List<InvalidParameterResponse>>> methodArgNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        List<InvalidParameterResponse> invalidParameterResponses = errors.stream()
                .map(InvalidParameterResponse::of)
                .toList();

        return ResponseEntity.status(ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(
                ExceptionResponse.of(
                        ILLEGAL_ARGUMENT_ERROR.getHttpStatus(),
                        ILLEGAL_ARGUMENT_ERROR.getMessage(),
                        invalidParameterResponses));
    }

    /**
     * [Exception] CustomException 반환 ErrorCode에 작성된 예외를 반환하는 경우 사용
     *
     * @param e CustomException
     * @return ResponseEntity<ExceptionResponse>
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<Void>> customExceptionHandler(CustomException e) {
        log.error("CustomException: " + e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
                ExceptionResponse.of(
                        e.getErrorCode().getHttpStatus(),
                        e.getErrorCode().getMessage(),
                        null
                )
        );
    }

    /**
     * [Exception] RuntimeException 반환
     *
     * @param e RuntimeException
     * @return ResponseEntity<ExceptionResponse>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse<Void>> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResponseEntity.internalServerError().body(
                ExceptionResponse.of(
                        ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                        null)
        );
    }
}
