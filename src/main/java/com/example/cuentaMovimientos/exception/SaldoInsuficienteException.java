package com.example.cuentaMovimientos.exception;

/**
 * Excepción personalizada que se lanza cuando se intenta realizar una operación con un saldo insuficiente.
 *
 * <p>Esta excepción extiende `RuntimeException` y se utiliza para indicar que una operación (como un
 * retiro o transferencia) no puede ser realizada debido a que el saldo de la cuenta es menor al monto
 * requerido.</p>
 */
public class SaldoInsuficienteException extends RuntimeException {

    /**
     * Constructor que inicializa la excepción con un mensaje específico.
     *
     * @param message Mensaje que describe el error. Este mensaje es utilizado para proporcionar detalles
     *                sobre la razón por la cual se lanzó la excepción.
     */
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
