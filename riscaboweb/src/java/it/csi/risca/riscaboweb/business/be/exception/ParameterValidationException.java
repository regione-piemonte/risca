package it.csi.risca.riscaboweb.business.be.exception;

public class ParameterValidationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterValidationException(String message) {
        super(message);
    }

}
