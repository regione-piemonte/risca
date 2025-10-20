package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * RicercaPagamentoDTO
 *
 * @author CSI PIEMONTE
 */
public class RicercaPagamentoDTO {

	@JsonProperty("stato_pagamento")
	private TipoRicercaPagamentoDTO statoPagamento;

	@JsonProperty("modalita_pagamento")
	private TipoModalitaPagDTO tipoModalitaPag;

	@JsonProperty("data_op_da")
	private String dataOpDa;

	@JsonProperty("data_op_a")
	private String dataOpA;

	@JsonProperty("quinto_campo")
	private String quintoCampo;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	@JsonProperty("cro")
	private String cro;

	@JsonProperty("importo_da")
	private BigDecimal importoDa;
	@JsonProperty("importo_a")
	private BigDecimal importoA;

	@JsonProperty("soggetto_versamento")
	private String soggettoVersamento;

	public TipoRicercaPagamentoDTO getStatoPagamento() {
		return statoPagamento;
	}

	public void setStatoPagamento(TipoRicercaPagamentoDTO statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public TipoModalitaPagDTO getTipoModalitaPag() {
		return tipoModalitaPag;
	}

	public void setTipoModalitaPag(TipoModalitaPagDTO tipoModalitaPag) {
		this.tipoModalitaPag = tipoModalitaPag;
	}

	public String getDataOpDa() {
		return dataOpDa;
	}

	public void setDataOpDa(String dataOpDa) {
		this.dataOpDa = dataOpDa;
	}

	public String getDataOpA() {
		return dataOpA;
	}

	public void setDataOpA(String dataOpA) {
		this.dataOpA = dataOpA;
	}

	public String getQuintoCampo() {
		return quintoCampo;
	}

	public void setQuintoCampo(String quintoCampo) {
		this.quintoCampo = quintoCampo;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public BigDecimal getImportoDa() {
		return importoDa;
	}

	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	public BigDecimal getImportoA() {
		return importoA;
	}

	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}

	public String getSoggettoVersamento() {
		return soggettoVersamento;
	}

	public void setSoggettoVersamento(String soggettoVersamento) {
		this.soggettoVersamento = soggettoVersamento;
	}

}
