package com.unipanamericana.dto;

import java.time.LocalDateTime;

/**
 * DTO para devolver información de una solicitud
 * Se utiliza en responses de GET, POST, PUT
 */
public class SolicitudResponseDTO {

    private Long id;
    private String codigo;
    private String emailEstudiante;
    private String nombreEstudiante;
    private String tipoSolicitud;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaRadicacion;
    private LocalDateTime fechaPrimeraRespuesta;
    private LocalDateTime fechaResolucion;
    private String responsableActual;
    private String prioridad;
    private String observaciones;
    private Integer tiempoRespuestaHoras;
    private Integer tiempoResolucionHoras;
    private Boolean dentroDePlazo;
    private String satisfaccion;
    private String comentarioSatisfaccion;

    // ==================== CONSTRUCTORES ====================

    public SolicitudResponseDTO() {
    }

    public SolicitudResponseDTO(Long id, String codigo, String nombreEstudiante,
                                String tipoSolicitud, String estado,
                                LocalDateTime fechaRadicacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombreEstudiante = nombreEstudiante;
        this.tipoSolicitud = tipoSolicitud;
        this.estado = estado;
        this.fechaRadicacion = fechaRadicacion;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmailEstudiante() {
        return emailEstudiante;
    }

    public void setEmailEstudiante(String emailEstudiante) {
        this.emailEstudiante = emailEstudiante;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRadicacion() {
        return fechaRadicacion;
    }

    public void setFechaRadicacion(LocalDateTime fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public LocalDateTime getFechaPrimeraRespuesta() {
        return fechaPrimeraRespuesta;
    }

    public void setFechaPrimeraRespuesta(LocalDateTime fechaPrimeraRespuesta) {
        this.fechaPrimeraRespuesta = fechaPrimeraRespuesta;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getResponsableActual() {
        return responsableActual;
    }

    public void setResponsableActual(String responsableActual) {
        this.responsableActual = responsableActual;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getTiempoRespuestaHoras() {
        return tiempoRespuestaHoras;
    }

    public void setTiempoRespuestaHoras(Integer tiempoRespuestaHoras) {
        this.tiempoRespuestaHoras = tiempoRespuestaHoras;
    }

    public Integer getTiempoResolucionHoras() {
        return tiempoResolucionHoras;
    }

    public void setTiempoResolucionHoras(Integer tiempoResolucionHoras) {
        this.tiempoResolucionHoras = tiempoResolucionHoras;
    }

    public Boolean getDentroDePlazo() {
        return dentroDePlazo;
    }

    public void setDentroDePlazo(Boolean dentroDePlazo) {
        this.dentroDePlazo = dentroDePlazo;
    }

    public String getSatisfaccion() {
        return satisfaccion;
    }

    public void setSatisfaccion(String satisfaccion) {
        this.satisfaccion = satisfaccion;
    }

    public String getComentarioSatisfaccion() {
        return comentarioSatisfaccion;
    }

    public void setComentarioSatisfaccion(String comentarioSatisfaccion) {
        this.comentarioSatisfaccion = comentarioSatisfaccion;
    }

    @Override
    public String toString() {
        return "SolicitudResponseDTO{" +
                "codigo='" + codigo + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}