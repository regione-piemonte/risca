/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.tracciamento;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.helper.LogAuditHelper;
import it.csi.risca.riscabesrv.business.be.impl.BaseApiServiceImpl;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class TracciamentoManagerImpl  extends BaseApiServiceImpl implements TracciamentoManager {
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private LogAuditHelper logAuditHelper;

	/**
	 * Salva il tracciamento di un'operazione.
	 *
	 * @param tracciabile    l'oggetto da tracciare Esempio: AccertamentoExtendedDTO accertamento
	 * @param identita       l'identita digitale
	 * @param idRiscossione  l'ID della riscossione
	 * @param tipoJson       il tipo di JSON Esempio: "JSON TIPO USO REGOLA" ,"JSON SOGGETTO"
	 * @param keyOper        la chiave dell'operazione Esempio: id dell'oggetto da salvare
	 * @param oggOperazione  il nome della tabella da tracciare Esempio: RISCA_T_ACCERTAMENTO
	 * @param flgOperazione  il flag dell'operazione Esempio: I,U,D
	 * @param operazione     l'operazione Esempio: insert, update, delete
	 * @param isLogAudit     flag per indicare se creare un log audit
	 * @param isTracciamento flag per indicare se tracciare l'operazione
	 * @param httpRequest    la richiesta HTTP
	 * @throws Exception in caso di errori durante il riflessione o invocazione del metodo
	 */
	@Override
	public void saveTracciamento(String fruitore, Object  tracciabile, Identita identita, Long idRiscossione, String tipoJson, String keyOper,
			String oggOperazione, String flgOperazione, String operazione, boolean isLogAudit, boolean isTracciamento,
			HttpServletRequest httpRequest) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			String cf= null;
		    if(fruitore != null ){
		    	cf = fruitore;
		    }else {
		    	cf = getCodiceFiscale(tracciabile, identita);
		    }
			if (isLogAudit) {
				if(!cf.equals(Constants.BATCH_PORTING)&&!cf.equals(Constants.BATCH_STATO_CONT)
						&&!cf.equals(Constants.BATCH)&&!cf.equals(Constants.SHELL)&&!cf.equals(Constants.MIGRAZIONE)
						&&!cf.equals(Constants.RISCABATCHARC)) {
				  logAudit(cf, keyOper, oggOperazione, operazione, httpRequest);
				}
				
			}

			if (isTracciamento) {
				if(!cf.equals(Constants.BATCH_PORTING)&&!cf.equals(Constants.BATCH_STATO_CONT)
						&&!cf.equals(Constants.BATCH)&&!cf.equals(Constants.SHELL)&&!cf.equals(Constants.MIGRAZIONE)
						&&!cf.equals(Constants.RISCABATCHARC)) {
			    	logTracciamento(tracciabile, idRiscossione, flgOperazione, tipoJson);
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			e.printStackTrace();
			throw e;
		}

		LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	}

	private String getCodiceFiscale(Object tracciabile, Identita identita) throws Exception {
		Class<?> objectClass = tracciabile.getClass();
        String GestAttoreInsProperty = "GestAttoreIns";
        Method getGestAttoreInsMethod = objectClass.getMethod("get" + GestAttoreInsProperty);
		String gestAttoreInsValue = (String) getGestAttoreInsMethod.invoke(tracciabile);

		return Optional.ofNullable(identita).map(Identita::getCodFiscale)
				.orElse(Optional.ofNullable(gestAttoreInsValue).orElse(""));
	}

	private void logAudit(String cf, String keyOper, String oggOperazione, String operazione,
			HttpServletRequest httpRequest) {
		logAuditHelper.createLogAudit(Constants.RISCA_RP_PROD_RISCABE, httpRequest, keyOper, oggOperazione,
				operazione, cf);
	}

	private void logTracciamento(Object tracciabile, Long idRiscossione, String flgOperazione, String tipoJson) {
		logAuditHelper.createTracciamento(tracciabile, idRiscossione, flgOperazione, tipoJson);
	}

}
