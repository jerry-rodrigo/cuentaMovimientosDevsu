package com.example.cuentaMovimientos.exception;

/**
 * Excepción personalizada que se lanza cuando un recurso solicitado no se encuentra.
 *
 * <p>Esta excepción extiende `RuntimeException` y se utiliza para indicar que un recurso requerido
 * (como una cuenta o un movimiento) no ha sido encontrado en el repositorio.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que inicializa la excepción con un mensaje específico.
     *
     * @param message Mensaje que describe el error. Este mensaje es utilizado para proporcionar detalles
     *                sobre la razón por la cual se lanzó la excepción.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
