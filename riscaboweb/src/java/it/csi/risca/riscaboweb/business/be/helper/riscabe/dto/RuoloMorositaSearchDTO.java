package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RuoloMorositaSearchDTO
 *
 * @author CSI PIEMONTE
 */
public class RuoloMorositaSearchDTO  {
	
	@JsonProperty("list_id_stato_debitorio")
    private List<Long> listIdStatoDebitorio;

}
