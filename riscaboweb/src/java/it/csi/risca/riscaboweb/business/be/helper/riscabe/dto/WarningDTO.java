package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;
/**
 * The type Warning DTO .
 *
 * @author CSI PIEMONTE
 */
public class WarningDTO {
	
    private String code;

    private String message;

    
	public WarningDTO(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
