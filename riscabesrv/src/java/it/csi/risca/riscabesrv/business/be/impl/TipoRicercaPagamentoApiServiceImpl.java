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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.TipoRicercaPagamentiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaPagamentoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.RicercaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaPagamentoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TipoRicercaPagamentoApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaPagamentoApiServiceImpl extends BaseApiServiceImpl implements TipoRicercaPagamentiApi {

	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipoRicercaPagamentoDAO tipoRicercaPagamentoDAO;
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager;

	@Override
	public Response loadAllTipiRicercaPagamenti(HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::loadAllTipoRicercaMorosita] BEGIN");
        List<TipoRicercaPagamentoDTO> tipoRicercaPagamentiList = new ArrayList<>();
		try {
			tipoRicercaPagamentiList = tipoRicercaPagamentoDAO.loadAllTipiRicercaPagamenti();
		}catch (Exception e) {
			buildErrorRepsonse(500, "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.",null) ;
		}
		LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::ricercaMorosita] END");
       return Response.ok(tipoRicercaPagamentiList).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response ricercaPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO, String fruitore, Integer offset,
			Integer limit, String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::ricercaPagamenti] BEGIN");
		 String jsonString = null;
		 List<PagamentoExtendedDTO> pagamenti = new ArrayList<>();
		 try {
			    LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::ricercaPagamenti] verificaIdentitaDigitale BEGIN");
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);   
			    LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::ricercaPagamenti] verificaIdentitaDigitale END");

			    pagamenti = tipoRicercaPagamentoDAO.ricercaPagamenti(ricercaPagamentoDTO,offset, limit, sort);
			    Integer numberAllpagamenti = tipoRicercaPagamentoDAO.countPagamenti(ricercaPagamentoDTO);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllpagamenti);
			    paginationHeader.setTotalPages((numberAllpagamenti / limit) + ((numberAllpagamenti % limit) == 0 ? 0 : 1));
			    paginationHeader.setPage(offset);
			    paginationHeader.setPageSize(limit);
			    paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			    jsonString =  jsonPaginationHeader.toString();   
			    
		}
		catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
		} 
		catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[TipoRicercaPagamentoApiServiceImpl::ricercaPagamenti] END");
       return Response.ok(pagamenti)
       		.header("PaginationInfo", jsonString)
       		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}





}
