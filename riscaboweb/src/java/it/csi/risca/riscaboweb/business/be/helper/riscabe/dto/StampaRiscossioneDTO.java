package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.DatiTecniciAmbienteDTO;


public class StampaRiscossioneDTO {

	
	@JsonProperty("riscossione")
	private  RiscossioneDTO riscossioneDTO ;
	
	@JsonProperty("soggetto")
    private SoggettiExtendedDTO soggetto;
	
	@JsonProperty("provvedimento_istanza")
	private List<ProvvedimentoDTO> provvedimento;
	
	@JsonProperty("recapito")
	private RecapitiExtendedDTO recapito;
	
	
	@JsonProperty("tipo_riscossione")
    private TipoRiscossioneExtendedDTO tipoRiscossioneExtendedDTO ;
	
	@JsonProperty("indirizzo_spedizione")
	private IndirizzoSpedizioneDTO indirizzoSpedizione;
	
	@JsonProperty("gruppo_soggetto")
    private GruppiDTO gruppoSoggetto;
	
	@JsonProperty("dati_tecnici")
	private DatiTecniciAmbienteDTO datiTecnici;
	
	@JsonProperty("stato_debitorio")
    private List<StatoDebitorioExtendedDTO> statoDebitorio;
	
	@JsonProperty("invio_speciale")
	private String invioSpeciale;
	
	@JsonProperty("is_gruppo")
	private String is_gruppo;
	
	@JsonProperty("componente_gruppo")
	private List<SoggettiExtendedDTO>  componentiGruppo;
	
	@JsonProperty("is_componenti_gruppo")
	private String isComponentiGruppo; 
	
	public String getIsComponentiGruppo() {
		return isComponentiGruppo;
	}

	public void setIsComponentiGruppo(String isComponentiGruppo) {
		this.isComponentiGruppo = isComponentiGruppo;
	}

	public List<StatoDebitorioExtendedDTO> getStatoDebitorio() {
		return statoDebitorio;
	}

	public void setStatoDebitorio(List<StatoDebitorioExtendedDTO> statoDebitorio) {
		this.statoDebitorio = statoDebitorio;
	}

	public RiscossioneDTO getRiscossioneDTO() {
		return riscossioneDTO;
	}

	public void setRiscossioneDTO(RiscossioneDTO riscossioneDTO) {
		this.riscossioneDTO = riscossioneDTO;
	}

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}

	public List<ProvvedimentoDTO> getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(List<ProvvedimentoDTO> provvedimento) {
		this.provvedimento = provvedimento;
	}



	public RecapitiExtendedDTO getRecapito() {
		return recapito;
	}

	public void setRecapito(RecapitiExtendedDTO recapito) {
		this.recapito = recapito;
	}

	public TipoRiscossioneExtendedDTO getTipoRiscossioneExtendedDTO() {
		return tipoRiscossioneExtendedDTO;
	}

	public void setTipoRiscossioneExtendedDTO(TipoRiscossioneExtendedDTO tipoRiscossioneExtendedDTO) {
		this.tipoRiscossioneExtendedDTO = tipoRiscossioneExtendedDTO;
	}

	public IndirizzoSpedizioneDTO getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(IndirizzoSpedizioneDTO indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public GruppiDTO getGruppoSoggetto() {
		return gruppoSoggetto;
	}

	public void setGruppoSoggetto(GruppiDTO gruppoSoggetto) {
		this.gruppoSoggetto = gruppoSoggetto;
	}

	public DatiTecniciAmbienteDTO getDatiTecnici() {
		return datiTecnici;
	}

	public void setDatiTecnici(DatiTecniciAmbienteDTO datiTecnici) {
		this.datiTecnici = datiTecnici;
	}

	public String getInvioSpeciale() {
		return invioSpeciale;
	}

	public void setInvioSpeciale(String invioSpeciale) {
		this.invioSpeciale = invioSpeciale;
	}

	public String getIs_gruppo() {
		return is_gruppo;
	}

	public void setIs_gruppo(String is_gruppo) {
		this.is_gruppo = is_gruppo;
	}

	public List<SoggettiExtendedDTO> getComponentiGruppo() {
		return componentiGruppo;
	}

	public void setComponentiGruppo(List<SoggettiExtendedDTO> componentiGruppo) {
		this.componentiGruppo = componentiGruppo;
	}



	
	
	
}
