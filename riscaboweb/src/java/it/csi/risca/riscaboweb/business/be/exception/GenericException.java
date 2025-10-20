package it.csi.risca.riscaboweb.business.be.exception;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;

public class GenericException extends Exception {

    private static final long serialVersionUID = 1L;

    private ErrorDTO error;
    private ErrorObjectDTO erroObjectDTO;
    
    public GenericException(String msg) {
        super(msg);
    }

    public GenericException(ErrorDTO errore) {
        this.setError(errore);
    }

    public GenericException(Throwable arg0) {
        super(arg0);
    }

    public ErrorDTO getError() {
        return error;
    }

    public void setError(ErrorDTO error) {
        this.error = error;
    }
    /**
     * Instantiates a new Generic exception.
     *
     * @param erroObjectDTO ErrorObjectDTO
     */
    public GenericException(ErrorObjectDTO erroObjectDTO) {
		super();
		this.erroObjectDTO = erroObjectDTO;
	}
    
    public ErrorObjectDTO getErroObjectDTO() {
		return erroObjectDTO;
	}

	public void setErroObjectDTO(ErrorObjectDTO erroObjectDTO) {
		this.erroObjectDTO = erroObjectDTO;
	}
}