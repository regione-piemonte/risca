package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsoDatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */

public class RiduzioneDTDTO {
	
	@JsonProperty("id_riduzione")
    private Long idRiduzione;
	
	@JsonProperty("motivazione")
    private String motivazione;
	
	@JsonProperty("perc_riduzione_motiv")
    private Long percentuale;
	
	public Long getIdRiduzione() {
		return idRiduzione;
	}

	public void setIdRiduzione(Long idRiduzione) {
		this.idRiduzione = idRiduzione;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public Long getPercentuale() {
		return percentuale;
	}

	public void setPercentuale(Long percentuale) {
		this.percentuale = percentuale;
	}

}
