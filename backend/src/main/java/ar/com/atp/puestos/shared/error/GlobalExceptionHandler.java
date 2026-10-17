package ar.com.atp.puestos.shared.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de la API al cuerpo uniforme {@link ApiError} (R-023, R-031, R-032).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente → 404. */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException ex) {
        return new ApiError(ex.getCode(), ex.getMessage());
    }

    /** Conflicto (PK duplicada) → 409. */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(ConflictException ex) {
        return new ApiError(ex.getCode(), ex.getMessage());
    }

    /** Violaciones de {@code @Valid} en el body → 400. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String detail = fieldError != null
                ? fieldError.getField() + ": " + fieldError.getDefaultMessage()
                : "cuerpo de la petición inválido";
        return new ApiError("VALIDACION", detail);
    }

    /** Body ilegible / tipos incompatibles → 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUnreadable(HttpMessageNotReadableException ex) {
        return new ApiError("CUERPO_INVALIDO", "El cuerpo de la petición no es válido.");
    }

    /** Parámetro obligatorio ausente → 400. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParam(MissingServletRequestParameterException ex) {
        return new ApiError("PARAMETRO_FALTANTE",
                "Falta el parámetro obligatorio '" + ex.getParameterName() + "'.");
    }

    /** Violaciones de constraints en parámetros → 400. */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraint(ConstraintViolationException ex) {
        return new ApiError("VALIDACION", ex.getMessage());
    }

    /** Restricciones de integridad de la BD (p. ej. PK duplicada) → 409. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrity(DataIntegrityViolationException ex) {
        return new ApiError("CONFLICTO_DATOS", "La operación viola una restricción de integridad.");
    }

    /** Fallback → 500 con el formato uniforme. */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneric(Exception ex) {
        return new ApiError("ERROR_INTERNO", "Ocurrió un error interno no esperado.");
    }
}
