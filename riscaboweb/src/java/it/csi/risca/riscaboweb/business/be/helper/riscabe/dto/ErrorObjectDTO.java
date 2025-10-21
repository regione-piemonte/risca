/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Error dto.
 *
 * @author CSI PIEMONTE
 */
public class ErrorObjectDTO {
    /**
     * Instantiates a new Error dto.
     *
     * @param status the status
     * @param code   the code
     * @param title  the title
     * @param detail the detail
     * @param links  the links
     */

    
    public ErrorObjectDTO() {
		super();
	}

	public ErrorObjectDTO(String status, String code, String title, DetailDTO detail, List<String> links,
			Map<String, String> details) {
		super();
		this.status = status;
		this.code = code;
		this.title = title;
		this.detail = detail;
		this.links = links;
		this.details = details;
	}

	private String status;

    private String code;

    private String title;

    private DetailDTO detail;

    private List<String> links;
    
	private Map<String, String> details = new HashMap<String, String>();
    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets detail.
     *
     * @return the detail
     */
    public DetailDTO getDetail() {
        return detail;
    }

    /**
     * Sets detail.
     *
     * @param detail the detail
     */
    public void setDetail(DetailDTO detail) {
        this.detail = detail;
    }

    /**
     * Gets links.
     *
     * @return the links
     */
    public List<String> getLinks() {
        return links;
    }

    /**
     * Sets links.
     *
     * @param links the links
     */
    public void setLinks(List<String> links) {
        this.links = links;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, detail, links, status, title);
    }

    /**
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorObjectDTO other = (ErrorObjectDTO) obj;
        return Objects.equals(code, other.code) && Objects.equals(detail, other.detail) && Objects.equals(links, other.links) && Objects.equals(status, other.status) && Objects.equals(title, other.title);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorDTO [status=").append(status).append("\n  code=").append(code).append("\n  title=").append(title).append("\n  detail=").append(detail).append("\n  links=").append(links).append("]");
        return builder.toString();
    }


}