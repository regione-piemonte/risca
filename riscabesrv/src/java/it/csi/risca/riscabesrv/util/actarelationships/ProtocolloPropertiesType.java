/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
/**
 * ProtocolloPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class ProtocolloPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.CommonPropertiesType  implements java.io.Serializable {
    private java.lang.String objectTypeId;

    private java.lang.String numRegistrazioneProtocollo;

    private java.util.Date dataRegistrazioneProtocollo;

    private java.lang.String oggetto;

    private boolean entrata;

    private java.lang.String AOOCheRegistra;

    private java.lang.String enteCheRegistra;

    private java.lang.String[] mittente;

    private java.lang.String[] destinatario;

    private java.lang.String segnaturaInternaXML;

    private java.lang.String segnaturaEsternaXML;

    private boolean riservato;

    private boolean annullato;

    private java.lang.String uuidRegistrazioneProtocollo;

    public ProtocolloPropertiesType() {
    }

    public ProtocolloPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String numRegistrazioneProtocollo,
           java.util.Date dataRegistrazioneProtocollo,
           java.lang.String oggetto,
           boolean entrata,
           java.lang.String AOOCheRegistra,
           java.lang.String enteCheRegistra,
           java.lang.String[] mittente,
           java.lang.String[] destinatario,
           java.lang.String segnaturaInternaXML,
           java.lang.String segnaturaEsternaXML,
           boolean riservato,
           boolean annullato,
           java.lang.String uuidRegistrazioneProtocollo) {
        super(
            objectId,
            changeToken);
        this.objectTypeId = objectTypeId;
        this.numRegistrazioneProtocollo = numRegistrazioneProtocollo;
        this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
        this.oggetto = oggetto;
        this.entrata = entrata;
        this.AOOCheRegistra = AOOCheRegistra;
        this.enteCheRegistra = enteCheRegistra;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.segnaturaInternaXML = segnaturaInternaXML;
        this.segnaturaEsternaXML = segnaturaEsternaXML;
        this.riservato = riservato;
        this.annullato = annullato;
        this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
    }


    /**
     * Gets the objectTypeId value for this ProtocolloPropertiesType.
     * 
     * @return objectTypeId
     */
    public java.lang.String getObjectTypeId() {
        return objectTypeId;
    }


    /**
     * Sets the objectTypeId value for this ProtocolloPropertiesType.
     * 
     * @param objectTypeId
     */
    public void setObjectTypeId(java.lang.String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }


    /**
     * Gets the numRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @return numRegistrazioneProtocollo
     */
    public java.lang.String getNumRegistrazioneProtocollo() {
        return numRegistrazioneProtocollo;
    }


    /**
     * Sets the numRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @param numRegistrazioneProtocollo
     */
    public void setNumRegistrazioneProtocollo(java.lang.String numRegistrazioneProtocollo) {
        this.numRegistrazioneProtocollo = numRegistrazioneProtocollo;
    }


    /**
     * Gets the dataRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @return dataRegistrazioneProtocollo
     */
    public java.util.Date getDataRegistrazioneProtocollo() {
        return dataRegistrazioneProtocollo;
    }


    /**
     * Sets the dataRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @param dataRegistrazioneProtocollo
     */
    public void setDataRegistrazioneProtocollo(java.util.Date dataRegistrazioneProtocollo) {
        this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
    }


    /**
     * Gets the oggetto value for this ProtocolloPropertiesType.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this ProtocolloPropertiesType.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the entrata value for this ProtocolloPropertiesType.
     * 
     * @return entrata
     */
    public boolean isEntrata() {
        return entrata;
    }


    /**
     * Sets the entrata value for this ProtocolloPropertiesType.
     * 
     * @param entrata
     */
    public void setEntrata(boolean entrata) {
        this.entrata = entrata;
    }


    /**
     * Gets the AOOCheRegistra value for this ProtocolloPropertiesType.
     * 
     * @return AOOCheRegistra
     */
    public java.lang.String getAOOCheRegistra() {
        return AOOCheRegistra;
    }


    /**
     * Sets the AOOCheRegistra value for this ProtocolloPropertiesType.
     * 
     * @param AOOCheRegistra
     */
    public void setAOOCheRegistra(java.lang.String AOOCheRegistra) {
        this.AOOCheRegistra = AOOCheRegistra;
    }


    /**
     * Gets the enteCheRegistra value for this ProtocolloPropertiesType.
     * 
     * @return enteCheRegistra
     */
    public java.lang.String getEnteCheRegistra() {
        return enteCheRegistra;
    }


    /**
     * Sets the enteCheRegistra value for this ProtocolloPropertiesType.
     * 
     * @param enteCheRegistra
     */
    public void setEnteCheRegistra(java.lang.String enteCheRegistra) {
        this.enteCheRegistra = enteCheRegistra;
    }


    /**
     * Gets the mittente value for this ProtocolloPropertiesType.
     * 
     * @return mittente
     */
    public java.lang.String[] getMittente() {
        return mittente;
    }


    /**
     * Sets the mittente value for this ProtocolloPropertiesType.
     * 
     * @param mittente
     */
    public void setMittente(java.lang.String[] mittente) {
        this.mittente = mittente;
    }

    public java.lang.String getMittente(int i) {
        return this.mittente[i];
    }

    public void setMittente(int i, java.lang.String _value) {
        this.mittente[i] = _value;
    }


    /**
     * Gets the destinatario value for this ProtocolloPropertiesType.
     * 
     * @return destinatario
     */
    public java.lang.String[] getDestinatario() {
        return destinatario;
    }


    /**
     * Sets the destinatario value for this ProtocolloPropertiesType.
     * 
     * @param destinatario
     */
    public void setDestinatario(java.lang.String[] destinatario) {
        this.destinatario = destinatario;
    }

    public java.lang.String getDestinatario(int i) {
        return this.destinatario[i];
    }

    public void setDestinatario(int i, java.lang.String _value) {
        this.destinatario[i] = _value;
    }


    /**
     * Gets the segnaturaInternaXML value for this ProtocolloPropertiesType.
     * 
     * @return segnaturaInternaXML
     */
    public java.lang.String getSegnaturaInternaXML() {
        return segnaturaInternaXML;
    }


    /**
     * Sets the segnaturaInternaXML value for this ProtocolloPropertiesType.
     * 
     * @param segnaturaInternaXML
     */
    public void setSegnaturaInternaXML(java.lang.String segnaturaInternaXML) {
        this.segnaturaInternaXML = segnaturaInternaXML;
    }


    /**
     * Gets the segnaturaEsternaXML value for this ProtocolloPropertiesType.
     * 
     * @return segnaturaEsternaXML
     */
    public java.lang.String getSegnaturaEsternaXML() {
        return segnaturaEsternaXML;
    }


    /**
     * Sets the segnaturaEsternaXML value for this ProtocolloPropertiesType.
     * 
     * @param segnaturaEsternaXML
     */
    public void setSegnaturaEsternaXML(java.lang.String segnaturaEsternaXML) {
        this.segnaturaEsternaXML = segnaturaEsternaXML;
    }


    /**
     * Gets the riservato value for this ProtocolloPropertiesType.
     * 
     * @return riservato
     */
    public boolean isRiservato() {
        return riservato;
    }


    /**
     * Sets the riservato value for this ProtocolloPropertiesType.
     * 
     * @param riservato
     */
    public void setRiservato(boolean riservato) {
        this.riservato = riservato;
    }


    /**
     * Gets the annullato value for this ProtocolloPropertiesType.
     * 
     * @return annullato
     */
    public boolean isAnnullato() {
        return annullato;
    }


    /**
     * Sets the annullato value for this ProtocolloPropertiesType.
     * 
     * @param annullato
     */
    public void setAnnullato(boolean annullato) {
        this.annullato = annullato;
    }


    /**
     * Gets the uuidRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @return uuidRegistrazioneProtocollo
     */
    public java.lang.String getUuidRegistrazioneProtocollo() {
        return uuidRegistrazioneProtocollo;
    }


    /**
     * Sets the uuidRegistrazioneProtocollo value for this ProtocolloPropertiesType.
     * 
     * @param uuidRegistrazioneProtocollo
     */
    public void setUuidRegistrazioneProtocollo(java.lang.String uuidRegistrazioneProtocollo) {
        this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ProtocolloPropertiesType)) return false;
        ProtocolloPropertiesType other = (ProtocolloPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.objectTypeId==null && other.getObjectTypeId()==null) || 
             (this.objectTypeId!=null &&
              this.objectTypeId.equals(other.getObjectTypeId()))) &&
            ((this.numRegistrazioneProtocollo==null && other.getNumRegistrazioneProtocollo()==null) || 
             (this.numRegistrazioneProtocollo!=null &&
              this.numRegistrazioneProtocollo.equals(other.getNumRegistrazioneProtocollo()))) &&
            ((this.dataRegistrazioneProtocollo==null && other.getDataRegistrazioneProtocollo()==null) || 
             (this.dataRegistrazioneProtocollo!=null &&
              this.dataRegistrazioneProtocollo.equals(other.getDataRegistrazioneProtocollo()))) &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            this.entrata == other.isEntrata() &&
            ((this.AOOCheRegistra==null && other.getAOOCheRegistra()==null) || 
             (this.AOOCheRegistra!=null &&
              this.AOOCheRegistra.equals(other.getAOOCheRegistra()))) &&
            ((this.enteCheRegistra==null && other.getEnteCheRegistra()==null) || 
             (this.enteCheRegistra!=null &&
              this.enteCheRegistra.equals(other.getEnteCheRegistra()))) &&
            ((this.mittente==null && other.getMittente()==null) || 
             (this.mittente!=null &&
              java.util.Arrays.equals(this.mittente, other.getMittente()))) &&
            ((this.destinatario==null && other.getDestinatario()==null) || 
             (this.destinatario!=null &&
              java.util.Arrays.equals(this.destinatario, other.getDestinatario()))) &&
            ((this.segnaturaInternaXML==null && other.getSegnaturaInternaXML()==null) || 
             (this.segnaturaInternaXML!=null &&
              this.segnaturaInternaXML.equals(other.getSegnaturaInternaXML()))) &&
            ((this.segnaturaEsternaXML==null && other.getSegnaturaEsternaXML()==null) || 
             (this.segnaturaEsternaXML!=null &&
              this.segnaturaEsternaXML.equals(other.getSegnaturaEsternaXML()))) &&
            this.riservato == other.isRiservato() &&
            this.annullato == other.isAnnullato() &&
            ((this.uuidRegistrazioneProtocollo==null && other.getUuidRegistrazioneProtocollo()==null) || 
             (this.uuidRegistrazioneProtocollo!=null &&
              this.uuidRegistrazioneProtocollo.equals(other.getUuidRegistrazioneProtocollo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getObjectTypeId() != null) {
            _hashCode += getObjectTypeId().hashCode();
        }
        if (getNumRegistrazioneProtocollo() != null) {
            _hashCode += getNumRegistrazioneProtocollo().hashCode();
        }
        if (getDataRegistrazioneProtocollo() != null) {
            _hashCode += getDataRegistrazioneProtocollo().hashCode();
        }
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        _hashCode += (isEntrata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getAOOCheRegistra() != null) {
            _hashCode += getAOOCheRegistra().hashCode();
        }
        if (getEnteCheRegistra() != null) {
            _hashCode += getEnteCheRegistra().hashCode();
        }
        if (getMittente() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMittente());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMittente(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatario() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatario());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatario(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSegnaturaInternaXML() != null) {
            _hashCode += getSegnaturaInternaXML().hashCode();
        }
        if (getSegnaturaEsternaXML() != null) {
            _hashCode += getSegnaturaEsternaXML().hashCode();
        }
        _hashCode += (isRiservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAnnullato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUuidRegistrazioneProtocollo() != null) {
            _hashCode += getUuidRegistrazioneProtocollo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProtocolloPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ProtocolloPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectTypeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numRegistrazioneProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numRegistrazioneProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRegistrazioneProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRegistrazioneProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entrata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entrata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AOOCheRegistra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AOOCheRegistra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enteCheRegistra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EnteCheRegistra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mittente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mittente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segnaturaInternaXML");
        elemField.setXmlName(new javax.xml.namespace.QName("", "segnaturaInternaXML"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segnaturaEsternaXML");
        elemField.setXmlName(new javax.xml.namespace.QName("", "segnaturaEsternaXML"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("riservato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "riservato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annullato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annullato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uuidRegistrazioneProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uuidRegistrazioneProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
