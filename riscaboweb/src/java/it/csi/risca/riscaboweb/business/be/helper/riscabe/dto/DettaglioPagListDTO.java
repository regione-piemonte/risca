package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DettaglioPagListDTO
 *
 * @author CSI PIEMONTE
 */

public class DettaglioPagListDTO {

	
	@JsonProperty("pagamento")
    private PagamentoExtendedDTO pagamento;	
    
    @JsonProperty("rate_sd")
    private List<RataSdDTO> rateSd;

	public PagamentoExtendedDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoExtendedDTO pagamento) {
		this.pagamento = pagamento;
	}

	public List<RataSdDTO> getRateSd() {
		return rateSd;
	}

	public void setRateSd(List<RataSdDTO> rateSd) {
		this.rateSd = rateSd;
	}   
    
    
    
}
