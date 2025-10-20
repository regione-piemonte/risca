/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

public class TableColumnDTO {

    private String columnName;
    private String dataType;
    private Integer characterMaximumLength;
    private Integer numericPrecision;
    private Integer numericScale;
    private String maxValue;


    public TableColumnDTO(String columnName, String dataType, Integer characterMaximumLength,
                          Integer numericPrecision, Integer numericScale, String maxValue) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.characterMaximumLength = characterMaximumLength;
        this.numericPrecision = numericPrecision;
        this.numericScale = numericScale;
        this.maxValue = maxValue;
    }


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public Integer getCharacterMaximumLength() {
		return characterMaximumLength;
	}


	public void setCharacterMaximumLength(Integer characterMaximumLength) {
		this.characterMaximumLength = characterMaximumLength;
	}


	public Integer getNumericPrecision() {
		return numericPrecision;
	}


	public void setNumericPrecision(Integer numericPrecision) {
		this.numericPrecision = numericPrecision;
	}


	public Integer getNumericScale() {
		return numericScale;
	}


	public void setNumericScale(Integer numericScale) {
		this.numericScale = numericScale;
	}


	public String getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

}

