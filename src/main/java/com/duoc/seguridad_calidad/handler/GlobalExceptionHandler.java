package com.duoc.seguridad_calidad.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex) {
        // Puedes redirigir a una vista de error 404 personalizada
        // o simplemente devolver el estado HTTP y dejar que el contenedor lo maneje.
        // Aquí devolvemos una vista simple de error.
        return "error/404"; // Asegúrate de tener una plantilla error/404.html
    }
}
