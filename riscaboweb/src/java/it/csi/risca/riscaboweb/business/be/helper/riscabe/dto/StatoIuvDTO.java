package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoIuvDTO {

	@JsonProperty("id_stato_iuv")
	private Long idStatoIuv;

	@JsonProperty("cod_stato_iuv")
	private String codStatoIuv;

	@JsonProperty("des_stato_iuv")
	private String desStatoIuv;

	public Long getIdStatoIuv() {
		return idStatoIuv;
	}

	public void setIdStatoIuv(Long idStatoIuv) {
		this.idStatoIuv = idStatoIuv;
	}

	public String getCodStatoIuv() {
		return codStatoIuv;
	}

	public void setCodStatoIuv(String codStatoIuv) {
		this.codStatoIuv = codStatoIuv;
	}

	public String getDesStatoIuv() {
		return desStatoIuv;
	}

	public void setDesStatoIuv(String desStatoIuv) {
		this.desStatoIuv = desStatoIuv;
	}


}
