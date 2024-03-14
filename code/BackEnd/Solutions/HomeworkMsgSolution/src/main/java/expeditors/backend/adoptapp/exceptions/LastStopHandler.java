package expeditors.backend.adoptapp.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Some of these methods just declared here as @ExceptionHandlers.
 * 
 * Others are @Overrides of the ResponseEntityExceptionHandler to override
 * specific errors.
 *
 *
 * @author whynot
 *
 */
@RestControllerAdvice
public class LastStopHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<?> accessDenied(AccessDeniedException ex, WebRequest request) {
        String rr = "LSH AccessDenied: " + ex;
        return ResponseEntity.status(403).body(rr);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<?> authException(AuthenticationException ex, WebRequest request) {
        String rr = ("LSH AuthenticationException: " + ex);
        return ResponseEntity.status(401).body(rr);
    }


    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /**
     * Used to deal with requests which do not pass the @Valid test.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        BindingResult br = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        for (FieldError e : br.getFieldErrors()) {
            errors.add(e.getField() + ": " + e.getDefaultMessage() + ", rejected value is " + e.getRejectedValue());
        }


        return ResponseEntity.badRequest().body(errors);

    }

    /**
     * Try sending an Accept of application/json, and change the controller
     * to only produce xml.  This will lead to a MediaTypeNotAcceptable situation,
     * and Spring will call this function if we have it set up.  This allows us to
     * customize the response we want to send back.
     * <p>
     * Note however, that we have to have a way to convert the RestResult to the
     * MediaType that the client wants.  Right now we can only convert to XML
     * or Json, so if the client wants a representation
     * that is not XML or Json, this method will fail miserably because it will not be
     * able to convert the RestResult.  It will get called twice, and then the client
     * will get an empty 406.
     */
    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add("Unsupported Media Type - " + request.getHeader(HttpHeaders.ACCEPT));
        errors.add("Supported Types are: ");
        List<MediaType> supportedTypes = ex.getSupportedMediaTypes();
        for (MediaType mt : supportedTypes) {
            errors.add(mt.toString());
        }

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errors);
    }

    //Catch all for anything not handled above
    @ExceptionHandler(value = {Throwable.class})
    protected String lastPortOfCall(Exception ex, WebRequest request) {
        logger.error("UnExpected Exception in LastStopHandler");
        ex.printStackTrace();
        String rr = ("Unexpected Exception: " + ex);
        return rr;
    }
}
