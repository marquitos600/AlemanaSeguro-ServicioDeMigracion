package com.alemanaseguros.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {
    @JsonProperty("spc_ben_id")
    private Integer spcBenId;

    @JsonProperty("spc_sol_rut_titular")
    private String spcSolRutTitular;

    @JsonProperty("spc_sol_sin_id")
    private String spcSolSinId;

    @JsonProperty("spc_sol_sin_npoliza")
    private String spcSolSinNpoliza;

    @JsonProperty("documento_tipo_solicitud")
    private String documentoTipoSolicitud;

    @JsonProperty("documento_tipo_archivo")
    private String documentoTipoArchivo;

    @JsonProperty("documento_nombre")
    private String documentoNombre;

    @JsonProperty("file_base64")
    private String fileBase64;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("disponible_affresco")
    private String disponibleAffresco;

    private String id;

    // Getters y Setters
    public Integer getSpcBenId() {
        return spcBenId;
    }

    public void setSpcBenId(Integer spcBenId) {
        this.spcBenId = spcBenId;
    }

    public String getSpcSolRutTitular() {
        return spcSolRutTitular;
    }

    public void setSpcSolRutTitular(String spcSolRutTitular) {
        this.spcSolRutTitular = spcSolRutTitular;
    }

    public String getSpcSolSinId() {
        return spcSolSinId;
    }

    public void setSpcSolSinId(String spcSolSinId) {
        this.spcSolSinId = spcSolSinId;
    }

    public String getSpcSolSinNpoliza() {
        return spcSolSinNpoliza;
    }

    public void setSpcSolSinNpoliza(String spcSolSinNpoliza) {
        this.spcSolSinNpoliza = spcSolSinNpoliza;
    }

    public String getDocumentoTipoSolicitud() {
        return documentoTipoSolicitud;
    }

    public void setDocumentoTipoSolicitud(String documentoTipoSolicitud) {
        this.documentoTipoSolicitud = documentoTipoSolicitud;
    }

    public String getDocumentoTipoArchivo() {
        return documentoTipoArchivo;
    }

    public void setDocumentoTipoArchivo(String documentoTipoArchivo) {
        this.documentoTipoArchivo = documentoTipoArchivo;
    }

    public String getDocumentoNombre() {
        return documentoNombre;
    }

    public void setDocumentoNombre(String documentoNombre) {
        this.documentoNombre = documentoNombre;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDisponibleAffresco() {
        return disponibleAffresco;
    }

    public void setDisponibleAffresco(String disponibleAffresco) {
        this.disponibleAffresco = disponibleAffresco;
    }

    public String getId() {
        return this.spcBenId.toString();
    }

    public void setId(String id) {
        this.id = id;
    }
}
