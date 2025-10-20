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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.PagamentoApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagNonPropriDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpPagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.ElaborazionePagamentiResultDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.RpPagopaDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;
import it.csi.risca.riscabesrv.util.mail.MailException;

@Component
public class PagamentoApiServiceImpl extends BaseApiServiceImpl implements PagamentoApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private PagamentoDAO pagamentoDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;

	@Autowired
	private TracciamentoManager tracciamentoManager;

	@Autowired
	private PagNonPropriDAO pagNonPropriDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	public BusinessLogic businessLogic;
	
	@Autowired
	public DettaglioPagBilAccDAO dettaglioPagBilAccDAO;
	
	@Autowired
	public PagamentoBilAccLogic pagamentoBilAccLogic;
	
	@Autowired
	private RpPagopaDAO rpPagopaDAO;
	
	@Autowired
	private ElaborazionePagamenti elaborazionePagamenti;

	@Autowired
	private BusinessLogicManager businessLogicManager;
	
	@Autowired
	private ElaboraDAO elaboraDAO;

	@Override
	public Response getPagamentoWithMaxDataOpVal(String fruitore, Long idStatoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoWithMaxDataOpVal] BEGIN");
		PagamentoDTO pagamento = new PagamentoDTO();
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoWithMaxDataOpVal] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoWithMaxDataOpVal] verificaIdentitaDigitale END");
			pagamento = pagamentoDAO.getPagamentoWithMaxDataOpVal(idStatoDebitorio);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoWithMaxDataOpVal] ERROR: Exception - END", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoWithMaxDataOpVal] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentoByIdRiscossione(String fruitore, Long idRiscossione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoByIdRiscossione] BEGIN");
		List<PagamentoExtendedDTO> pagamento = new ArrayList<PagamentoExtendedDTO>();
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoByIdRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoByIdRiscossione] verificaIdentitaDigitale END");

			pagamento = pagamentoDAO.getPagamentoByIdRiscossione(idRiscossione);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoByIdRiscossione] ERROR: Exception - END", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoByIdRiscossione] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response savePagamento(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] BEGIN");
		PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			setGestAttoreInsUpd(pagamentoExtendedDTO, fruitore, httpRequest, httpHeaders);

			businessLogic.validatorDTO(pagamentoExtendedDTO, null, null);
			LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] verificaIdentitaDigitale END");
			LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			pagamentoExtendedDTO.setIdAmbito(idAmbito);
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			pagamentoExtendedDTO.setAmbito(ambito);
			pagamento = pagamentoDAO.savePagamento(pagamentoExtendedDTO, fruitore);
			// DP aggiungo
			List<DettaglioPagDTO> listDettaglioPag = new ArrayList<DettaglioPagDTO>();
			List<StatoDebitorioExtendedDTO> statoDebitorioList = new ArrayList<>();
			if (pagamentoExtendedDTO.getDettaglioPag() != null) {
				for (DettaglioPagDTO dettaglioPag : pagamentoExtendedDTO.getDettaglioPag()) {
					dettaglioPag.setIdPagamento(pagamento.getIdPagamento());
					try {
						setGestAttoreInsUpd(dettaglioPag, fruitore, httpRequest, httpHeaders);
						businessLogic.validatorDTO(dettaglioPag, null, null);

						if (Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE.equals(pagamento.getIdTipologiaPag())) {
							statoDebitorioList
									.add(statoDebitorioDAO.loadStatoDebitorioByIdRata(dettaglioPag.getIdRataSd()));
						}

						dettaglioPag = dettaglioPagDAO.saveDettaglioPag(dettaglioPag);
						// DP aggiungo per aver l'id dettaglio pag inserito
						listDettaglioPag.add(dettaglioPag);
					} catch (ValidationException ve) {
						throw ve;
					} catch (Exception e) {
						LOGGER.error("[PagamentoApiServiceImpl::savePagamento:: " + e);
						ErrorDTO err = new ErrorDTO("500", "E005",
								"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
						return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

					}
				}
			}

			// updateStatoContribuzione
			if (Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE.equals(pagamento.getIdTipologiaPag()))
				businessLogic.aggiornaStatoContribuzione(statoDebitorioList, idAmbito,
						pagamentoExtendedDTO.getGestAttoreIns(), null);

			// DP aggiungo inizio
			if (listDettaglioPag != null && !listDettaglioPag.isEmpty()) {
				pagamento.setDettaglioPag(listDettaglioPag);
			}
			// DP aggiungo fine

			if (pagamento != null) {
				try {

					// DP SE FRUITORE BATCH_PORTING INSERIRE PAGAMENTI NON PROPRI SE PRESENTI
					if (fruitore != null && fruitore.equals(Constants.BATCH_PORTING)) {
						if (pagamentoExtendedDTO.getPagNonPropri() != null) {
							List<PagNonPropriExtendedDTO> listPagNonPropri = new ArrayList<PagNonPropriExtendedDTO>();
							for (PagNonPropriExtendedDTO pagNonPropri : pagamentoExtendedDTO.getPagNonPropri()) {
								// SETTO ID PAGAMENTO INSERITO
								setGestAttoreInsUpd(pagNonPropri, fruitore, httpRequest, httpHeaders);

								businessLogic.validatorDTO(pagNonPropri, null, null);
								pagNonPropri.setIdPagamento(pagamento.getIdPagamento());
								pagNonPropri = pagNonPropriDAO.savePagNonPropri(pagNonPropri);
								listPagNonPropri.add(pagNonPropri);
							}

							if (listPagNonPropri != null && !listPagNonPropri.isEmpty()) {
								pagamento.setPagNonPropri(listPagNonPropri);
							}
						}

					}

					LOGGER.debug("[PagamentoApiServiceImpl::savePagamento]  BEGIN save tracciamento");

					tracciamentoManager.saveTracciamento(fruitore, pagamento, identita, null,
							"JSON PAGAMENTO",
							pagamento.getIdPagamento() != null
									? pagamento.getIdPagamento().toString()
									: null,
							"RISCA_T_PAGAMENTO", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true,
							true, httpRequest);

					if (Utils.isNotEmpty(pagamentoExtendedDTO.getDettaglioPag())) {
						tracciamentoManager.saveTracciamento(fruitore, pagamento.getDettaglioPag().get(0),
								identita, null, null,
								pagamento.getDettaglioPag().get(0).getIdDettaglioPag() != null
										? pagamento.getDettaglioPag().get(0).getIdDettaglioPag().toString()
										: null,
								"RISCA_R_DETTAGLIO_PAG", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT,
								true, false, httpRequest);
					}

					LOGGER.debug("[PagamentoApiServiceImpl::savePagamento]  END save tracciamento");

				} catch (ValidationException ve) {
					throw ve;
				} catch (Exception e) {
					LOGGER.error("[PagamentoApiServiceImpl::savePagamento:: operazione insert LogAudit ERROR ]: ", e);
					ErrorDTO err = new ErrorDTO("500", "E005",
							"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
					return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

				}

			}
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[PagamentoApiServiceImpl::savePagamento:: operazione insert LogAudit ERROR ]: ", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::savePagamento] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response updatePagamento(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento] BEGIN");
		PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			setGestAttoreInsUpd(pagamentoExtendedDTO, fruitore, httpRequest, httpHeaders);

			businessLogic.validatorDTO(pagamentoExtendedDTO, null, null);

			LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			pagamentoExtendedDTO.setIdAmbito(idAmbito);
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			pagamentoExtendedDTO.setAmbito(ambito);
			try {
				pagamento = pagamentoDAO.updatePagamento(pagamentoExtendedDTO, fruitore);
				if (Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE.equals(pagamento.getIdTipologiaPag())) {
					List<StatoDebitorioExtendedDTO> statoDebitorioList = new ArrayList<>();
					if (pagamentoExtendedDTO.getDettaglioPag() != null) {
						for (DettaglioPagDTO dettaglioPag : pagamentoExtendedDTO.getDettaglioPag()) {
							dettaglioPag.setIdPagamento(pagamento.getIdPagamento());
							try {
								setGestAttoreInsUpd(dettaglioPag, fruitore, httpRequest, httpHeaders);
								businessLogic.validatorDTO(dettaglioPag, null, null);
								statoDebitorioList
										.add(statoDebitorioDAO.loadStatoDebitorioByIdRata(dettaglioPag.getIdRataSd()));

								DettaglioPagExtendedDTO dettaglioPagExtended = new DettaglioPagExtendedDTO();
								dettaglioPagExtended.setIdDettaglioPag(dettaglioPag.getIdDettaglioPag());
								dettaglioPagExtended.setImportoVersato(pagamento.getImportoVersato());
								dettaglioPagExtended.setGestAttoreUpd(dettaglioPag.getGestAttoreUpd());
								dettaglioPag = dettaglioPagDAO.updateDettaglioPag(dettaglioPagExtended);
							} catch (ValidationException ve) {
								throw ve;
							} catch (Exception e) {
								LOGGER.error("[PagamentoApiServiceImpl::savePagamento:: " + e);
								ErrorDTO err = new ErrorDTO("500", "E005",
										"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null,
										null);
								return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus()))
										.build();

							}
						}
					}

					// updateStatoContribuzione
					businessLogic.aggiornaStatoContribuzione(statoDebitorioList, idAmbito,
							pagamentoExtendedDTO.getGestAttoreUpd(), null);
				}

				businessLogic.updatePagamentiNonPropri(pagamentoExtendedDTO.getPagNonPropri(), pagamentoExtendedDTO,
						httpHeaders, httpRequest, fruitore, identita);

				LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento]  BEGIN save tracciamento");

				tracciamentoManager.saveTracciamento(fruitore, pagamentoExtendedDTO, identita, null, "JSON PAGAMENTO",
						pagamentoExtendedDTO.getIdPagamento() != null ? pagamentoExtendedDTO.getIdPagamento().toString()
								: null,
						"RISCA_T_PAGAMENTO", Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true,
						httpRequest);

				if (Utils.isNotEmpty(pagamentoExtendedDTO.getDettaglioPag())) {
					tracciamentoManager.saveTracciamento(fruitore, pagamentoExtendedDTO.getDettaglioPag().get(0),
							identita, null, null,
							pagamentoExtendedDTO.getDettaglioPag().get(0).getIdDettaglioPag() != null
									? pagamentoExtendedDTO.getDettaglioPag().get(0).getIdDettaglioPag().toString()
									: null,
							"RISCA_R_DETTAGLIO_PAG", Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true,
							false, httpRequest);
				}

				if (Utils.isNotEmpty(pagamentoExtendedDTO.getPagNonPropri())) {
					tracciamentoManager.saveTracciamento(fruitore, pagamentoExtendedDTO.getPagNonPropri().get(0),
							identita, null, null,
							pagamentoExtendedDTO.getPagNonPropri().get(0).getIdPagNonPropri() != null
									? pagamentoExtendedDTO.getPagNonPropri().get(0).getIdPagNonPropri().toString()
									: null,
							"RISCA_R_PAG_NON_PROPRI", Constants.FLG_OPERAZIONE_UPDATE, Constants.FLG_OPERAZIONE_UPDATE,
							true, false, httpRequest);
				}

				LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento]  END save tracciamento");
			} catch (ValidationException ve) {

				throw ve;

			} catch (Exception e) {
				LOGGER.error("[PagamentoApiServiceImpl::updatePagamento:: operazione insert LogAudit ERROR ]: ", e);
				ErrorDTO err = new ErrorDTO("500", "E005",
						"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[PagamentoApiServiceImpl::updatePagamento:: ERROR ]: ", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::updatePagamento] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteByIdPagamento(Long idPagamento, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::deleteByIdPagamento] BEGIN");
		LOGGER.debug("[PagamentoApiServiceImpl::deleteByIdPagamento] Parametro in input idPagamento :" + idPagamento);
		Long idPag;
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::deleteByIdPagamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::deleteByIdPagamento] verificaIdentitaDigitale END");

			PagamentoExtendedDTO pagamentoExtendedDTO = pagamentoDAO.getPagamentoByIdPagamento(idPagamento);
			List<StatoDebitorioExtendedDTO> statoDebitorioList = new ArrayList<>();
			List<Long> listIdRataSd = new ArrayList<Long>();
			for (DettaglioPagDTO dettaglioPag : pagamentoExtendedDTO.getDettaglioPag()) {
				if (Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE.equals(pagamentoExtendedDTO.getIdTipologiaPag())) {
					statoDebitorioList.add(statoDebitorioDAO.loadStatoDebitorioByIdRata(dettaglioPag.getIdRataSd()));
					listIdRataSd.add(dettaglioPag.getIdRataSd());
				}
			}
			
			List<Long> listaAltriIdPag = deleteDettaglioPagBilAcc(idPagamento, listIdRataSd);
			
			idPag = pagamentoDAO.deleteByIdPagamento(idPagamento);
			
			if (Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE.equals(pagamentoExtendedDTO.getIdTipologiaPag())) {
				Identita identita = IdentitaDigitaleManager
						.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
				Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				businessLogic.aggiornaStatoContribuzione(statoDebitorioList, idAmbito,
						pagamentoExtendedDTO.getGestAttoreIns(), null);
				insertDettaglioPagBilAcc(fruitore, listaAltriIdPag, httpHeaders);
				
				
				tracciamentoManager.saveTracciamento(fruitore, pagamentoExtendedDTO, identita, null, "JSON PAGAMENTO",
						idPagamento != null ? idPagamento.toString(): null,
						"RISCA_T_PAGAMENTO", Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, true, true,
						httpRequest);
				
			}	
			
			

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[PagamentoApiServiceImpl::deleteByIdPagamento:: ERROR ]: " + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(idPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private void insertDettaglioPagBilAcc(String fruitore, List<Long> listaAltriIdPag, HttpHeaders httpHeaders) {
		String attore = null;
		if (fruitore != null) {
			attore = fruitore;
		} else {
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);
			attore = identita.getCodFiscale();
		}
		for (Long idPag : listaAltriIdPag) {
			pagamentoBilAccLogic.saveDettaglioPagamentoBilAcc(attore, idPag);
		}
	}

	private List<Long> deleteDettaglioPagBilAcc(Long idPagamento, List<Long> listIdRataSd) throws DAOException, Exception {
		// cancello da dettaglioPAgBilAcc i dettagli relativi all'idPagamento considerato
		dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByIdPagamento(idPagamento);
		List<Long> listIdPag = new ArrayList<Long>();
		// Elimino anche i dettagliPagBilAcc relativi a dettagli di altri pagamenti per la stessa rata
		if(listIdRataSd.size()>0) {
			// Prima di fare la delete per idRata determino quali sono gli idPag per i quali
			// dovro' andare eventualmente a reinserire i dettagli di pagamento bil acc
			for (Long idRataSd : listIdRataSd) {	
				listIdPag.addAll(dettaglioPagDAO.getDettaglioPagByIdRate(idRataSd).stream()
						.map(dp -> dp.getIdPagamento()).collect(Collectors.toList()));
			}
			// Rimuovo i duplicati dalla lista degli idPag
			listIdPag = listIdPag.stream().distinct().collect(Collectors.toList());
			// Rimuovo dalla lista l'idPagamento oggetto della delete
			listIdPag.remove(idPagamento);
			
			dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByListaIdRataSd(listIdRataSd);
		}
		return listIdPag;
	}

	@Override
	public Response getPagamentiByIdStatoDebitorio(String fruitore, Long idStatoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] BEGIN");
		List<PagamentoExtendedDTO> listPagamenti = new ArrayList<PagamentoExtendedDTO>();
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] verificaIdentitaDigitale END");
			listPagamenti = pagamentoDAO.getPagamentiByIdStatoDebitorio(idStatoDebitorio);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] ERROR: Exception - END" + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] END");
		return Response.ok(listPagamenti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentoByIdPagamento(Long idPagamento, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] BEGIN");
		PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] verificaIdentitaDigitale END");
			pagamento = pagamentoDAO.getPagamentoByIdPagamento(idPagamento);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] ERROR: Exception - END", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentiByIdStatoDebitorio] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveDettaglioPagamento(DettaglioPagDTO dettaglioPagDTO, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento] BEGIN");

		DettaglioPagDTO dettaglioPagamento = new DettaglioPagDTO();
		try {
			setGestAttoreInsUpd(dettaglioPagDTO, fruitore, httpRequest, httpHeaders);

			businessLogic.validatorDTO(dettaglioPagDTO, null, null);
			LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento] verificaIdentitaDigitale END");

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			try {
				dettaglioPagamento = dettaglioPagDAO.saveDettaglioPag(dettaglioPagDTO);
			} catch (Exception e) {
				LOGGER.error("[PagamentoApiServiceImpl::saveDettaglioPagamento:: " + e);
				ErrorDTO err = new ErrorDTO("500", "E005",
						"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

			}

			if (dettaglioPagamento != null) {
				try {
					LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento]  BEGIN save tracciamento");

					tracciamentoManager.saveTracciamento(fruitore, dettaglioPagamento, identita, null, null,
							dettaglioPagamento.getIdDettaglioPag() != null ? dettaglioPagamento.getIdDettaglioPag().toString()
									: null,
							"RISCA_R_DETTAGLIO_PAG", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true,
							false, httpRequest);

					LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento]  END save tracciamento");

				} catch (Exception e) {
					LOGGER.error(
							"[PagamentoApiServiceImpl::saveDettaglioPagamento:: operazione insert LogAudit ERROR ]: "
									+ e);
					ErrorDTO err = new ErrorDTO("500", "E005",
							"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
					return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

				}

			}
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[PagamentoApiServiceImpl::saveDettaglioPagamento:: ERROR ]: " + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::saveDettaglioPagamento] END");
		return Response.ok(dettaglioPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentiDaVisionare(String fruitore, Integer offset, Integer limit, String sort,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoDaVisionare] BEGIN");
		List<PagamentoExtendedDTO> listPagamenti = new ArrayList<PagamentoExtendedDTO>();
		String jsonString = null;
		try {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoDaVisionare] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoDaVisionare] verificaIdentitaDigitale END");
			listPagamenti = pagamentoDAO.getPagamentiDaVisionare(offset, limit, sort);
			Integer countPagamentiDaVisionare = pagamentoDAO.countPagamentiDaVisionare();
			PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
			paginationHeader.setTotalElements(countPagamentiDaVisionare);
			paginationHeader.setTotalPages(
					(countPagamentiDaVisionare / limit) + ((countPagamentiDaVisionare % limit) == 0 ? 0 : 1));
			paginationHeader.setPage(offset);
			paginationHeader.setPageSize(limit);
			paginationHeader.setSort(sort);
			JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			jsonString = jsonPaginationHeader.toString();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoDaVisionare] ERROR: Exception - END" + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::getPagamentoDaVisionare] END");
		return Response.ok(listPagamenti).header("PaginationInfo", jsonString)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getEsistenzaPagamento(String fruitore, String dataOpVal, BigDecimal importoVersato, Long idFileSoris,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamento] BEGIN");
		    List<Long> idPagamento = null;

			try {			    
			    LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamento] getEsistenzaPagamento BEGIN");
			    idPagamento = pagamentoDAO.countEsistenzaPagamentoPerSoris(dataOpVal,importoVersato, idFileSoris);
			    LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamento] getEsistenzaPagamento END");
			}
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			} 
	        LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamento] END");
	        return Response.ok(idPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response getEsistenzaPagamentoDaAnnullare(String fruitore, String dataOpVal, Long idPagamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamentoDaAnnullare] BEGIN");
		    Integer countEsistenza = null;

			try {				   
			    LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamentoDaAnnullare] getEsistenzaPagamento BEGIN");
			    countEsistenza = pagamentoDAO.countEsistenzaPagamentoDaAnnullarePerSoris(dataOpVal,idPagamento);
			    LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamentoDaAnnullare] getEsistenzaPagamento END");
			}
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			} 
	        LOGGER.debug("[PagamentoApiServiceImpl::getEsistenzaPagamentoDaAnnullare] END");
	        return Response.ok(countEsistenza).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updatePagamentoPerAnnullamentoSoris(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore, Long idFileSoris,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::updatePagamentoPerAnnullamentoSoris] BEGIN");
		PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			setGestAttoreInsUpd(pagamentoExtendedDTO, fruitore, httpRequest, httpHeaders);

			businessLogic.validatorDTO(pagamentoExtendedDTO, null, null);
	
			pagamento = pagamentoDAO.updateAnnullamentoPagamentoSoris(pagamentoExtendedDTO, fruitore, idFileSoris);
			
			LOGGER.debug("[PagamentoApiServiceImpl::updatePagamentoPerAnnullamentoSoris]  END ");
			
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[PagamentoApiServiceImpl::updatePagamentoPerAnnullamentoSoris:: ERROR ]: ", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::updatePagamentoPerAnnullamentoSoris] END");
		return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response recuperaPagamentoPagopa(@RequestBody List<Long> listaIdElabora, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoApiServiceImpl::recuperaPagamentoPagopa] BEGIN");
		List<ElaborazionePagamentiResultDTO> result = new ArrayList<ElaborazionePagamentiResultDTO>();
		try {
			for (Long idElabora : listaIdElabora) {
				Long idAmbito = elaboraDAO.loadElaboraById(idElabora, false).getAmbito().getIdAmbito();

				List<RpPagopaDTO> listaPagopa = rpPagopaDAO.loadRpPagopaByElabora(idElabora);
				if (listaPagopa.size() == 0) {
					LOGGER.error(
							"[PagamentoApiServiceImpl::recuperaPagamentoPagopa] - Non ci sono pagamenti su risca_w_rp_pagopa per l'elaborazione: "
									+ idElabora);
				} else {
					ElaborazionePagamentiResultDTO elabResult = elaborazionePagamenti
							.elaboraPagamentiPagopa(listaPagopa, idElabora);
					result.add(elabResult);

					// Aggiornamento stato contribuzione
					List<StatoDebitorioExtendedDTO> sdList = elabResult.getSdList();
					if (sdList != null && sdList.size() > 0) {
						int status = businessLogicManager.aggiornaStatoContribuzione(sdList, idAmbito, fruitore,
								idElabora);
						if (status == 1) {
							List<Long> listIdStatoDeb = sdList.stream().map(dto -> dto.getIdStatoDebitorio()).distinct()
									.collect(Collectors.toList());
							Long[] arrayIdStatoDeb = listIdStatoDeb.toArray(new Long[listIdStatoDeb.size()]);
							LOGGER.debug(
									" [PagamentoApiServiceImpl::recuperaPagamentoPagopa] - Aggiornamento stato contribuzione fallito per stati debitori con id: "
											+ arrayIdStatoDeb);
						}
					}
				}
			}
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoApiServiceImpl::recuperaPagamentoPagopa] ERROR: Exception - END", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoApiServiceImpl::recuperaPagamentoPagopa] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
