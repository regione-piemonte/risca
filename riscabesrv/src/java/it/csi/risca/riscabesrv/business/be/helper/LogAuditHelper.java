/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.risca.riscabesrv.business.be.impl.dao.CsiLogAuditDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TracciamentoDAO;
import it.csi.risca.riscabesrv.dto.CsiLogAuditDTO;
import it.csi.risca.riscabesrv.dto.TracciamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class LogAuditHelper {
	
    protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".business");

    
	@Autowired
	private CsiLogAuditDAO csiLogAuditDAO;
	
	@Autowired
	private TracciamentoDAO tracciamentoDAO;
	
	public void saveCsiLogAudit(CsiLogAuditDTO csiLogAuditDTO) {
		
		try {
			LOGGER.debug("saveCsiLogAudit INIZIO---");
			csiLogAuditDAO.saveCsiLogAudit(csiLogAuditDTO);
			LOGGER.debug("saveCsiLogAudit FINE---");
		} catch (Exception e) {
            LOGGER.error("[LogAuditHelper::saveCsiLogAudit] EXCEPTION : " , e);
			throw e;
		}
		
	}
	
	public void saveTracciamento(TracciamentoDTO trac) {
		
		try {
			tracciamentoDAO.saveTracciamento(trac);
		} catch (Exception e) {
            LOGGER.error("[LogAuditHelper::saveTracciamento] EXCEPTION : " , e);
			throw e;
		}
		
	}
	
	public void createLogAudit(String idApp, HttpServletRequest request, String keyOper, String oggOperazione, String operazione, String utente) {
		CsiLogAuditDTO log = new CsiLogAuditDTO();
	    log.setIdApp(idApp);
	    log.setIpAddress(getClientIpAddr(request));
	    log.setKeyOper(keyOper);
	    log.setOggOper(oggOperazione);
	    log.setOperazione(operazione);
	    log.setUtente(utente);
	    saveCsiLogAudit(log);
	}
	
	public <T> void createTracciamento(T dto,Long idRiscosssione,String flgOperazione, String nomeTipoJson) {
	    JSONObject jsonObject = new JSONObject(dto);
	    String jsonString = jsonObject.toString();

	    TracciamentoDTO track = new TracciamentoDTO();
	    track.setFlgOperazione(flgOperazione);
	    track.setIdRiscossione(idRiscosssione);
	    track.setJsonTracciamento(jsonString);
	    track.setTipoJson(nomeTipoJson); 

	    saveTracciamento(track);
	}
	
	public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = null;
		try {
			ip = request.getHeader("X-Forwarded-For");  
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			    ip = request.getHeader("Proxy-Client-IP");  
			} 
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			    ip = request.getHeader("WL-Proxy-Client-IP");  
			}  
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			    ip = request.getHeader("HTTP_CLIENT_IP");  
			}  
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			    ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
			}  
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			    ip = request.getRemoteAddr();  
			}
		} catch (Exception e) {			
			LOGGER.error("[LogAuditHelper::getClientIpAddr] EXCEPTION : " , e);
			e.printStackTrace();
		}  
        return ip;  
    } 
	

}
