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

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ErrorDTO {

    private String status;

    private String code;

    private String title;

    private Map<String,String> detail;

    private List<String> links;

    public ErrorDTO(String status, String code, String title, Map<String, String> detail, List<String> links) {
        this.status = status;
        this.code = code;
        this.title = title;
        this.detail = detail;
        this.links = links;
    }

    public ErrorDTO() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, detail, links, status, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorDTO other = (ErrorDTO) obj;
        return Objects.equals(code, other.code) && Objects.equals(detail, other.detail) && Objects.equals(links, other.links) && Objects.equals(status, other.status) && Objects.equals(title, other.title);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorDTO [status=").append(status).append("\n  code=").append(code).append("\n  title=").append(title).append("\n  detail=").append(detail).append("\n  links=").append(links).append("]");
        return builder.toString();
    }


}