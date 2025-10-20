package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GruppiDTO
 *
 * @author CSI PIEMONTE
 */

public class GruppiSoggettoDTO {
	
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
    
	@JsonProperty("flg_capo_gruppo")
    private int flgCapoGruppo;
    

    public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public int getFlgCapoGruppo() {
		return flgCapoGruppo;
	}

	public void setFlgCapoGruppo(int flgCapoGruppo) {
		this.flgCapoGruppo = flgCapoGruppo;
	}

}
