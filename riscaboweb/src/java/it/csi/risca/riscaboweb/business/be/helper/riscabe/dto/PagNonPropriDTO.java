package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagNonPropriDTO {
	
	@JsonProperty("id_pag_non_propri")
    private Long idPagNonPropri;
	
	@JsonProperty("id_pagamento")
    private Long idPagamento;
	
	@JsonProperty("id_tipo_imp_non_propri")
    private Long idTipoImpNonPropri;

    @JsonProperty("importo_versato")
    private BigDecimal importoVersato;
    
	@JsonProperty("des_tipo_imp_non_propri")
    private String desTipoImpNonPropri;
    

    public Long getIdPagNonPropri() {
		return idPagNonPropri;
	}

	public void setIdPagNonPropri(Long idPagNonPropri) {
		this.idPagNonPropri = idPagNonPropri;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdTipoImpNonPropri() {
		return idTipoImpNonPropri;
	}

	public void setIdTipoImpNonPropri(Long idTipoImpNonPropri) {
		this.idTipoImpNonPropri = idTipoImpNonPropri;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public String getDesTipoImpNonPropri() {
		return desTipoImpNonPropri;
	}

	public void setDesTipoImpNonPropri(String desTipoImpNonPropri) {
		this.desTipoImpNonPropri = desTipoImpNonPropri;
	}

}
