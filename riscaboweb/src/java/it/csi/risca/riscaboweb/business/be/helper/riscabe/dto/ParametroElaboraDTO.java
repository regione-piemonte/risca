package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParametroElaboraDTO {
	
	@JsonProperty("id_parametro_elabora")
	private Long idParametroElabora;

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("raggruppamento")
	private String raggruppamento = null;

	@JsonProperty("chiave")
	private String chiave = null;

	@JsonProperty("valore")
	private String valore = null;

	public Long getIdParametroElabora() {
		return idParametroElabora;
	}

	public void setIdParametroElabora(Long idParametroElabora) {
		this.idParametroElabora = idParametroElabora;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getRaggruppamento() {
		return raggruppamento;
	}

	public void setRaggruppamento(String raggruppamento) {
		this.raggruppamento = raggruppamento;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}
