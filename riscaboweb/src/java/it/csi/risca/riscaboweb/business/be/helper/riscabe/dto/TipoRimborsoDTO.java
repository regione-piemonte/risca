package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoRimborsoDTO {
	
	@JsonProperty("id_tipo_rimborso")
	private Long idTipoRimborso;

	@JsonProperty("cod_tipo_rimborso")
	private String codTipoRimborso;

	@JsonProperty("des_tipo_rimborso")
	private String desTipoRimborso;

	public Long getIdTipoRimborso() {
		return idTipoRimborso;
	}

	public void setIdTipoRimborso(Long idTipoRimborso) {
		this.idTipoRimborso = idTipoRimborso;
	}

	public String getCodTipoRimborso() {
		return codTipoRimborso;
	}

	public void setCodTipoRimborso(String codTipoRimborso) {
		this.codTipoRimborso = codTipoRimborso;
	}

	public String getDesTipoRimborso() {
		return desTipoRimborso;
	}

	public void setDesTipoRimborso(String desTipoRimborso) {
		this.desTipoRimborso = desTipoRimborso;
	}
	
}
