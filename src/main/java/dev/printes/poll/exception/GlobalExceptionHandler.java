package dev.printes.poll.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        return ResponseEntity.badRequest().body(createErrorResponse(errors));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Map<String, List<String>>> handleDataIntegrityViolation(Exception ex) {
        String message = "Violação de integridade: já existe um registro com esses dados.";

        Pattern pattern = Pattern.compile("\\((.*?)\\)=\\((.*?)\\)");
        Matcher matcher = pattern.matcher(ex.getMessage());

        if (matcher.find()) {
            String value = matcher.group(2);
            message = String.format("Já existe um registro com o valor '%s'.", value);
        }

        List<String> errors = Collections.singletonList(message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(createErrorResponse(errors));
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Map<String, List<String>>> handlePollExceptions(ValidationException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(createErrorResponse(errors));
    }

    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<Map<String, List<String>>> handlePollExceptions(ConflictException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(createErrorResponse(errors));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.internalServerError().body(createErrorResponse(errors));
    }

    private Map<String, List<String>> createErrorResponse(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
