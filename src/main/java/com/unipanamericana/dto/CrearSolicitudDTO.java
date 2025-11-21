package com.unipanamericana.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO para crear una nueva solicitud
 * Se utiliza para el endpoint POST /api/solicitudes
 *
 * Valida los datos antes de llegar al servicio
 */
public class CrearSolicitudDTO {

    @NotBlank(message = "El email del estudiante es requerido")
    @Email(message = "Debe proporcionar un email válido")
    private String emailEstudiante;

    @NotBlank(message = "La cédula es requerida")
    @Pattern(regexp = "\\d{6,15}", message = "La cédula debe contener entre 6 y 15 dígitos")
    private String cedulaEstudiante;

    @NotBlank(message = "El nombre del estudiante es requerido")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombreEstudiante;

    @NotBlank(message = "El tipo de solicitud es requerido")
    @Size(max = 50, message = "El tipo de solicitud no puede exceder 50 caracteres")
    private String tipoSolicitud; // certificado, constancia, homologación, etc.

    @NotBlank(message = "La descripción es requerida")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    private String descripcion;

    @Size(max = 20, message = "La prioridad no puede exceder 20 caracteres")
    private String prioridad = "MEDIA"; // BAJA, MEDIA, ALTA, URGENTE

    private String facultad;

    private String programa;

    // ==================== CONSTRUCTORES ====================

    public CrearSolicitudDTO() {
    }

    public CrearSolicitudDTO(String emailEstudiante, String cedulaEstudiante,
                             String nombreEstudiante, String tipoSolicitud,
                             String descripcion) {
        this.emailEstudiante = emailEstudiante;
        this.cedulaEstudiante = cedulaEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.prioridad = "MEDIA";
    }

    // ==================== GETTERS Y SETTERS ====================

    public String getEmailEstudiante() {
        return emailEstudiante;
    }

    public void setEmailEstudiante(String emailEstudiante) {
        this.emailEstudiante = emailEstudiante;
    }

    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }

    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    @Override
    public String toString() {
        return "CrearSolicitudDTO{" +
                "emailEstudiante='" + emailEstudiante + '\'' +
                ", tipoSolicitud='" + tipoSolicitud + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                '}';
    }
}