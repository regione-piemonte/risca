package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoTitoloDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiTitoloDTO {

	@JsonProperty("id_tipo_titolo")
    private Long idTipoTitolo;

    @JsonProperty("id_ambito")
    private Long idAmbito;

    @JsonProperty("cod_tipo_titolo")
    private String codTipoTitolo;
    
    @JsonProperty("des_tipo_titolo")
    private String desTipoTitolo;

    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;
    
    @JsonProperty("ordina_tipo_titolo")
    private Long ordinaTipoTitolo;
    
    @JsonProperty("flg_default")
    private int flgDefault;
    
    
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
    
	
	public Long getIdTipoTitolo() {
		return idTipoTitolo;
	}

	public void setIdTipoTitolo(Long idTipoTitolo) {
		this.idTipoTitolo = idTipoTitolo;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodTipoTitolo() {
		return codTipoTitolo;
	}

	public void setCodTipoTitolo(String codTipoTitolo) {
		this.codTipoTitolo = codTipoTitolo;
	}

	public String getDesTipoTitolo() {
		return desTipoTitolo;
	}

	public void setDesTipoTitolo(String desTipoTitolo) {
		this.desTipoTitolo = desTipoTitolo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codTipoTitolo, dataFineValidita, dataInizioValidita, desTipoTitolo, flgDefault, idAmbito,
				idTipoTitolo, ordinaTipoTitolo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipiTitoloDTO other = (TipiTitoloDTO) obj;
		return Objects.equals(codTipoTitolo, other.codTipoTitolo)
				&& Objects.equals(dataFineValidita, other.dataFineValidita)
				&& Objects.equals(dataInizioValidita, other.dataInizioValidita)
				&& Objects.equals(desTipoTitolo, other.desTipoTitolo) && flgDefault == other.flgDefault
				&& Objects.equals(idAmbito, other.idAmbito) && Objects.equals(idTipoTitolo, other.idTipoTitolo)
				&& Objects.equals(ordinaTipoTitolo, other.ordinaTipoTitolo);
	}

	@Override
	public String toString() {
		return "TipiTitoloDTO [idTipoTitolo=" + idTipoTitolo + ", idAmbito=" + idAmbito + ", codTipoTitolo="
				+ codTipoTitolo + ", desTipoTitolo=" + desTipoTitolo + ", dataFineValidita=" + dataFineValidita
				+ ", dataInizioValidita=" + dataInizioValidita + ", ordinaTipoTitolo=" + ordinaTipoTitolo
				+ ", flgDefault=" + flgDefault + "]";
	}
    
}
