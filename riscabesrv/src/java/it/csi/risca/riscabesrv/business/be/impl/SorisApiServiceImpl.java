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
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.SorisApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.FileSorisDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr3DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr7DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris00CDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris99CDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr0DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr1DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr3DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr7DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisTotDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;
import it.csi.risca.riscabesrv.dto.soris.ElaborazioneSorisResultDTO;
import it.csi.risca.riscabesrv.dto.soris.FileSorisDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr7DTO;
import it.csi.risca.riscabesrv.dto.soris.Soris00CDTO;
import it.csi.risca.riscabesrv.dto.soris.Soris99CDTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr0DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr7DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisTotDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Soris api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class SorisApiServiceImpl extends BaseApiServiceImpl implements SorisApi {

	private static final String IDENTITY = "identity";
	// private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private FileSorisDAO fileSorisDAO;

	@Autowired
	private SorisTotDAO sorisTotDAO;
	
	@Autowired
	private Soris00CDAO soris00CDAO;
	
	@Autowired
	private SorisFr0DAO sorisFr0DAO;

	@Autowired
	private SorisFr1DAO sorisFr1DAO;
	
	@Autowired
	private SorisFr3DAO sorisFr3DAO;
	
	@Autowired
	private SorisFr7DAO sorisFr7DAO;
	
	@Autowired
	private Soris99CDAO soris99CDAO;
	
	@Autowired
	private RuoloSorisFr3DAO ruoloSorisFr3DAO;
	
	@Autowired
	private ElaborazioneSoris elaborazioneSoris;

	@Autowired
	public BusinessLogic businessLogic;	
	
	@Autowired
	private RuoloSorisFr7DAO ruoloSorisFr7DAO;
	
	
	
	@Override
	public Response saveFileSoris(FileSorisDTO fileSoris, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
		setGestAttoreInsUpd(fileSoris, fruitore, httpRequest, httpHeaders);
		businessLogic.validatorDTO(fileSoris, null, null);
		FileSorisDTO dto = fileSorisDAO.saveFileSoris(fileSoris);
		return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
	  } catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} 	
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
      }
	}

	@Override
	public Response saveSorisTot(SorisTotDTO sorisTot, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(sorisTot, null, null);
			SorisTotDTO dto = sorisTotDAO.saveSorisTot(sorisTot);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response saveSorisWorking00C(Soris00CDTO soris00C, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(soris00C, null, null);
			Soris00CDTO dto = soris00CDAO.saveSoris00C(soris00C);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	@Override
	public Response saveSorisWorkingFr0(SorisFr0DTO sorisFr0, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(sorisFr0, null, null);
			SorisFr0DTO dto = sorisFr0DAO.saveSorisFr0(sorisFr0);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response saveSorisWorkingFr1(SorisFr1DTO sorisFr1, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(sorisFr1, null, null);
			SorisFr1DTO dto = sorisFr1DAO.saveSorisFr1(sorisFr1);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	@Override
	public Response saveSorisWorkingFr3(SorisFr3DTO sorisFr3, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(sorisFr3, null, null);
			SorisFr3DTO dto = sorisFr3DAO.saveSorisFr3(sorisFr3);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	@Override
	public Response saveSorisWorkingFr7(SorisFr7DTO sorisFr7, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(sorisFr7, null, null);
			SorisFr7DTO dto = sorisFr7DAO.saveSorisFr7(sorisFr7);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response saveSorisWorking99C(Soris99CDTO soris99c, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(soris99c, null, null);
			Soris99CDTO dto = soris99CDAO.saveSoris99C(soris99c);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}


	@Override
	public Response getSorisWorkingFr1(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	  LOGGER.debug("[SorisApiServiceImpl::getSorisWorkingFr1] BEGIN");
      List<SorisFr1DTO> listaFr1;
		try {
			listaFr1 = sorisFr1DAO.loadSorisFr1();
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
          return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
     
      LOGGER.debug("[SorisApiServiceImpl::getSorisWorkingFr1] END");
      return Response.ok(listaFr1).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response inserisciRuoloSorisFr1(RuoloSorisFr1DTO ruoloSorisFr1, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(ruoloSorisFr1, null, null);
			ElaborazioneSorisResultDTO result = elaborazioneSoris.elaboraRuoloSorisFr1(ruoloSorisFr1);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response inserisciRuoloSorisFr3(RuoloSorisFr3DTO ruoloSorisFr3, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(ruoloSorisFr3, null, null);
			boolean result = elaborazioneSoris.elaboraRuoloSorisFr3(ruoloSorisFr3);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	
//	@Override
//	public Response inserisciPagamentoSoris(RpEstrcoDTO rpEstrco, SecurityContext securityContext,
//			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			businessLogic.validatorDTO(rpEstrco, null, null);
//			boolean result = elaborazioneSoris.elaboraPagamentoSoris(rpEstrco);
//			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
//			
//		  } catch (ValidationException ve) {
//				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
//				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
//				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
//				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
//			} 	
//			catch (BusinessException be) {
//				return handleBusinessException(be.getHttpStatus(), be);
//			} catch (Exception e) {
//				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
//	      }
//	}
	
	@Override
	public Response getEstrazioneDatiSorisPerInsertPagamenti(Long idElabora, Long idFileSoris,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RuoloSorisFr3DTO> listaRuoloFr3 = ruoloSorisFr3DAO.loadEstrazioneDatiSorisPerInsertPagamenti(idElabora,
				idFileSoris);
		return Response.ok(listaRuoloFr3).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
	}

	@Override
	public Response aggiornaRuoloSorisFr3(String fruitore, RuoloSorisFr3DTO ruoloSorisFr3, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SorisApiServiceImpl::aggiornaRuoloSorisFr3] BEGIN");
		LOGGER.debug(
				"[SorisApiServiceImpl::aggiornaRuoloSorisFr3] Parametro in input ruoloSorisFr3 :\n " + ruoloSorisFr3);
		try {
			businessLogic.validatorDTO(ruoloSorisFr3, null, null);
			ruoloSorisFr3.setGestAttoreUpd(fruitore);
System.out.println("gest attore upd--> "+ruoloSorisFr3.getGestAttoreUpd());
			ruoloSorisFr3DAO.updateRuoloSorisFr3(ruoloSorisFr3);
			return Response.ok(ruoloSorisFr3).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		}catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response countFileSoris(String nomeFileSoris, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Integer countFileSoris = 0;
		try {
			countFileSoris = fileSorisDAO.countFileSorisByNomeFile(nomeFileSoris);
			return Response.ok(countFileSoris).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	@Override
	public Response inserisciRuoloSorisFr7(RuoloSorisFr7DTO ruoloSorisFr7, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(ruoloSorisFr7, null, null);
			boolean result = elaborazioneSoris.elaboraRuoloSorisFr7(ruoloSorisFr7);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}
	
	@Override
	public Response getEstrazioneDatiSorisPerInsertAnnullamenti(Long idElabora, Long idFileSoris,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RuoloSorisFr7DTO> listaRuoloFr7 = ruoloSorisFr7DAO.loadEstrazioneDatiSorisPerInsertAnnullamenti(idElabora,
				idFileSoris);
		return Response.ok(listaRuoloFr7).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
	}
	
	@Override
	public Response deleteWorkingSoris(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Integer result = null;
		try {
			result = businessLogic.deleteWorkingSoris();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	    }
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
}
