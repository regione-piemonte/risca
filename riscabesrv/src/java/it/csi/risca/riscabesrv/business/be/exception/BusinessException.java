/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Business exception.
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -2631969419398877860L;

	private Integer httpstatus = 400;
	private String message = null;
	private String messageCode = null;
	private Map<String, String> detail = new HashMap<String, String>();
	


    public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
     * Sets message/message_code.
     *
     * @param message the message
     */
    public void setMessage(Integer httpstatus, String messageCode, String message, Map<String, String> detail) {
    	this.httpstatus = httpstatus;
		this.message = message;
		this.messageCode = messageCode;
		this.detail = detail;
	}

    public Integer getHttpStatus() {
		return this.httpstatus;
	}

    
    @Override
    public String getMessage() {
		return this.message;
	}

    public String getMessageCode() {
		return this.messageCode;
	}





	public Map<String, String> getDetail() {
		return detail;
	}

	public void setDetail(Map<String, String> detail) {
		this.detail = detail;
	}

	/**
     * Instantiates a new Business exception.
     *
     * @param message the message
     */
    public BusinessException(String messageCode) {
		super();
		setMessage(messageCode);
	}
	
    /**
     * Instantiates a new Business exception.
     * @param httpstatus TODO
     * @param message the message
     */
    public BusinessException(Integer httpstatus, String message_code, String message) {
		super();
		setMessage(httpstatus, message_code, message, null);
	}
	
    /**
     * Instantiates a new Business exception.
     * @param httpstatus TODO
     * @param message the message
     */
    public BusinessException(Integer httpstatus, String message_code, String message, Map<String, String> details) {
		super();
		setMessage(httpstatus, message_code, message, details);
	}
	
	

}