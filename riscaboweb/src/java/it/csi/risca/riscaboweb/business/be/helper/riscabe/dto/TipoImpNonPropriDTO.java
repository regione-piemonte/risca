package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoImpNonPropriDTO {
	
	@JsonProperty("id_tipo_imp_non_propri")
    private Long idTipoImpNonPropri;

    @JsonProperty("cod_tipo_imp_non_propri")
    private String codTipoImpNonPropri;

    @JsonProperty("des_tipo_imp_non_propri")
    private String desTipoImpNonPropri;
	
	public Long getIdTipoImpNonPropri() {
		return idTipoImpNonPropri;
	}

	public void setIdTipoImpNonPropri(Long idTipoImpNonPropri) {
		this.idTipoImpNonPropri = idTipoImpNonPropri;
	}

	public String getCodTipoImpNonPropri() {
		return codTipoImpNonPropri;
	}

	public void setCodTipoImpNonPropri(String codTipoImpNonPropri) {
		this.codTipoImpNonPropri = codTipoImpNonPropri;
	}

	public String getDesTipoImpNonPropri() {
		return desTipoImpNonPropri;
	}

	public void setDesTipoImpNonPropri(String desTipoImpNonPropri) {
		this.desTipoImpNonPropri = desTipoImpNonPropri;
	}	
	
}
