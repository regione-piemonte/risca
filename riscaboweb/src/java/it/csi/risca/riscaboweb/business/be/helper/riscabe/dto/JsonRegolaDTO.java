package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRegolaDTO {
	
	@JsonProperty("canone_unitario")
	private BigDecimal canoneUnitario;

	@JsonProperty("canone_percentuale")
	private BigDecimal canonePercentuale;

	@JsonProperty("canone_minimo")
	private BigDecimal canoneMinimo;
	
	@JsonProperty("anno_riferimento")
	private Integer annoRiferimento;

	@JsonProperty("ranges")
	private List<JsonRangeDTO> jsonRanges;

	@JsonProperty("soglia")
	private BigDecimal soglia;
	
	@JsonProperty("canone_minimo_soglia_inf")
	private BigDecimal canoneMinimoSogliaInf;

	@JsonProperty("canone_minimo_soglia_sup")
	private BigDecimal canoneMinimoSogliaSup;

	@JsonProperty("minimo_principale")
	private BigDecimal minimoPrincipale;

	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}

	public BigDecimal getCanonePercentuale() {
		return canonePercentuale;
	}

	public void setCanonePercentuale(BigDecimal canonePercentuale) {
		this.canonePercentuale = canonePercentuale;
	}

	public BigDecimal getCanoneMinimo() {
		return canoneMinimo;
	}

	public void setCanoneMinimo(BigDecimal canoneMinimo) {
		this.canoneMinimo = canoneMinimo;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public List<JsonRangeDTO> getJsonRanges() {
		return jsonRanges;
	}

	public void setJsonRanges(List<JsonRangeDTO> jsonRanges) {
		this.jsonRanges = jsonRanges;
	}

	public BigDecimal getSoglia() {
		return soglia;
	}

	public void setSoglia(BigDecimal soglia) {
		this.soglia = soglia;
	}

	public BigDecimal getCanoneMinimoSogliaInf() {
		return canoneMinimoSogliaInf;
	}

	public void setCanoneMinimoSogliaInf(BigDecimal canoneMinimoSogliaInf) {
		this.canoneMinimoSogliaInf = canoneMinimoSogliaInf;
	}

	public BigDecimal getCanoneMinimoSogliaSup() {
		return canoneMinimoSogliaSup;
	}

	public void setCanoneMinimoSogliaSup(BigDecimal canoneMinimoSogliaSup) {
		this.canoneMinimoSogliaSup = canoneMinimoSogliaSup;
	}

	public BigDecimal getMinimoPrincipale() {
		return minimoPrincipale;
	}

	public void setMinimoPrincipale(BigDecimal minimoPrincipale) {
		this.minimoPrincipale = minimoPrincipale;
	}


	
	
}
