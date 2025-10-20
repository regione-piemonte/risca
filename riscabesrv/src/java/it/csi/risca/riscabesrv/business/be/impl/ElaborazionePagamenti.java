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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoModalitaPagDAO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.ElaborazionePagamentiResultDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.PagamentoDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;
import it.csi.risca.riscabesrv.dto.RpPagopaDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component("elaborazionePagamenti")
public class ElaborazionePagamenti {

	@Autowired
	private IuvDAO iuvDAO;

	@Autowired
	private PagamentoDAO pagamentoDAO;

	@Autowired
	private CalcoloInteresseDAO calcoloInteresseDAO;

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private TipoModalitaPagDAO tipoModalitaPagDAO;

	// Passi per PAGOPA
	public static final String PASSO_CODAVVISO_PRESENTE = "CODAVVISO_PRESENTE";
	private static final String PASSO_PRESENZA_CODAVVISO = "PRESENZA_CODAVVISO";
	private static final String PASSO_UPD_IUV_UTILIZZATO = "UPD_IUV_UTILIZZATO";
	private static final String PASSO_VERIF_SD_AVVISO_PAG = "VERIF_SD_AVVISO_PAG";
	private static final String PASSO_VERIF_RATA_PRINC = "VERIF_RATA_PRINC";
	private static final String PASSO_INS_PAGAM_IN_DEF = "INS_PAGAM_IN_DEF";
	private static final String PASSO_INS_DETT_PAG_IN_DEF = "INS_DETT_PAG_IN_DEF";

	// Passi per POSTE
	private static final String PASSO_ESTRAE_PAGAM_DA_WORK = "ESTRAE_PAGAM_DA_WORK";

	private static final String NUMERO_CONTO = "NumeroConto";
	private static final String COD_AMBITO_AMBIENTE = "AMBIENTE";

	private static final String COD_STATO_IUV_ATT = "ATT";
	private static final String COD_STATO_IUV_UTI = "UTI";

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String BONIFICO_ISTANTANEO = "BONIFICO INSTANT IN ENTRATA";
	private static final String COD_TIPO_MODALITA_PAG_RESIDUALI_POSTE = "RES";

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	private String fase;
	private String codFase;
	private String attore;

	private TipoModalitaPagDTO tipoModPag;

	public ElaborazionePagamentiResultDTO elaboraPagamentiPagopa(List<RpPagopaDTO> listaPagopa, Long idElabora) {

		attore = "pagamentipagopa";
		fase = "Acquisizione Pagamenti PagoPA";
		codFase = "ACQ_PAGAMENTI_PAGOPA";

		Long idAmbito = elaboraDAO.loadElaboraById(idElabora, false).getAmbito().getIdAmbito();

		int numPagamLetti = 0;
		int numPagamScart = 0;
		int numPagamCaric = 0;
		String nap = null;
		String soggettoVers = null;
		String quintoCampo = null;
		
		List<StatoDebitorioExtendedDTO> sdElaborati = new ArrayList<StatoDebitorioExtendedDTO>();
		
		for (RpPagopaDTO pagopaDto : listaPagopa) {
			// 3.5 -3.6
			numPagamLetti++;
			if (pagopaDto.getPosizDebitoria() != null) {
				// potrebbero arrivare dall'esterno valori più lunghi che tanto non
				// sono presenti nel NAP e che darebbero errore sul DB
				String posDeb = pagopaDto.getPosizDebitoria().length() > 20
						? pagopaDto.getPosizDebitoria().substring(0, 20)
						: pagopaDto.getPosizDebitoria();
				pagopaDto.setPosizDebitoria(posDeb);
				nap = posDeb;
				quintoCampo = StringUtils.rightPad(posDeb, 16, '0');
			} else {
				// Se posiz_debitoria e' NULL allora si tratta di un "PAGAMENTO SPONTANEO"
				soggettoVers = pagopaDto.getCognRsocDebitore();
				if (pagopaDto.getNomeDebitore() != null) {
					soggettoVers = soggettoVers + " " + pagopaDto.getNomeDebitore();
				}
				if (pagopaDto.getCfPiDebitore() != null) {
					soggettoVers = soggettoVers + " CF " + pagopaDto.getCfPiDebitore();
				}
				soggettoVers = soggettoVers.trim();
			}

			// 3.7 Verificare l'esistenza del CODICE_AVVISO nella RISCA_T_PAGAMENTO
			List<PagamentoDTO> listaPagCodAvviso = pagamentoDAO.getPagamentiByCodiceAvviso(pagopaDto.getCodiceAvviso());
			if (listaPagCodAvviso.size() > 0) {
				numPagamScart++;
				logStep(idElabora, PASSO_PRESENZA_CODAVVISO, 1, fase + " - Step 3 - WARNING - Codice Avviso "
						+ pagopaDto.getCodiceAvviso() + " gia' presente nella tabella RISCA_T_PAGAMENTO");
				// Si passa record successivo
				continue;
			}

			// 3.8 Verifica esistenza del NAP nella RISCA_T_IUV poiché se il NAP e' NULL
			// oppure non viene trovato nella RISCA_T_IUV andra' inserito solo il Pagamento
			boolean foundNapInIuv = false;
			try {
				IuvDTO iuvDto = iuvDAO.getIuvByNap(pagopaDto.getPosizDebitoria());
				if (iuvDto != null && iuvDto.getIdIuv() != null) {
					foundNapInIuv = true;
					if (iuvDto.getStatoIuv().getCodStatoIuv().equalsIgnoreCase(COD_STATO_IUV_UTI)) {
						// WARNING stato iuv UTILIZZATO
						numPagamScart++;
						logStep(idElabora, PASSO_PRESENZA_CODAVVISO, 1,
								fase + " - Step 3 - WARNING - IUV con stato_utilizzo = UTILIZZATO - IUV: "
										+ iuvDto.getIuv());
						// Si passa record successivo
						continue;
					}
					if (iuvDto.getStatoIuv().getCodStatoIuv().equalsIgnoreCase(COD_STATO_IUV_ATT)) {
						// stato iuv ATTIVO --> aggiorno iuv a stato UTILIZZATO
						iuvDAO.updateStatoIuvByNap(pagopaDto.getPosizDebitoria(), COD_STATO_IUV_UTI, attore);
						logStep(idElabora, PASSO_UPD_IUV_UTILIZZATO, 0, fase
								+ " - Step 3 - Aggiornamento Stato IUV per il NAP: " + pagopaDto.getPosizDebitoria());
					}
				}
			} catch (Exception e) {
				LOGGER.debug("[ElaborazionePagamenti::getIuvByNap] " + e.getMessage());
			}

			boolean utenzaSingola = true;
			List<StatoDebitorioExtendedDTO> sdList = new ArrayList<StatoDebitorioExtendedDTO>();
			// 3.9 se foundNapInIuv = true cercare SD legato all'avviso di pagamento
			if (foundNapInIuv) {
				try {
					sdList = statoDebitorioDAO.loadAllStatoDebitorioByNap(nap, null, null, null, null);
					if (sdList.size() == 0) {
						// WARNING Stato debitorio non trovato
						numPagamScart++;
						logStep(idElabora, PASSO_VERIF_SD_AVVISO_PAG, 1,
								fase + " - Step 3 - WARNING - Stato debitorio non trovato per NAP: " + nap);
						// Si passa record successivo
						continue;
					} else if (sdList.size() > 1) {
						// NAP legato a più stati debitori --> saltare ai passi 3.13-3.16
						utenzaSingola = false;
					}
				} catch (DAOException e) {
					LOGGER.debug("[ElaborazionePagamenti::loadAllStatoDebitorioByNap] " + e.getMessage());
					numPagamScart++;
					logStep(idElabora, PASSO_VERIF_SD_AVVISO_PAG, 1,
							fase + " - Step 3 - WARNING - Stato debitorio non trovato per NAP: " + nap);
					// Si passa record successivo
					continue;
				}
			}

			if (utenzaSingola) {
				RataSdDTO rataSd = null;
				Long idStatoDebitorio = null;
				// passi 3.10 - 3.12
				if (foundNapInIuv) {
					// 3.10 estrarre rata padre
					idStatoDebitorio = sdList.get(0).getIdStatoDebitorio();
					rataSd = rataSdDAO.loadRataSdByStatoDebitorio(idStatoDebitorio);
					if (rataSd == null || rataSd.getIdRataSd() == null) {
						// ERRORE Rata non trovata
						numPagamScart++;
						logStep(idElabora, PASSO_VERIF_RATA_PRINC, 1, fase
								+ " - Step 3 - ERRORE - Impossibile trovare il canone dovuto dello stato debitorio,  NAP: "
								+ nap + " - SD: " + idStatoDebitorio);
						// Si passa record successivo
						continue;
					}
				}

				// 3.11 inserimento pagamento e dettaglio
				PagamentoDTO pagDto = inserisciPagamento(idElabora, idAmbito, pagopaDto, quintoCampo, soggettoVers);
				if (pagDto == null || pagDto.getIdPagamento() == null) {
					numPagamScart++;
					// Si passa record successivo
					continue;
				} else {
					numPagamCaric++;
				}

				if (foundNapInIuv) {
					// 3.12 Calcolo interessi
					boolean okDett = inserisciDettaglioPagamento(idAmbito, idElabora, pagDto.getIdPagamento(),
							pagopaDto, rataSd, true);
					if (!okDett) {
						// Si passa record successivo
						continue;
					}

					// Update attivita' stato debitorio
					try {
						statoDebitorioDAO.updateAttivitaStatoDebitorio(idStatoDebitorio);
					} catch (DAOException e) {
						LOGGER.debug("[ElaborazionePagamenti::updateAttivitaStatoDebitorio] " + e.getMessage());
					}
				}

			} else {
				BigDecimal sommInteressi = BigDecimal.ZERO;
				BigDecimal sommCanoneDovuto = BigDecimal.ZERO;
				RataSdDTO rataSd = null;
				// passi 3.13 -1.16
				try {
					sdList = statoDebitorioDAO.loadAllStatoDebitorioByNap(nap, null, null, null, null);
				} catch (DAOException e) {
					LOGGER.debug("[ElaborazionePagamenti::loadAllStatoDebitorioByNap] " + e.getMessage());
					numPagamScart++;
					logStep(idElabora, PASSO_VERIF_SD_AVVISO_PAG, 1,
							fase + " - Step 3 - WARNING - Stato debitorio non trovato per NAP: " + nap);
					// Si passa record successivo
					continue;
				}
				for (StatoDebitorioExtendedDTO sd : sdList) {
					// 31.14 2.estrarre rata padre
					rataSd = rataSdDAO.loadRataSdByStatoDebitorio(sd.getIdStatoDebitorio());
					if (rataSd == null || rataSd.getIdRataSd() == null) {
						// ERRORE Rata non trovata
						numPagamScart++;
						logStep(idElabora, PASSO_VERIF_RATA_PRINC, 1, fase
								+ " - Step 3 - ERRORE - Impossibile trovare il canone dovuto dello stato debitorio,  NAP: "
								+ nap + " - SD: " + sd.getIdStatoDebitorio());
						// Si passa record successivo
						continue;
					}
					// 3.14 3.calcolo interessi
					SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
					String dataEsitoPag = df.format(pagopaDto.getDataEsitoPagamento());
					try {
						BigDecimal interessiPerSd = calcoloInteresseDAO.calcoloInteressi(idAmbito,
								rataSd.getCanoneDovuto(), rataSd.getDataScadenzaPagamento(), dataEsitoPag);
						sommInteressi = sommInteressi.add(interessiPerSd);
						sommCanoneDovuto = rataSd.getCanoneDovuto() != null
								? sommCanoneDovuto.add(rataSd.getCanoneDovuto())
								: sommCanoneDovuto;
					} catch (DataAccessException | BusinessException | SQLException | SystemException e) {
						numPagamScart++;
						logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 1,
								fase + " - Step 3 - ERRORE - Errore nel calcolo interessi per rataSd : "
										+ rataSd.getIdRataSd());
						// Si passa record successivo
						continue;
					}
				}

				// 3.15 Inserimento pagamento
				PagamentoDTO pagDto = inserisciPagamento(idElabora, idAmbito, pagopaDto, quintoCampo, soggettoVers);
				if (pagDto == null || pagDto.getIdPagamento() == null) {
					numPagamScart++;
					// Si passa record successivo
					continue;
				} else {
					numPagamCaric++;
				}

				// 3.16 Se l'Importo Pagato e' uguale alla sommatoria dei Canoni dovuti (per SD)
				// + sommatoria degli Interessi (per SD)
				BigDecimal sommaPagata = sommCanoneDovuto.add(sommInteressi);
				if (pagopaDto.getImportoPagato().equals(sommaPagata)) {
					for (StatoDebitorioExtendedDTO sd : sdList) {
						// estrazione rata
						rataSd = rataSdDAO.loadRataSdByStatoDebitorio(sd.getIdStatoDebitorio());
						// Calcolo interessi e inserimento dettaglio pagamento
						boolean okDett = inserisciDettaglioPagamento(idAmbito, idElabora, pagDto.getIdPagamento(),
								pagopaDto, rataSd, false);
						if (!okDett) {
							// Si passa record successivo
							continue;
						}
						// Update attivita' stato debitorio
						try {
							statoDebitorioDAO.updateAttivitaStatoDebitorio(sd.getIdStatoDebitorio());
						} catch (DAOException e) {
							LOGGER.debug("[ElaborazionePagamenti::updateAttivitaStatoDebitorio] " + e.getMessage());
						}
					}
				}
			}
			
			sdElaborati.addAll(sdList);

		}

		ElaborazionePagamentiResultDTO result = new ElaborazionePagamentiResultDTO();
		result.setSdList(sdElaborati);
		result.setNumPagamLetti(numPagamLetti);
		result.setNumPagamScart(numPagamScart);
		result.setNumPagamCaric(numPagamCaric);
		return result;
	}

	public boolean elaboraPagamentoNonPremarcato(RpNonPremarcatiDTO rpNonPremarcato) {
		attore = "riscabatchpag";
		fase = "Acquisizione Pagamenti POSTE";
		codFase = "ACQ_PAGAMENTI_POSTE";
		Long idElabora = rpNonPremarcato.getIdElabora();
		Long idAmbito = elaboraDAO.loadElaboraById(idElabora, false).getAmbito().getIdAmbito();

		boolean isOk = controlloScartiNonPremarcati(rpNonPremarcato, idElabora);
		if (isOk) {
			PagamentoDTO pagamento = inserisciPagamento(idAmbito, rpNonPremarcato);
			if (pagamento == null) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	public boolean elaboraPagamentoEstrco(RpEstrcoDTO rpEstrco) {
		attore = "riscabatchpag";
		fase = "Acquisizione Pagamenti POSTE";
		codFase = "ACQ_PAGAMENTI_POSTE";
		Long idElabora = rpEstrco.getIdElabora();

		Long idAmbito = elaboraDAO.loadElaboraById(idElabora, false).getAmbito().getIdAmbito();

		boolean isOk = controlloScartiEstrco(rpEstrco, idElabora);
		if (isOk) {
			PagamentoDTO pagamento = inserisciPagamento(idAmbito, rpEstrco);
			if (pagamento == null) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	private boolean controlloScartiEstrco(RpEstrcoDTO rpEstrco, Long idElabora) {
		// Controlli sui dati di input
		if (rpEstrco.getCausaleAbi() == null || rpEstrco.getDataValuta() == null
				|| rpEstrco.getDescrizioneMovimento() == null || rpEstrco.getDescrizioneMovimento().contains("/PUR/")
				|| rpEstrco.getDescrizioneMovimento().contains("/RFS/")
				|| rpEstrco.getDescrizioneMovimento().contains("/RFB/")) {
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - ESTRCO - WARNING - Dati input non validi - descrizione movimento: "
							+ trim(rpEstrco.getDescrizioneMovimento()) + " - Progressivo movimento: "
							+ rpEstrco.getProgressivoMovimento());
			// Pagamento scartato
			return false;
		}

		// Estrazione della modalita' di pagamento
		tipoModPag = tipoModalitaPagDAO.loadTipoModalitaPagByCodTipoModalitaPag(rpEstrco.getCausaleAbi());
		if (tipoModPag == null) {
			// Evolutiva 06/2024
			if (rpEstrco.getCausaleAbi().equalsIgnoreCase("07")
					&& rpEstrco.getDescrizioneMovimento().indexOf(BONIFICO_ISTANTANEO) != -1) {
				// Si tratta di un Bonifico istantaneo e non lo devo scartare
				// Utilizzo tipoModPag 14 ('RES', descrizione =  'residuali bonifici POSTE')
				tipoModPag = tipoModalitaPagDAO.loadTipoModalitaPagByCodTipoModalitaPag(COD_TIPO_MODALITA_PAG_RESIDUALI_POSTE);
			} else {
				logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
						fase + " - Step 3 - ESTRCO - WARNING - Modalita' di pagamento non trovata: "
								+ rpEstrco.getCausaleAbi() + " - Progressivo movimento: "
								+ rpEstrco.getProgressivoMovimento());
				// Pagamento scartato
				return false;
			}
		}
		
		if (tipoModPag != null) {
			// Verifica esistenza pagamento
			List<PagamentoDTO> pagamenti = getPagamento(rpEstrco);
			if (pagamenti != null && pagamenti.size() > 0) {
				// Esiste un pagamento --> pagamento scartato
				logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
						fase + " - Step 3 - ESTRCO - WARNING - Pagamento gia' esistente: "
								+ pagamenti.get(0).getIdPagamento() + " - Progressivo movimento: "
								+ rpEstrco.getProgressivoMovimento());
				return false;
			}
		}

		return true;
	}

	private boolean controlloScartiNonPremarcati(RpNonPremarcatiDTO rpNonPremarcato, Long idElabora) {
		// Verifico numero conto da elaborare
		try {
			String numeroContoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(COD_AMBITO_AMBIENTE, NUMERO_CONTO)
					.get(0).getValore();
			if (!rpNonPremarcato.getNumeroConto().equals(numeroContoConfig)) {
				// Pagamento scartato
				logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
						fase + " - Step 3 - NON PREMARCATI - WARNING - Numero Conto errato : "
								+ rpNonPremarcato.getNumeroConto() + " - Progr: " + rpNonPremarcato.getProgr());
				return false;
			}
		} catch (SQLException e) {
			LOGGER.debug("[ElaborazionePagamenti::esisteNap] ERROR " + e.getMessage());
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - NON PREMARCATI  - WARNING - Errore in verifica Numero Conto : "
							+ rpNonPremarcato.getNumeroConto() + " - Progr: " + rpNonPremarcato.getProgr());
			return false;
		}

		// Controllo se il numero avviso e' valorizzato
		String numAvviso = rpNonPremarcato.getNumeroAvviso();
		if (numAvviso == null || numAvviso.matches("^[0]+$")) {
			// Pagamento scartato
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - NON PREMARCATI - WARNING - Numero Avviso non valorizzato : "
							+ rpNonPremarcato.getNumeroAvviso() + " - Progr: " + rpNonPremarcato.getProgr());
			return false;
		}

		// Controllo se 'data_acc' (data del versamento), e' valorizzata e valida
		// (formato yyyyMMdd)
		String dataAcc = rpNonPremarcato.getDataAcc();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			df.parse(dataAcc);
		} catch (ParseException e) {
			// Data acc non valida --> Pagamento scartato
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - NON PREMARCATI - WARNING - Data Acc non valida : "
							+ rpNonPremarcato.getDataAcc() + " - Progr: " + rpNonPremarcato.getProgr());
			return false;
		}

		// Controllo se esiste gia' il pagamento su RISCA_T_PAGAMENTO utilizzando il
		// quinto-campo
		if (esistePagamento(rpNonPremarcato)) {
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - NON PREMARCATI - WARNING - Pagamento gia' esistente – Quinto_campo: "
							+ rpNonPremarcato.getQuintoCampo() + " - Progr: " + rpNonPremarcato.getProgr());
			// Pagamento scartato
			return false;
		}

		// Estrazione della modalita' di pagamento
		tipoModPag = tipoModalitaPagDAO.loadTipoModalitaPagByCodTipoModalitaPag(rpNonPremarcato.getTipoDoc());
		if (tipoModPag == null) {
			logStep(idElabora, PASSO_ESTRAE_PAGAM_DA_WORK, 0,
					fase + " - Step 3 - NON PREMARCATI - WARNING - Modalita' di pagamento non trovata: "
							+ rpNonPremarcato.getTipoDoc() + " - Progr: " + rpNonPremarcato.getProgr());
			// Pagamento scartato
			return false;
		}

		return true;
	}

	private boolean esistePagamento(RpNonPremarcatiDTO rpNonPremarcato) {
		boolean esistePag = false;
		List<PagamentoDTO> listaPag = pagamentoDAO.getPagamentiByQuintoCampo(rpNonPremarcato.getQuintoCampo());
		if (listaPag.size() > 0) {
			esistePag = true;
		}
		return esistePag;
	}

	private List<PagamentoDTO> getPagamento(RpEstrcoDTO rpEstrco) {
		List<PagamentoDTO> pagamenti = null;
		if (rpEstrco.getRiferimentoBanca() != null && !rpEstrco.getRiferimentoBanca().trim().isEmpty()) {
			pagamenti = pagamentoDAO.getPagamentiForPosteEstrco(trim(rpEstrco.getDataValuta()),
					rpEstrco.getImportoMovimento(), trim(rpEstrco.getDescrizioneMovimento()),
					tipoModPag.getIdTipoModalitaPag(), trim(rpEstrco.getRiferimentoBanca().trim()));
		} else {
			pagamenti = pagamentoDAO.getPagamentiForPosteEstrco(trim(rpEstrco.getDataValuta()),
					rpEstrco.getImportoMovimento(), trim(rpEstrco.getDescrizioneMovimento()),
					tipoModPag.getIdTipoModalitaPag(), null);
		}
		return pagamenti;
	}

	private boolean inserisciDettaglioPagamento(Long idAmbito, Long idElabora, Long idPagamento, RpPagopaDTO pagopaDto,
			RataSdDTO rataSd, boolean utenzaSingola) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		String dataEsitoPag = df.format(pagopaDto.getDataEsitoPagamento());
		LOGGER.debug("[ElaborazionePagamenti::inserisciDettaglioPagamento] INIZIO ");
		boolean ret = false;
		try {
			BigDecimal interessiPerSd = calcoloInteresseDAO.calcoloInteressi(idAmbito, rataSd.getCanoneDovuto(),
					rataSd.getDataScadenzaPagamento(), dataEsitoPag);

			LOGGER.debug("[ElaborazionePagamenti::calcoloInteressi] interessiPerSd= " + interessiPerSd);

			DettaglioPagDTO dettDto = new DettaglioPagDTO();
			dettDto.setIdPagamento(idPagamento);
			dettDto.setIdRataSd(rataSd.getIdRataSd());
			if (utenzaSingola) {
				dettDto.setImportoVersato(pagopaDto.getImportoPagato());
			} else {
				BigDecimal impVers = rataSd.getCanoneDovuto().add(interessiPerSd);
				dettDto.setImportoVersato(impVers);
			}
			dettDto.setInteressiMaturati(interessiPerSd);
			dettDto.setGestAttoreIns(attore);
			dettDto.setGestAttoreUpd(attore);

			dettDto = dettaglioPagDAO.saveDettaglioPag(dettDto);

			if (dettDto == null || dettDto.getIdDettaglioPag() == null) {
				deletePagamentoAndLogError(idElabora, idPagamento,
						"Inserimento dati dettaglio del pagamento KO - ID Pagamento: " + idPagamento);
			} else {
				LOGGER.debug("[ElaborazionePagamenti::inserisciDettaglioPagamento] dettaglio pagamento inserito.");
				logStep(idElabora, PASSO_INS_DETT_PAG_IN_DEF, 0,
						fase + " - Step 3 - Inserimento dati dettaglio del pagamento OK - ID: "
								+ dettDto.getIdDettaglioPag());
				ret = true;
			}
		} catch (Exception e) {
			deletePagamentoAndLogError(idElabora, idPagamento, e.getMessage());
		}
		LOGGER.debug("[ElaborazionePagamenti::inserisciDettaglioPagamento] FINE ");
		return ret;
	}

	private void deletePagamentoAndLogError(Long idElabora, Long idPagamento, String error) {
		LOGGER.error("[ElaborazionePagamenti::deletePagamentoAndLogError] ERRORE " + error);
		logStep(idElabora, PASSO_INS_DETT_PAG_IN_DEF, 1,
				fase + " - Step 3 - Inserimento dati dettaglio del pagamento KO - ID Pagamento: " + idPagamento);
		// devo cancellare il pagamento eventualmente inserito in precedenza perche' e'
		// fallito l'inserimento del dettaglio
		pagamentoDAO.deleteByIdPagamento(idPagamento);
		LOGGER.debug("[ElaborazionePagamenti::deletePagamentoAndLogError] Cancellazione del pagamento  idPagamento = "
				+ idPagamento);
	}

	private PagamentoDTO inserisciPagamento(Long idElabora, Long idAmbito, RpPagopaDTO pagopaDto, String quintoCampo,
			String soggettoVers) {
		PagamentoExtendedDTO pagDto = new PagamentoExtendedDTO();
		pagDto.setIdAmbito(idAmbito);
		pagDto.setIdTipoModalitaPag(11l); // pagopa
		pagDto.setIdTipologiaPag(1l); // Automatico(Poste/PagoPa)
		String causale = pagopaDto.getCausale();
		if (pagopaDto.getNote() != null) {
			causale = causale + " - " + pagopaDto.getNote();
		}
		if (pagopaDto.getIuv() != null) {
			causale = causale + " - IUV: " + pagopaDto.getIuv();
		}
		pagDto.setCausale(causale);
		pagDto.setDataOpVal(pagopaDto.getDataEsitoPagamento());
		pagDto.setImportoVersato(pagopaDto.getImportoPagato());
		pagDto.setDataDownload(new Date());
		pagDto.setIdFilePoste(null);
		pagDto.setNumeroPagamento(null);
		pagDto.setQuintoCampo(quintoCampo);
		pagDto.setCodiceAvviso(pagopaDto.getCodiceAvviso());
		if (pagopaDto.getCodiceAvviso() != null) {
			pagDto.setNote("CODICE AVVISO: " + pagopaDto.getCodiceAvviso());
		}
		pagDto.setSoggettoVersamento(soggettoVers);
		pagDto.setFlgRimborsato(0);
		pagDto.setGestAttoreIns(attore);
		pagDto.setGestAttoreUpd(attore);
		try {
			pagDto = pagamentoDAO.savePagamento(pagDto, attore);
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 0,
					fase + " - Step 3 - Inserimento dati del pagamento OK - ID: " + pagDto.getIdPagamento());
		} catch (SQLException e) {
			LOGGER.debug("[ElaborazionePagamenti::inserisciPagamento] " + e.getMessage());
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 1, fase
					+ " - Step 3 - Inserimento dati del pagamento KO - Codice Avviso : " + pagopaDto.getCodiceAvviso());
			return null;
		}
		return pagDto;
	}

	private PagamentoDTO inserisciPagamento(Long idAmbito, RpNonPremarcatiDTO rpNonPremarcato) {
		PagamentoExtendedDTO pagDto = new PagamentoExtendedDTO();
		Long idElabora = rpNonPremarcato.getIdElabora();
		try {
			pagDto.setIdAmbito(idAmbito);
			pagDto.setIdTipoModalitaPag(tipoModPag.getIdTipoModalitaPag());
			pagDto.setIdTipologiaPag(1l); // Automatico(Poste)
			pagDto.setIdFilePoste(rpNonPremarcato.getIdFilePoste());
			pagDto.setIdImmagine(rpNonPremarcato.getIdImmagine());
			pagDto.setCausale(null); // causale non disponibile per non premarcati
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			pagDto.setDataOpVal(df.parse(rpNonPremarcato.getDataAcc()));
			pagDto.setImportoVersato(rpNonPremarcato.getImporto());
			pagDto.setDataDownload(new Date());
			pagDto.setQuintoCampo(rpNonPremarcato.getQuintoCampo());
			pagDto.setCodiceAvviso(rpNonPremarcato.getNumeroAvviso());
			pagDto.setFlgRimborsato(0);
			pagDto.setGestAttoreIns(attore);
			pagDto.setGestAttoreUpd(attore);

			pagDto = pagamentoDAO.savePagamento(pagDto, attore);
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 0,
					fase + " - Step 3 - NON PREMARCATI - Inserimento dati del pagamento OK - ID: "
							+ pagDto.getIdPagamento() + " - Progr: " + rpNonPremarcato.getProgr());
		} catch (Exception e) {
			LOGGER.debug("[ElaborazionePagamenti::inserisciPagamento] " + e.getMessage());
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 1,
					fase + " - Step 3 - NON PREMARCATI - Inserimento dati del pagamento KO - Numero Avviso : "
							+ rpNonPremarcato.getNumeroAvviso() + " - Progr: " + rpNonPremarcato.getProgr());
			return null;
		}
		return pagDto;
	}

	private PagamentoDTO inserisciPagamento(Long idAmbito, RpEstrcoDTO rpEstrco) {
		PagamentoExtendedDTO pagDto = new PagamentoExtendedDTO();
		Long idElabora = rpEstrco.getIdElabora();
		try {
			pagDto.setIdAmbito(idAmbito);
			pagDto.setIdTipoModalitaPag(tipoModPag.getIdTipoModalitaPag());
			pagDto.setIdTipologiaPag(1l); // Automatico(Poste)
			pagDto.setIdFilePoste(rpEstrco.getIdFilePoste());
			pagDto.setCausale(rpEstrco.getDescrizioneMovimento());
			SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
			pagDto.setDataOpVal(df.parse(trim(rpEstrco.getDataValuta())));
			pagDto.setImportoVersato(rpEstrco.getImportoMovimento());
			pagDto.setDataDownload(new Date());
			pagDto.setCro(rpEstrco.getRiferimentoBanca());
			pagDto.setNumeroPagamento(rpEstrco.getNumeroProgressivo());
			pagDto.setFlgRimborsato(0);
			pagDto.setGestAttoreIns(attore);
			pagDto.setGestAttoreUpd(attore);
			
			if (tipoModPag.getCodModalitaPag().equals(COD_TIPO_MODALITA_PAG_RESIDUALI_POSTE)) {
				pagDto.setNote(BONIFICO_ISTANTANEO);
			};

			pagDto = pagamentoDAO.savePagamento(pagDto, attore);
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 0,
					fase + " - Step 3 - ESTRCO - Inserimento dati del pagamento OK - ID: " + pagDto.getIdPagamento()
							+ " - Progressivo movimento: " + rpEstrco.getProgressivoMovimento());
		} catch (Exception e) {
			LOGGER.debug("[ElaborazionePagamenti::inserisciPagamento] " + e.getMessage());
			logStep(idElabora, PASSO_INS_PAGAM_IN_DEF, 1,
					fase + " - Step 3 - ESTRCO - Inserimento dati del pagamento KO - Progressivo movimento : "
							+ rpEstrco.getProgressivoMovimento());
			return null;
		}
		return pagDto;
	}

	private String trim(String s) {
		return s != null ? s.trim() : null;
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
			LOGGER.error("[ElaborazionePagamenti::logStep] Errore nell'esecuzione della query", e);

		}
		if (note.length() > 500) {
			note = note.substring(0, 500);
		}
		LOGGER.debug(note);
	}

}
