package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;
/**
 * Stato Elaborazione DTO
 *
 * @author CSI PIEMONTE
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoElaborazioneDTO {
	
	
	
	@JsonProperty("id_stato_elabora")
    private Long idStatoElabora;

    @JsonProperty("cod_stato_elabora")
    private String codStatoElabora;
    
    @JsonProperty("des_stato_elabora")
    private String desStatoElabora;

	public Long getIdStatoElabora() {
		return idStatoElabora;
	}

	public void setIdStatoElabora(Long idStatoElabora) {
		this.idStatoElabora = idStatoElabora;
	}

	public String getCodStatoElabora() {
		return codStatoElabora;
	}

	public void setCodStatoElabora(String codStatoElabora) {
		this.codStatoElabora = codStatoElabora;
	}

	public String getDesStatoElabora() {
		return desStatoElabora;
	}

	public void setDesStatoElabora(String desStatoElabora) {
		this.desStatoElabora = desStatoElabora;
	}

}
