package exercise.kadai10th.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class DuplicateCodeExceptionHandler {

    private final static Logger logger = Logger.getLogger(DuplicateCodeException.class.getName());

    @ExceptionHandler(DuplicateCodeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCodeException(
            DuplicateCodeException e, HttpServletRequest request) {

        logger.log(Level.INFO, "Exception was thrown due to Code Duplication, but properly handled", e);

        Map<String, String> body = new HashMap<>();
        body.put("timestamp", ZonedDateTime.now().toString());
        body.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        body.put("message", e.getMessage());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
