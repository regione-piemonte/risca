package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * LockRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class LockRiscossioneDTO {
	
	@JsonProperty("id_riscossione")
    private Long idRiscossione;
    
    @JsonProperty("utente_lock")
    private String utenteLock;
    
 	@JsonProperty("cf_utente_lock")
 	private String cfUtenteLock = null;


	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public String getUtenteLock() {
		return utenteLock;
	}

	public void setUtenteLock(String utenteLock) {
		this.utenteLock = utenteLock;
	}

	public String getCfUtenteLock() {
		return cfUtenteLock;
	}

	public void setCfUtenteLock(String cfUtenteLock) {
		this.cfUtenteLock = cfUtenteLock;
	}
	
	
	
}
