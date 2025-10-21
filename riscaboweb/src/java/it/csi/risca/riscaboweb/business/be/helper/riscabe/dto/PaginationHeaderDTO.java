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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The type Pagination header dto.
 *
 * @author CSI PIEMONTE
 */
public class PaginationHeaderDTO {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("total_elements")
    private Integer totalElements;

    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("sort")
    private String sort;
    
    /**
     * Gets page.
     *
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets total elements.
     *
     * @return the total elements
     */
    public Integer getTotalElements() {
        return totalElements;
    }

    /**
     * Sets total elements.
     *
     * @param totalElements the total elements
     */
    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets total pages.
     *
     * @return the total pages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * Sets total pages.
     *
     * @param totalPages the total pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    

    public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(page, pageSize, totalElements, totalPages);
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
        PaginationHeaderDTO other = (PaginationHeaderDTO) obj;
        return Objects.equals(page, other.page) && Objects.equals(pageSize, other.pageSize) && Objects.equals(totalElements, other.totalElements) && Objects.equals(totalPages, other.totalPages);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaginationHeaderDTO {\n    page: ").append(page).append("\n    pageSize: ").append(pageSize).append("\n    totalElements: ").append(totalElements).append("\n    totalPages: ").append(totalPages).append("\n}");
        return builder.toString();
    }

    /**
     * Gets map.
     *
     * @return Map<String, Integer> map
     */
    public Map<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", this.page);
        map.put("page_size", this.pageSize);
        map.put("total_elements", this.totalElements);
        map.put("total_pages", this.totalPages);
        map.put("sort", this.sort);
        return map;
    }

}