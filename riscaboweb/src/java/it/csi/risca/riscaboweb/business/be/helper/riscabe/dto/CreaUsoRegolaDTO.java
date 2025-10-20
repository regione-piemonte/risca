package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

public class CreaUsoRegolaDTO {

	private Integer idAmbito;
	private Integer anno;
	private BigDecimal percentuale;
	
	public Integer getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public BigDecimal getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	} 
	
	
}
