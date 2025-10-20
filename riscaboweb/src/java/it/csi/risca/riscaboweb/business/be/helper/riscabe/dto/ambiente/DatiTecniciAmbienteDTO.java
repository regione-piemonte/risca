package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatiTecniciAmbienteDTO
 *
 * @author CSI PIEMONTE
 */

public class DatiTecniciAmbienteDTO {

	@JsonProperty("dati_generali")
	private DatiGeneraliDTO datiGenerali;
	
	@JsonProperty("usi")
    private Map<String, TipoUsoDatoTecnicoDTO> usi;

	public DatiGeneraliDTO getDatiGenerali() {
		return datiGenerali;
	}

	public void setDatiGenerali(DatiGeneraliDTO datiGenerali) {
		this.datiGenerali = datiGenerali;
	}

	public Map<String, TipoUsoDatoTecnicoDTO> getUsi() {
		return usi;
	}

	public void setUsi(Map<String, TipoUsoDatoTecnicoDTO> usi) {
		this.usi = usi;
	}

}
