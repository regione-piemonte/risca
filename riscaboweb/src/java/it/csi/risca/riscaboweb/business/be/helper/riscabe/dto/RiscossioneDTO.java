package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class RiscossioneDTO {

	@JsonProperty("id_riscossione")
    private Long idRiscossione;

	@JsonProperty("id_tipo_riscossione")
    private Long idTipoRiscossione;

	@JsonProperty("id_componente_dt")
    private Long idComponenteDt;
    
	@JsonIgnore
    private Long idStatoRiscossione;
	
	@JsonProperty("soggetto")
    private SoggettiExtendedDTO soggetto;
	
	@JsonIgnore
    private Long idSoggetto;
	
	@JsonIgnore
    private Long idGruppoSoggetto;
	
	@JsonIgnore
    private Long idTipoAutorizza;

	@JsonProperty("gruppo_soggetto")
    private GruppiDTO gruppoSoggetto;
	
	@JsonProperty("tipo_autorizza")
    private TipiAutorizzazioneExtendedDTO tipoAutorizza;

	@JsonProperty("cod_riscossione")
    private String codRiscossione;
    
    @JsonProperty("cod_riscossione_prov")
    private String codRiscossioneProv;
    
    @JsonProperty("cod_riscossione_prog")
    private String codRiscossioneProg;
    
	@JsonProperty("id_sigla_riscossione")
    private Long idSiglaRiscossione;
	
    @JsonProperty("cod_riscossione_lettera_prov")
    private String codRiscossioneLetteraProv;
    
    @JsonProperty("num_pratica")
    private String numPratica;
    
    @JsonProperty("flg_prenotata")
    private int flgPrenotata;
    
    @JsonProperty("motivo_prenotazione")
    private String motivoPrenotazione;
    
    @JsonProperty("note_riscossione")
    private String noteRiscossione;
    
	@JsonProperty("data_ini_concessione")
    private String dataIniConcessione;
	
	@JsonProperty("data_scad_concessione")
    private String dataScadConcessione;
	
	@JsonProperty("data_ini_sosp_canone")
    private String dataIniSospCanone;
	
	@JsonProperty("data_fine_sosp_canone")
    private String dataFineSospCanone;
	
	@JsonProperty("json_dt")
    private String jsonDt;
	
	@JsonProperty("ambito")
	private String ambito;
	
	@JsonProperty("provvedimentoIstanza")
	private List<ProvvedimentoDTO> provvedimento;

	@JsonProperty("riscossione_recapito")
	private List<RiscossioneRecapitoDTO> riscossioneRecapito;

	@JsonProperty("recapiti_riscossione")
	private List<RecapitiExtendedDTO> recapitiRiscossione;

	@JsonProperty("stato_riscossione")
	private StatiRiscossioneDTO statiRiscossione;
	
	@JsonProperty("data_inizio_titolarita")
    private String dataInizioTitolarita;
	
	@JsonProperty("data_rinuncia_revoca")
    private String dataRinunciaRevoca;
	
	
	
	public String getDataRinunciaRevoca() {
		return dataRinunciaRevoca;
	}

	public void setDataRinunciaRevoca(String dataRinunciaRevoca) {
		this.dataRinunciaRevoca = dataRinunciaRevoca;
	}

	public void setRecapitiRiscossione(List<RecapitiExtendedDTO> recapitiRiscossione) {
		this.recapitiRiscossione = recapitiRiscossione;
	}

	public String getDataInizioTitolarita() {
		return dataInizioTitolarita;
	}

	public void setDataInizioTitolarita(String dataInizioTitolarita) {
		this.dataInizioTitolarita = dataInizioTitolarita;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getIdTipoRiscossione() {
		return idTipoRiscossione;
	}

	public void setIdTipoRiscossione(Long idTipoRiscossione) {
		this.idTipoRiscossione = idTipoRiscossione;
	}

    public Long getIdComponenteDt() {
		return idComponenteDt;
	}

	public void setIdComponenteDt(Long idComponenteDt) {
		this.idComponenteDt = idComponenteDt;
	}

	public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}

	public GruppiDTO getGruppoSoggetto() {
		return gruppoSoggetto;
	}

	public void setGruppoSoggetto(GruppiDTO gruppoSoggetto) {
		this.gruppoSoggetto = gruppoSoggetto;
	}

	public TipiAutorizzazioneExtendedDTO getTipoAutorizza() {
		return tipoAutorizza;
	}

	public void setTipoAutorizza(TipiAutorizzazioneExtendedDTO tipoAutorizza) {
		this.tipoAutorizza = tipoAutorizza;
	}
	
	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public Long getIdTipoAutorizza() {
		return idTipoAutorizza;
	}

	public void setIdTipoAutorizza(Long idTipoAutorizza) {
		this.idTipoAutorizza = idTipoAutorizza;
	}

	public String getCodRiscossione() {
		return codRiscossione;
	}

	public void setCodRiscossione(String codRiscossione) {
		this.codRiscossione = codRiscossione;
	}

	public String getCodRiscossioneProv() {
		return codRiscossioneProv;
	}

	public void setCodRiscossioneProv(String codRiscossioneProv) {
		this.codRiscossioneProv = codRiscossioneProv;
	}

	public String getCodRiscossioneProg() {
		return codRiscossioneProg;
	}

	public void setCodRiscossioneProg(String codRiscossioneProg) {
		this.codRiscossioneProg = codRiscossioneProg;
	}

	public Long getIdSiglaRiscossione() {
		return idSiglaRiscossione;
	}

	public void setIdSiglaRiscossione(Long idSiglaRiscossione) {
		this.idSiglaRiscossione = idSiglaRiscossione;
	}

	public String getCodRiscossioneLetteraProv() {
		return codRiscossioneLetteraProv;
	}

	public void setCodRiscossioneLetteraProv(String codRiscossioneLetteraProv) {
		this.codRiscossioneLetteraProv = codRiscossioneLetteraProv;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}

	public int getFlgPrenotata() {
		return flgPrenotata;
	}

	public void setFlgPrenotata(int flgPrenotata) {
		this.flgPrenotata = flgPrenotata;
	}

	public String getMotivoPrenotazione() {
		return motivoPrenotazione;
	}

	public void setMotivoPrenotazione(String motivoPrenotazione) {
		this.motivoPrenotazione = motivoPrenotazione;
	}

	public String getNoteRiscossione() {
		return noteRiscossione;
	}

	public void setNoteRiscossione(String noteRiscossione) {
		this.noteRiscossione = noteRiscossione;
	}

	public String getDataIniConcessione() {
		return dataIniConcessione;
	}

	public void setDataIniConcessione(String dataIniConcessione) {
		this.dataIniConcessione = dataIniConcessione;
	}

	public String getDataScadConcessione() {
		return dataScadConcessione;
	}

	public void setDataScadConcessione(String dataScadConcessione) {
		this.dataScadConcessione = dataScadConcessione;
	}

	public String getDataIniSospCanone() {
		return dataIniSospCanone;
	}

	public void setDataIniSospCanone(String dataIniSospCanone) {
		this.dataIniSospCanone = dataIniSospCanone;
	}

	public String getDataFineSospCanone() {
		return dataFineSospCanone;
	}

	public void setDataFineSospCanone(String dataFineSospCanone) {
		this.dataFineSospCanone = dataFineSospCanone;
	}

	public String getJsonDt() {
		return jsonDt;
	}

	public void setJsonDt(String jsonDt) {
		this.jsonDt = jsonDt;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	
	public List<ProvvedimentoDTO> getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(List<ProvvedimentoDTO> provvedimento) {
		this.provvedimento = provvedimento;
	}

	public List<RiscossioneRecapitoDTO> getRiscossioneRecapito() {
		return riscossioneRecapito;
	}

	public void setRiscossioneRecapito(List<RiscossioneRecapitoDTO> riscossioneRecapito) {
		this.riscossioneRecapito = riscossioneRecapito;
	}
	
	public List<RecapitiExtendedDTO> getRecapitiRiscossione() {
		return recapitiRiscossione;
	}

	public void setRecapiti(List<RecapitiExtendedDTO> recapitiRiscossione) {
		this.recapitiRiscossione = recapitiRiscossione;
	}
	
	public StatiRiscossioneDTO getStatiRiscossione() {
		return statiRiscossione;
	}

	public void setStatiRiscossione(StatiRiscossioneDTO statiRiscossione) {
		this.statiRiscossione = statiRiscossione;
	}
}
