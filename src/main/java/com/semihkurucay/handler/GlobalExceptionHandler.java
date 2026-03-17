package com.semihkurucay.handler;

import com.semihkurucay.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler <T> {

    private List<String> listAddToValue(String value, List<String> list) {
        list.add(value);
        return list;
    }

    private Map<String, List<String>> getErrorMessage(BindingResult bindingResult) {
        Map<String, List<String>> errors = new HashMap<>();

        for (ObjectError error : bindingResult.getAllErrors()) {
            String field = ((FieldError)error).getField();

            if(errors.containsKey(field)){
                errors.put(field, listAddToValue(error.getDefaultMessage(), errors.get(field)));
            }else{
                errors.put(field, listAddToValue(error.getDefaultMessage(), new ArrayList<String>()));
            }
        }

        return errors;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<?>> handleException(MethodArgumentNotValidException e, WebRequest request) {
        return ResponseEntity.badRequest().body(createApiError(getErrorMessage(e.getBindingResult()), request));
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ApiError<?>> handleException(BaseException e, WebRequest request) {
        return ResponseEntity.badRequest().body(createApiError(e.getMessage(), request));
    }

    private <T> ApiError<?> createApiError(T errorMessage, WebRequest request) {
        ApiError<T> apiError = new ApiError<>();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        Exception exception = new Exception();
        exception.setDateTime(LocalDateTime.now());
        exception.setExceptionMessage(errorMessage);
        exception.setPath(request.getDescription(false).substring(5));

        apiError.setException(exception);
        return apiError;
    }
}
