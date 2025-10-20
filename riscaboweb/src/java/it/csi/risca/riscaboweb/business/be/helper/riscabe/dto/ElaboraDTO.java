package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElaboraDTO {

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("ambito")
	private AmbitoDTO ambito;

	@JsonProperty("tipo_elabora")
	private TipoElaboraExtendedDTO tipoElabora;

	@JsonProperty("stato_elabora")
	private StatoElaborazioneDTO statoElabora;

	@JsonProperty("data_richiesta")
	private Date dataRichiesta;
	
	@JsonProperty("nome_file_generato")
	private String nomeFileGenerato = null;

	@JsonProperty("parametri")
	private List<ParametroElaboraDTO> parametri;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	public TipoElaboraExtendedDTO getTipoElabora() {
		return tipoElabora;
	}

	public void setTipoElabora(TipoElaboraExtendedDTO tipoElabora) {
		this.tipoElabora = tipoElabora;
	}

	public StatoElaborazioneDTO getStatoElabora() {
		return statoElabora;
	}

	public void setStatoElabora(StatoElaborazioneDTO statoElabora) {
		this.statoElabora = statoElabora;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}


	public String getNomeFileGenerato() {
		return nomeFileGenerato;
	}

	public void setNomeFileGenerato(String nomeFileGenerato) {
		this.nomeFileGenerato = nomeFileGenerato;
	}
	
	public List<ParametroElaboraDTO> getParametri() {
		return parametri;
	}

	public void setParametri(List<ParametroElaboraDTO> parametri) {
		this.parametri = parametri;
	}

}
