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
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.BilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.BilAccDTO;
import it.csi.risca.riscabesrv.dto.BilAccResultDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagBilAccDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SommaCanoneTipoUsoSdDTO;
import it.csi.risca.riscabesrv.dto.SommaImpAssegnatoTipoUsoSdDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class PagamentoBilAccLogic {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private PagamentoDAO pagamentoDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private RiscossioneDAO riscossioneDAO;

	@Autowired
	private SoggettiDAO soggettiDAO;

	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;

	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;

	@Autowired
	private BilAccDAO bilAccDAO;

	@Autowired
	private TipoUsoDAO tipoUsoDAO;

	@Autowired
	private DettaglioPagBilAccDAO dettaglioPagBilAccDAO;

	public BilAccResultDTO saveDettaglioPagamentoBilAcc(String attore, Long idPagamento) {
		LOGGER.debug("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc] BEGIN");
		BilAccResultDTO result = new BilAccResultDTO();
		try {
			PagamentoExtendedDTO pagamento = pagamentoDAO.getPagamentoByIdPagamento(idPagamento);
			if (pagamento == null) {
				return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
						"Pagamento non trovato - idPagamento = " + idPagamento);
			}
			result.setIdPagamento(idPagamento);

			List<DettaglioPagDTO> dettagliPag = pagamento.getDettaglioPag();
			if (dettagliPag == null || dettagliPag.size() == 0) {
				return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
						"Pagamento senza dettaglio - idPagamento = " + idPagamento);
			}

			Long idAmbito = pagamento.getAmbito().getIdAmbito();

			// Modalita di pagamento
			TipoModalitaPagDTO tipoModalitaPag = pagamento.getTipoModalitaPag();
			boolean flgRuolo = false;
			if (tipoModalitaPag.getCodModalitaPag().equals("RC")
					|| (pagamento.getNote() != null && pagamento.getNote().toUpperCase().indexOf("SORIS") != -1)
					|| (pagamento.getNote() != null && pagamento.getNote().toUpperCase().indexOf("EQUITALIA") != -1)) {
				// RC = Riscossione coattiva
				flgRuolo = true;
			}

			// Anno del pagamento
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pagamento.getDataOpVal());
			int annoPag = calendar.get(Calendar.YEAR);

			for (DettaglioPagDTO dettaglioPag : dettagliPag) {
				BigDecimal impVersatoDett = dettaglioPag.getImportoVersato();

				if (impVersatoDett.compareTo(BigDecimal.ZERO) <= 0) {
					// se l'importo versato non e' maggiore di zero passo al dettaglio successivo
					continue;
				}

				// 2. Dati della rata dello stato debitorio
				RataSdDTO rataSd = rataSdDAO.loadRataSdById(dettaglioPag.getIdRataSd());

				// Passo 2.2 verificare su risca_r_dettaglio_pag_bil_acc eventuali record
				// associati, ovvero se esistono altri record con id_dettaglio_pag, diverso da
				// quello su cui si sta lavorando, associato allo stesso id_rata_sd
				BigDecimal importoAssegnato = BigDecimal.ZERO;
				List<DettaglioPagBilAccDTO> listDettBilByRata = dettaglioPagBilAccDAO
						.getDettaglioPagBilAccByIdRataSd(rataSd.getIdRataSd());
				if (listDettBilByRata != null && listDettBilByRata.size() > 0) {
					for (DettaglioPagBilAccDTO dett : listDettBilByRata) {
						importoAssegnato = importoAssegnato.add(dett.getImportoAccertaBilancio());
					}
				}

				BigDecimal impAssegnatoResiduo = BigDecimal.ZERO;
				BigDecimal canoneDovuto = rataSd.getCanoneDovuto().subtract(importoAssegnato);
				if (canoneDovuto.compareTo(BigDecimal.ZERO) < 0) {
					impAssegnatoResiduo = canoneDovuto.abs();
					canoneDovuto = BigDecimal.ZERO;
				}

				BigDecimal interessiMaturati = rataSd.getInteressiMaturati() != null ? rataSd.getInteressiMaturati()
						: BigDecimal.ZERO;
				interessiMaturati = interessiMaturati.subtract(impAssegnatoResiduo);
				if (interessiMaturati.compareTo(BigDecimal.ZERO) < 0) {
					impAssegnatoResiduo = interessiMaturati.abs();
					interessiMaturati = BigDecimal.ZERO;
				}

				// 2.1 Anno accertamento
				int annoCanone = Integer.parseInt(rataSd.getDataScadenzaPagamento().substring(0, 4));

				// 3. Stato debitorio
				StatoDebitorioDTO statoDebitorio = statoDebitorioDAO
						.loadStatoDebitorioLightById(rataSd.getIdStatoDebitorio());

				// spese notifica
				BigDecimal impNotifica = statoDebitorio.getImpSpeseNotifica() != null
						? statoDebitorio.getImpSpeseNotifica()
						: BigDecimal.ZERO;
				impNotifica = impNotifica.subtract(impAssegnatoResiduo);
				if (impNotifica.compareTo(BigDecimal.ZERO) < 0) {
					impAssegnatoResiduo = impNotifica.abs();
					impNotifica = BigDecimal.ZERO;
				}

				// 3.1 se interessiMaturati e' valorizzato
				boolean flgPubblico = false;
				if (interessiMaturati.compareTo(BigDecimal.ZERO) > 0) {
					Long idSoggetto = null;
					if (flgRuolo) {
						idSoggetto = statoDebitorio.getIdSoggetto();
					} else {
						idSoggetto = riscossioneDAO.getIdSoggettoRiscossione(statoDebitorio.getIdRiscossione());
					}
					SoggettiExtendedDTO soggetto = soggettiDAO.loadSoggettoById(idSoggetto);
					if (soggetto.getTipoSoggetto().getCodTipoSoggetto().equals("PB")) {
						// Persona giuridica pubblica
						flgPubblico = true;
					}
				}

				// 4.1 Importo dovuto tot
				BigDecimal impDovutoTot = canoneDovuto.add(interessiMaturati).add(impNotifica);

				Long idAccCanone = null;
				int anno = annoCanone <= annoPag ? annoCanone : annoPag;
				int annoCompetenza = annoCanone;

				// Annualita' dello stato debitorio
				List<AnnualitaSdDTO> listaAnnualitaSd = annualitaSdDAO
						.loadAnnualitaSd(statoDebitorio.getIdStatoDebitorio(), null, null);

				boolean isGrandeIdro = false;
				for (AnnualitaSdDTO annualitaSd : listaAnnualitaSd) {
					// Usi associati all'annualita
					List<AnnualitaUsoSdExtendedDTO> listAnnualitaUsi = annualitaUsoSdDAO
							.loadAnnualitaUsiPrincipaliSd(annualitaSd.getIdAnnualitaSd());
					isGrandeIdro = checkCodiceBilancioGrandeIdro(listAnnualitaUsi);
				}
				if (isGrandeIdro) {
					if (canoneDovuto.compareTo(BigDecimal.ZERO) > 0) {
						// 5.2 Canone per tipo uso
						List<SommaCanoneTipoUsoSdDTO> listaCanoniPerTipoUso = statoDebitorioDAO
								.loadSommaCanonePerTipoUsoAnnualitaSd(statoDebitorio.getIdStatoDebitorio());

						// 5.3 Ricalcolo Canone per tipo Uso
						listaCanoniPerTipoUso = ricalcoloCanonePerTipoUso(rataSd.getIdRataSd(), listaCanoniPerTipoUso);
						
						// Elimino eventuali righe con importo a 0 per evitare di inserirle
						listaCanoniPerTipoUso = listaCanoniPerTipoUso.stream()
								.filter(ctu -> ctu.getTotaleCanoneUso().compareTo(BigDecimal.ZERO) > 0)
								.collect(Collectors.toList());

						// NB. se la lista contiene un solo tipo uso devo sostituire l'importo calcolato
						// totaleCanoneUso (che deriva dalla somma delle varie annualita) con il valore
						// canoneDovuto che ho preso dalla rata
						if (listaCanoniPerTipoUso != null && listaCanoniPerTipoUso.size() == 1) {
							listaCanoniPerTipoUso.get(0).setTotaleCanoneUso(canoneDovuto);
						} else if (listaCanoniPerTipoUso != null && listaCanoniPerTipoUso.size() > 1) {
							// Se ci sono piu' tipi uso devo ricalcolare i totaliCanoneUso per via di
							// eventuali arrotondamenti per fare in modo che la somma coincida con il
							// canoneDovuto

							// calcolo la somma dei totaliCanoneUso
							BigDecimal sommTotCanoniUso = listaCanoniPerTipoUso.stream()
									.map(SommaCanoneTipoUsoSdDTO::getTotaleCanoneUso)
									.reduce(BigDecimal.ZERO, BigDecimal::add);
							if (sommTotCanoniUso.compareTo(canoneDovuto) != 0) {
								// Se la somma non coincide con il canoneDovuto, calcolo la differenza (puo'
								// venire negativa o positiva) e la assegno al primo uso della lista (che e'
								// quello con ordina_tipo_uso minore)
								BigDecimal diff = canoneDovuto.subtract(sommTotCanoniUso);
								BigDecimal newValue = listaCanoniPerTipoUso.get(0).getTotaleCanoneUso().add(diff);
								listaCanoniPerTipoUso.get(0).setTotaleCanoneUso(newValue);
							}
						}
						
						// Rifaccio la verifica per eliminare eventuali importi che diventano 0 dopo gli arrotondamenti
						listaCanoniPerTipoUso = listaCanoniPerTipoUso.stream()
								.filter(ctu -> ctu.getTotaleCanoneUso().compareTo(BigDecimal.ZERO) > 0)
								.collect(Collectors.toList());

						BigDecimal impResiduo = impVersatoDett;
						for (SommaCanoneTipoUsoSdDTO canoneTipoUso : listaCanoniPerTipoUso) {
							// Accertamento relativo al canone
							BilAccDTO bilAcc = bilAccDAO.loadBilAccByIdAccertaBilancio(idAmbito, anno, annoCompetenza,
									canoneTipoUso.getIdAccertaBilancio(), annoPag);
							if (bilAcc == null) {
								// Provo a cercare solo per annoCompetenza
								bilAcc = bilAccDAO.loadBilAccByIdAccertaBilancio(idAmbito, null, annoCompetenza,
										canoneTipoUso.getIdAccertaBilancio(), annoPag);
								if (bilAcc == null) {
									// Provo ancora a cercare per anno = annoCompetenza
									bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, annoCompetenza,
											annoCompetenza, "CANONE", annoPag);
								}
							}
							if (bilAcc == null) {
								// se ancora non ho trovato un id bilancio cerco quello piu' vecchio valido
								idAccCanone = bilAccDAO.loadBilAccValidoPiuVecchio(idAmbito, "CANONE", annoPag)
										.getIdBilAcc();
							} else {
								idAccCanone = bilAcc.getIdBilAcc();
							}

							BigDecimal canone = canoneTipoUso.getTotaleCanoneUso();
							if (impResiduo.compareTo(canone) < 0) {
								canone = impResiduo;
							}
							if (idAccCanone != null) {
								insertDettaglioPagBilAcc(attore, flgRuolo, flgPubblico, false,
										dettaglioPag.getIdDettaglioPag(), idAccCanone, canone);
							} else {
								return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
										"idBilAcc per CANONE non trovato - idPagamento = " + idPagamento);
							}

							// Sottraggo il canone dal residuo per il prossimo tipo uso
							impResiduo = impResiduo.subtract(canone);

							if (impResiduo.compareTo(BigDecimal.ZERO) <= 0) {
								// esco dal ciclo for perche' non ho altro residuo da assegnare al canone
								break;
							}
						}
					}
				} else {
					if (canoneDovuto.compareTo(BigDecimal.ZERO) > 0) {
						// Accertamento relativo al canone
						BilAccDTO bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, anno, annoCompetenza,
								"CANONE", annoPag);
						if (bilAcc == null) {
							// Provo a cercare solo per annoCompetenza
							bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, null, annoCompetenza, "CANONE",
									annoPag);
							if (bilAcc == null) {
								// Provo ancora a cercare per anno = annoCompetenza
								bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, annoCompetenza,
										annoCompetenza, "CANONE", annoPag);
							}
						}
						if (bilAcc == null) {
							// se ancora non ho trovato un id bilancio cerco quello piu' vecchio valido
							idAccCanone = bilAccDAO.loadBilAccValidoPiuVecchio(idAmbito, "CANONE", annoPag)
									.getIdBilAcc();
						} else {
							idAccCanone = bilAcc.getIdBilAcc();
						}

						BigDecimal canone = canoneDovuto;
						if (impVersatoDett.compareTo(canoneDovuto) < 0) {
							canone = impVersatoDett;
						}
						if (idAccCanone != null) {
							insertDettaglioPagBilAcc(attore, flgRuolo, flgPubblico, false,
									dettaglioPag.getIdDettaglioPag(), idAccCanone, canone);
						} else {
							return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
									"idBilAcc per CANONE non trovato - idPagamento = " + idPagamento);
						}
					}
				}

				// Accertamento relativo ai Tassi d'interesse
				// Confronto fra canone dovuto e importo versato
				if (impVersatoDett.compareTo(canoneDovuto) > 0) {
					// Verifica interessi

					if (interessiMaturati.compareTo(BigDecimal.ZERO) > 0) {

						BigDecimal residuoInteressi = impVersatoDett.subtract(canoneDovuto);

						BigDecimal interessi = interessiMaturati;
						if (residuoInteressi.compareTo(interessi) < 0) {
							interessi = residuoInteressi;
						}
						Long idBilAccInteressi = null;
						// Accertamento relativo agli interessi maturati
						BilAccDTO bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, annoPag, null,
								"INTERESSI", annoPag);
						if (bilAcc == null) {
							// se ancora non ho trovato un id bilancio cerco quello piu' vecchio valido
							idBilAccInteressi = bilAccDAO.loadBilAccValidoPiuVecchio(idAmbito, "INTERESSI", annoPag)
									.getIdBilAcc();
						} else {
							idBilAccInteressi = bilAcc.getIdBilAcc();
						}
						if (idBilAccInteressi != null) {
							// Se per caso non ha trovato un idBilAcc per interessi allora non inserisce la
							// riga e AMEN!
							insertDettaglioPagBilAcc(attore, flgRuolo, flgPubblico, false,
									dettaglioPag.getIdDettaglioPag(), idBilAccInteressi, interessi);
						} else {
							return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
									"idBilAcc per INTERESSI non trovato - idPagamento = " + idPagamento);
						}
					}
				}

				BigDecimal canonePiuInteressi = canoneDovuto.add(interessiMaturati);

				if (impVersatoDett.compareTo(canonePiuInteressi) > 0) {
					// Accertamento relativo alle spese di notifica
					// Verifica spese di notifica
					if (impNotifica != null && impNotifica.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal residuoNotifica = impVersatoDett.subtract(canonePiuInteressi);

						if (residuoNotifica.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal spNotifica = impNotifica;
							if (residuoNotifica.compareTo(impNotifica) < 0) {
								spNotifica = residuoNotifica;
							}
							Long idBilAccSpese = null;
							BilAccDTO bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, annoPag, null,
									"SPESA_NOTIFICA", annoPag);
							if (bilAcc == null) {
								// se ancora non ho trovato un id bilancio cerco quello piu' vecchio valido
								idBilAccSpese = bilAccDAO
										.loadBilAccValidoPiuVecchio(idAmbito, "SPESA_NOTIFICA", annoPag).getIdBilAcc();
							} else {
								idBilAccSpese = bilAcc.getIdBilAcc();
							}
							if (idBilAccSpese != null) {
								// Se per caso non ha trovato un idBilAcc per spese di notifica allora non
								// inserisce la riga e AMEN!
								insertDettaglioPagBilAcc(attore, flgRuolo, flgPubblico, false,
										dettaglioPag.getIdDettaglioPag(), idBilAccSpese, spNotifica);
							} else {
								return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
										"idBilAcc per SPESE NOTIFICA non trovato - idPagamento = " + idPagamento);
							}
						}
					}
				}

				// Eventuale eccedenza
				if (impVersatoDett.compareTo(impDovutoTot) > 0) {
					Long idBilAccEccedenze = null;
					String codAccBil = isGrandeIdro ? "CAN_GRANDE_IDRO" : "CANONE";
					// Cerco accertamento relativo all'anno del pagamento anno = annoCompetenza =
					// annoPag
					BilAccDTO bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, annoPag, annoPag, codAccBil,
							annoPag);
					if (bilAcc == null) {
						// Provo a cercare solo per annoCompetenza = annoPag
						bilAcc = bilAccDAO.loadBilAccByCodAccertaBilancio(idAmbito, null, annoPag, codAccBil, annoPag);
					}
					if (bilAcc == null) {
						// se ancora non ho trovato un id bilancio cerco quello piu' vecchio valido
						idBilAccEccedenze = bilAccDAO.loadBilAccValidoPiuVecchio(idAmbito, codAccBil, annoPag)
								.getIdBilAcc();
					} else {
						idBilAccEccedenze = bilAcc.getIdBilAcc();
					}

					BigDecimal impEccedenza = impVersatoDett.subtract(impDovutoTot);
					if (idBilAccEccedenze != null) {
						insertDettaglioPagBilAcc(attore, flgRuolo, flgPubblico, true, dettaglioPag.getIdDettaglioPag(),
								idBilAccEccedenze, impEccedenza);
					} else {
						return getErrorResult("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc]",
								"idBilAcc per ECCEDENZA non trovato - idPagamento = " + idPagamento);
					}
				}
			}

			// Fine elaborazione del pagamento
			result.setStatus("OK");
			result.setMessage("Pagamento elaborato con successo");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc] ERROR: Exception - END - " + e.getMessage());
			result.setStatus("KO");
			result.setMessage(e.getMessage());
		}

		LOGGER.debug("[PagamentoBilAccLogic::saveDettaglioPagamentoBilAcc] END");
		return result;
	}

	private List<SommaCanoneTipoUsoSdDTO> ricalcoloCanonePerTipoUso(Long idRataSd,
			List<SommaCanoneTipoUsoSdDTO> listaCanoniPerTipoUso) {
		for (SommaCanoneTipoUsoSdDTO canoneTipoUso : listaCanoniPerTipoUso) {
			try {
				SommaImpAssegnatoTipoUsoSdDTO sommaImpAss = dettaglioPagBilAccDAO.getSommaImpAssegnatoTipoUso(idRataSd,
						canoneTipoUso.getIdAccertaBilancio());
				if (sommaImpAss != null) {
					BigDecimal canoneUsoNew = canoneTipoUso.getTotaleCanoneUso()
							.subtract(sommaImpAss.getImpAssegnato());
					if (canoneUsoNew.compareTo(BigDecimal.ZERO) < 0) {
						canoneUsoNew = BigDecimal.ZERO;
					}
					canoneTipoUso.setTotaleCanoneUso(canoneUsoNew);
				}
			} catch (DAOException e) {
				LOGGER.error("[PagamentoBilAccLogic::ricalcoloCanonePerTipoUso] ERROR " + e);
				return null;
			}
		}
		return listaCanoniPerTipoUso;
	}

	private void insertDettaglioPagBilAcc(String attore, boolean flgRuolo, boolean flgPubblico, boolean flgEccedenza,
			Long idDettaglioPag, Long idBilAcc, BigDecimal importo) throws Exception {
		DettaglioPagBilAccDTO dettPagBilAcc = new DettaglioPagBilAccDTO();
		dettPagBilAcc.setIdDettaglioPag(idDettaglioPag);
		dettPagBilAcc.setIdBilAcc(idBilAcc);
		dettPagBilAcc.setImportoAccertaBilancio(importo);
		dettPagBilAcc.setFlgEccedenza(flgEccedenza ? 1 : 0);
		dettPagBilAcc.setFlgRuolo(flgRuolo ? 1 : 0);
		dettPagBilAcc.setFlgPubblico(flgPubblico ? 1 : 0);
		dettPagBilAcc.setGestAttoreIns(attore);
		dettPagBilAcc.setGestAttoreUpd(attore);
		dettaglioPagBilAccDAO.saveDettaglioPagBilAcc(dettPagBilAcc);
	}

	private boolean checkCodiceBilancioGrandeIdro(List<AnnualitaUsoSdExtendedDTO> listAnnualitaUsi)
			throws SQLException {
		for (AnnualitaUsoSdExtendedDTO annualitaUso : listAnnualitaUsi) {
			TipoUsoExtendedDTO tipoUso = tipoUsoDAO
					.loadTipoUsoByIdTipoUsoOrCodTipoUso("" + annualitaUso.getIdTipoUso());
			if (!tipoUso.getAccertamentoBilancio().getCodAccertaBilancio().equals("CANONE")) {
				return true;
			}
		}
		return false;
	}

	private BilAccResultDTO getErrorResult(String label, String msg) {
		LOGGER.error(label + msg);
		BilAccResultDTO result = new BilAccResultDTO();
		result.setStatus("KO");
		result.setMessage(msg);
		return result;
	}

}
