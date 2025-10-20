/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr1DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr3DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr7DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr1DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr3DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr7DAO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.soris.ElaborazioneSorisResultDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr7DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr7DTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component("elaborazioneSoris")
public class ElaborazioneSoris {


	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;


	@Autowired
	private SorisFr1DAO sorisFr1DAO;
	
	@Autowired
	private SorisFr3DAO sorisFr3DAO;
	
	@Autowired
	private RuoloSorisFr1DAO ruoloSorisFr1DAO;

	@Autowired
	private RuoloSorisFr3DAO ruoloSorisFr3DAO;

	@Autowired
	private RuoloSorisFr7DAO ruoloSorisFr7DAO;
	
	@Autowired
	private SorisFr7DAO sorisFr7DAO;
	
	// Passi 
	public static final String PASSO_ACQ_NUOVE_CARTELLE = "ACQ_NUOVE_CARTELLE";
	public static final String PASSO_ACQ_PAGAMENTI = "ACQ_PAGAMENTI";
	public static final String PASSO_ACQ_ANNULLAMENTI = "ACQ_ANNULLAMENTI";

	
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	private String fase;
	private String codFase;
	private String attore;


	public ElaborazioneSorisResultDTO elaboraRuoloSorisFr1(RuoloSorisFr1DTO ruoloFr1) throws DataAccessException, DAOException, SQLException {
		attore = "riscabatchpag";
		fase = "Acquisizione Riscossione Soris";
		codFase = "ACQ_RISCOSSIONI_SORIS";	
		boolean ret = false;
		int numNuoveCartelleLette = 0;
		int numNuoveCartelleAcquisite = 0;
		int numNuoveCartelleImportiKO = 0;
		
		try {
			List<SorisFr1DTO> listSorisFr1 = sorisFr1DAO.loadSorisFr1();
			
			
			if(listSorisFr1!= null && !listSorisFr1.isEmpty()) {
				for (int i = 0; i < listSorisFr1.size(); i++) {
					numNuoveCartelleLette++;
					
					RuoloSorisFr1DTO ruoloSorisFr1 = new RuoloSorisFr1DTO();
					
					setRuoloSorisFr1(ruoloFr1.getIdFileSoris(), listSorisFr1.get(i), ruoloSorisFr1);
					
					ruoloSorisFr1DAO.saveRuoloSorisFr1(ruoloSorisFr1);
					numNuoveCartelleAcquisite++;
					if(listSorisFr1.get(i).getIdRuolo() == null) {
						logStep(ruoloFr1.getIdElabora(), PASSO_ACQ_NUOVE_CARTELLE, 0,
								fase + " - Step 3 - WARNING  - Nuove Cartelle - Non trovato ruolo su RISCA - p_ident_prenot_ruolo:"
										+ listSorisFr1.get(i).getpIdentPrenotRuolo() + " - utenza: "
										+ listSorisFr1.get(i).getUtenza()+" - identif_cartella:  "+listSorisFr1.get(i).getIdentifCartella());
					}
				}
			}	
			//setto i log e warning se gli importi coincidono o no
			List<RuoloSorisFr1DTO> listRuoloSorisFr1 = ruoloSorisFr1DAO.loadConfrontoImporti(ruoloFr1.getIdFileSoris());
			if(listRuoloSorisFr1 != null && !listRuoloSorisFr1.isEmpty()) {
				for (int i = 0; i < listRuoloSorisFr1.size(); i++) {
					//VERIFICA SE IMPORTI COINCIDONO (SE NON COINCIDONO SEGNALARE IL WARNING)
					if(listRuoloSorisFr1.get(i).getFlgConfronto() == null || (Boolean.FALSE.equals(listRuoloSorisFr1.get(i).getFlgConfronto()))) {
						numNuoveCartelleImportiKO++;
						logStep(ruoloFr1.getIdElabora(), PASSO_ACQ_NUOVE_CARTELLE, 0,
								fase + " - Step 3 - WARNING - (FR1) Importi ruolo non corrispondenti - id_ruolo:"
										+ listSorisFr1.get(i).getIdRuolo() + " - p_ident_prenot_ruolo: "
										+ listSorisFr1.get(i).getpIdentPrenotRuolo()+" - id_file_soris:  "+ruoloFr1.getIdFileSoris());
					}else {
						logStep(ruoloFr1.getIdElabora(), PASSO_ACQ_NUOVE_CARTELLE, 0,
								fase + " - Step 3  - (FR1) Importi ruolo CORRISPONDENTI - id_ruolo:"
										+ listSorisFr1.get(i).getIdRuolo() + " - p_ident_prenot_ruolo: "
										+ listSorisFr1.get(i).getpIdentPrenotRuolo()+" - id_file_soris:  "+ruoloFr1.getIdFileSoris());
					}
				}
			}
			
			
			ret = true;
			logStep(ruoloFr1.getIdElabora(), PASSO_ACQ_NUOVE_CARTELLE, 0,
					fase + " - Step 3 - Acquisizione Riscossioni Soris - Fine Caricamento Nuove Cartelle - n. cartelle: "
							+ listSorisFr1.size());
		} catch (Exception e) {
			logStep(ruoloFr1.getIdElabora(), PASSO_ACQ_NUOVE_CARTELLE, 1,
					fase + " - Step 3 - Acquisizione Riscossioni Soris - Caricamento Nuove Cartelle - KO - " );
			e.printStackTrace();
		}
		
		ElaborazioneSorisResultDTO result = new ElaborazioneSorisResultDTO();
		result.setNumNuoveCartelleLette(numNuoveCartelleLette);
		result.setNumNuoveCartelleAcquisite(numNuoveCartelleAcquisite);
		result.setNumNuoveCartelleImportiKO(numNuoveCartelleImportiKO);
		result.setRet(ret);
		return result;


	}

	private void setRuoloSorisFr1(Long idFileSoris, SorisFr1DTO sorisFr1, 
			RuoloSorisFr1DTO ruoloSorisFr1) {
		
		ruoloSorisFr1.setIdFileSoris(idFileSoris);	
		ruoloSorisFr1.setTipoRecord("fr1");
		ruoloSorisFr1.setIdRuolo(sorisFr1.getIdRuolo());
		ruoloSorisFr1.setUtenza(sorisFr1.getUtenza());
		ruoloSorisFr1.setProgrRecord(sorisFr1.getProgrRecord());
		ruoloSorisFr1.setCodAmbito(sorisFr1.getCodAmbito());
		ruoloSorisFr1.setCodEnteCreditore(sorisFr1.getCodEnteCreditore());
		ruoloSorisFr1.setAnnoRuolo(sorisFr1.getAnnoRuolo());
		ruoloSorisFr1.setNumeroRuolo(sorisFr1.getNumeroRuolo());
		ruoloSorisFr1.setpTipoUfficio(sorisFr1.getpTipoUfficio());
		ruoloSorisFr1.setpCodiceUfficio(sorisFr1.getpCodiceUfficio());
		ruoloSorisFr1.setpTipoModello(sorisFr1.getpTipoModello());
		ruoloSorisFr1.setpIdentPrenotRuolo(sorisFr1.getpIdentPrenotRuolo());
		ruoloSorisFr1.setpIdentAtto(sorisFr1.getpIdentAtto());
		ruoloSorisFr1.setProgrArticoloRuolo(sorisFr1.getProgrArticoloRuolo());
		ruoloSorisFr1.setIdentifCartella(sorisFr1.getIdentifCartella());
		ruoloSorisFr1.setFlgRateizzazione(sorisFr1.getFlgRateizzazione());
		ruoloSorisFr1.setProgrArticoloCartella(sorisFr1.getProgrArticoloCartella());
		ruoloSorisFr1.setCodiceEntrata(sorisFr1.getCodiceEntrata());
		ruoloSorisFr1.setTipoEntrata(sorisFr1.getTipoEntrata());
		ruoloSorisFr1.setCodiceFiscale(sorisFr1.getCodiceFiscale());
		ruoloSorisFr1.setTipoEvento(sorisFr1.getTipoEvento());
		ruoloSorisFr1.setDataEvento(sorisFr1.getDataEvento());
		ruoloSorisFr1.setImportoCarico(sorisFr1.getImportoCarico());
		ruoloSorisFr1.setCodiceDivisa(sorisFr1.getCodiceDivisa());
		ruoloSorisFr1.setDataScadReg(sorisFr1.getDataScadReg());
		ruoloSorisFr1.setGestAttoreIns(attore);
		ruoloSorisFr1.setGestAttoreUpd(attore);
	}

	
	public boolean elaboraRuoloSorisFr3(RuoloSorisFr3DTO ruoloFr3) throws DataAccessException, DAOException, SQLException {
		attore = "riscabatchpag";
		fase = "Acquisizione Riscossione Soris";
		codFase = "ACQ_RISCOSSIONI_SORIS";	
		boolean ret = false;
		String pIdentPrenotRuolo = "";
		try {
			List<SorisFr3DTO> listSorisFr3 = sorisFr3DAO.loadSorisFr3();
			
			
			if(listSorisFr3!= null && !listSorisFr3.isEmpty()) {
				for (int i = 0; i < listSorisFr3.size(); i++) {
					RuoloSorisFr3DTO ruoloSorisFr3 = new RuoloSorisFr3DTO();
					
					setRuoloSorisFr3(ruoloFr3.getIdFileSoris(), listSorisFr3.get(i), ruoloSorisFr3);
					pIdentPrenotRuolo = listSorisFr3.get(i).getpIdentPrenotRuolo();
					ruoloSorisFr3 =ruoloSorisFr3DAO.saveRuoloSorisFr3(ruoloSorisFr3);
					
					if(listSorisFr3.get(i).getIdRuolo() == null) {
						logStep(ruoloFr3.getIdElabora(), PASSO_ACQ_PAGAMENTI, 0,
								fase + " - Step 3 - WARNING - Non trovato il ruolo per il pagamento - p_ident_prenot_ruolo:"
										+ listSorisFr3.get(i).getpIdentPrenotRuolo() );
					}	
					
					logStep(ruoloFr3.getIdElabora(), PASSO_ACQ_PAGAMENTI, 0,
							fase + " - Step 3 - Acquisizione Riscossioni Soris - Insert pagamento su risca_r_ruolo_soris_fr3 OK - Chiave: "
									+ ruoloSorisFr3.getIdRuoloSorisFr3() + " id ruolo: "+ruoloSorisFr3.getIdRuolo()+ " p ident prenot ruolo: "+ruoloSorisFr3.getpIdentPrenotRuolo());
				}
				
				
				
			}			
			
			//TODO inserisco la parte successiva
			ret = true;
			
		} catch (Exception e) {
			logStep(ruoloFr3.getIdElabora(), PASSO_ACQ_PAGAMENTI, 1,
					fase + " - Step 3 - Acquisizione Riscossioni Soris - Insert pagamento su risca_r_ruolo_soris_fr3 KO - p_ident_prenot_ruolo: "+pIdentPrenotRuolo );
			e.printStackTrace();
		}
		
		return ret;


	}

	
	private void setRuoloSorisFr3(Long idFileSoris, SorisFr3DTO sorisFr3, 
			RuoloSorisFr3DTO ruoloSorisFr3) {
		
		
		ruoloSorisFr3.setIdFileSoris(idFileSoris);	
		ruoloSorisFr3.setTipoRecord("fr3");
		ruoloSorisFr3.setIdRuolo(sorisFr3.getIdRuolo());
		ruoloSorisFr3.setIdPagamento(null);
		ruoloSorisFr3.setUtenza(sorisFr3.getUtenza());
		ruoloSorisFr3.setProgrRecord(sorisFr3.getProgrRecord());
		ruoloSorisFr3.setCodAmbito(sorisFr3.getCodAmbito());
		ruoloSorisFr3.setCodEnteCreditore(sorisFr3.getCodEnteCreditore());
		ruoloSorisFr3.setAnnoRuolo(sorisFr3.getAnnoRuolo());
		ruoloSorisFr3.setNumeroRuolo(sorisFr3.getNumeroRuolo());
		ruoloSorisFr3.setpTipoUfficio(sorisFr3.getpTipoUfficio());
		ruoloSorisFr3.setpCodiceUfficio(sorisFr3.getpCodiceUfficio());
		ruoloSorisFr3.setpAnnoRiferimento(sorisFr3.getpAnnoRiferimento());
		ruoloSorisFr3.setpTipoModello(sorisFr3.getpTipoModello());		
		ruoloSorisFr3.setpIdentPrenotRuolo(sorisFr3.getpIdentPrenotRuolo());
		
		ruoloSorisFr3.setpIdentAtto(sorisFr3.getpIdentAtto());
		ruoloSorisFr3.setProgrArticoloRuolo(sorisFr3.getProgrArticoloRuolo());
		ruoloSorisFr3.setIdentifCartella(sorisFr3.getIdentifCartella());		
		ruoloSorisFr3.setProgrArticoloCartella(sorisFr3.getProgrArticoloCartella());
		ruoloSorisFr3.setCodiceEntrata(sorisFr3.getCodiceEntrata());
		ruoloSorisFr3.setTipoEntrata(sorisFr3.getTipoEntrata());
		ruoloSorisFr3.setCodiceFiscale(sorisFr3.getCodiceFiscale());
		ruoloSorisFr3.setDataEvento(sorisFr3.getDataEvento());
		ruoloSorisFr3.setImportoCaricoRisco(sorisFr3.getImportoCaricoRisco());
		ruoloSorisFr3.setImportoInteressi(sorisFr3.getImportoInteressi());
		ruoloSorisFr3.setImportoAggioEnte(sorisFr3.getImportoAggioEnte());
		ruoloSorisFr3.setImportoAggioContrib(sorisFr3.getImportoAggioContrib());
		ruoloSorisFr3.settSpeseProcEsec(sorisFr3.gettSpeseProcEsec());
		ruoloSorisFr3.settSpeseProcEsecPLista(sorisFr3.gettSpeseProcEsecPLista());
		ruoloSorisFr3.setCodiceDivisa(sorisFr3.getCodiceDivisa());
		ruoloSorisFr3.setModalitaPagam(sorisFr3.getModalitaPagam());
		ruoloSorisFr3.setDataRegistr(sorisFr3.getDataRegist());
		ruoloSorisFr3.setNumOperazContabile(sorisFr3.getNumOperazContabile());
		ruoloSorisFr3.setProgrInterOpContab(sorisFr3.getProgrInterOpContab());
		ruoloSorisFr3.setNote("");
		ruoloSorisFr3.setGestAttoreIns(attore);
		ruoloSorisFr3.setGestAttoreUpd(attore);
	}
	
	
		

	private void logStep(Long idElabora, String codPasso, int esito, String note) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(attore);
		registroElabora.setGestAttoreUpd(attore);
		registroElabora.setCodFaseElabora(codFase);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOGGER.error("[ElaborazioneSoris::logStep] Errore nell'esecuzione della query", e);

		}
		if (note.length() > 500) {
			note = note.substring(0, 500);
		}
		LOGGER.debug(note);
	}

	public boolean elaboraRuoloSorisFr7(RuoloSorisFr7DTO ruoloFr7) throws DataAccessException, DAOException, SQLException  {
		attore = "riscabatchpag";
		fase = "Acquisizione Riscossione Soris";
		codFase = "ACQ_RISCOSSIONI_SORIS";	
		boolean ret = false;
		String pIdentPrenotRuolo = "";
		try {
			List<SorisFr7DTO> listSorisFr7 = sorisFr7DAO.loadSorisFr7();


			if(listSorisFr7!= null && !listSorisFr7.isEmpty()) {
				for (int i = 0; i < listSorisFr7.size(); i++) {
					RuoloSorisFr7DTO ruoloSorisFr7 = new RuoloSorisFr7DTO();
						
							setRuoloSorisFr7(ruoloFr7.getIdFileSoris(), listSorisFr7.get(i), ruoloSorisFr7);
							pIdentPrenotRuolo = listSorisFr7.get(i).getpIdentPrenotRuolo();
							ruoloSorisFr7 =ruoloSorisFr7DAO.saveRuoloSorisFr7(ruoloSorisFr7);

							if(listSorisFr7.get(i).getIdPagamento() == null) {
								logStep(ruoloFr7.getIdElabora(), PASSO_ACQ_ANNULLAMENTI, 0,
										fase + " - Step 3 - WARNING - Pagamento da annullare non trovato su risca_r_ruolo_soris_fr3 - p_ident_prenot_ruolo:"
												+ listSorisFr7.get(i).getpIdentPrenotRuolo() );
							}	

							logStep(ruoloFr7.getIdElabora(), PASSO_ACQ_ANNULLAMENTI, 0,
									fase + " - Step 3 - Acquisizione Riscossioni Soris - Pagamento da annullare caricato - OK - Id inserito: "
											+ ruoloSorisFr7.getIdRuoloSorisFr7() + " p ident prenot ruolo: "+ruoloSorisFr7.getpIdentPrenotRuolo());
						
				}
			}			
			ret = true;

		} catch (Exception e) {
			logStep(ruoloFr7.getIdElabora(), PASSO_ACQ_ANNULLAMENTI, 1,
					fase + " - Step 3 - Acquisizione Riscossioni Soris - Insert pagamento su risca_r_ruolo_soris_fr3 KO - p_ident_prenot_ruolo: "+pIdentPrenotRuolo );
			e.printStackTrace();
		}
		return ret;
	}
	
	private void setRuoloSorisFr7(Long idFileSoris, SorisFr7DTO sorisFr7, 
			RuoloSorisFr7DTO ruoloSorisFr7) {
		
		
		ruoloSorisFr7.setIdFileSoris(idFileSoris);	
		ruoloSorisFr7.setTipoRecord("fr7");
		ruoloSorisFr7.setIdRuolo(sorisFr7.getIdRuolo());
		ruoloSorisFr7.setIdPagamento(sorisFr7.getIdPagamento());
		ruoloSorisFr7.setUtenza(sorisFr7.getUtenza());
		ruoloSorisFr7.setProgrRecord(sorisFr7.getProgrRecord());
		ruoloSorisFr7.setCodAmbito(sorisFr7.getCodAmbito());
		ruoloSorisFr7.setCodEnteCreditore(sorisFr7.getCodEnteCreditore());
		ruoloSorisFr7.setAnnoRuolo(sorisFr7.getAnnoRuolo());
		ruoloSorisFr7.setNumeroRuolo(sorisFr7.getNumeroRuolo());
		ruoloSorisFr7.setpTipoUfficio(sorisFr7.getpTipoUfficio());
		ruoloSorisFr7.setpCodiceUfficio(sorisFr7.getpCodiceUfficio());
		ruoloSorisFr7.setpAnnoRiferimento(sorisFr7.getpAnnoRiferimento());
		ruoloSorisFr7.setpIdentPrenotRuolo(sorisFr7.getpIdentPrenotRuolo());
		ruoloSorisFr7.setChiaveInfAnnullare(sorisFr7.getChiaveInfAnnullare());
		ruoloSorisFr7.setChiaveInfCorrettiva(sorisFr7.getChiaveInfCorrettiva());
		ruoloSorisFr7.setTipoEvento(sorisFr7.getTipoEvento());
		ruoloSorisFr7.setMotivoRichAnnul(sorisFr7.getMotivoRichAnnul());
		ruoloSorisFr7.setEnteRichiedente(sorisFr7.getEnteRichiedente());
		ruoloSorisFr7.setDataRichiesta(sorisFr7.getDataRichiesta());
		ruoloSorisFr7.setNote("");
		ruoloSorisFr7.setGestAttoreIns(attore);
		ruoloSorisFr7.setGestAttoreUpd(attore);
	}

}
