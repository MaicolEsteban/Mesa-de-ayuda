package com.unipanamericana.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    private String codigo;

    @Column(name = "email_estudiante", nullable = false)
    private String emailEstudiante;

    @Column(name = "cedula_estudiante", nullable = false)
    private String cedulaEstudiante;

    @Column(name = "nombre_estudiante", nullable = false)
    private String nombreEstudiante;

    @Column(name = "tipo_solicitud", nullable = false)
    private String tipoSolicitud;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private String estado = "RADICADA";

    @Column(name = "email_responsable")
    private String emailResponsable;

    @Column(name = "fecha_radicacion", nullable = false, updatable = false)
    private LocalDateTime fechaRadicacion = LocalDateTime.now();

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "fecha_primera_respuesta")
    private LocalDateTime fechaPrimeraRespuesta;

    @Column(name = "calificacion_satisfaccion")
    private Integer calificacionSatisfaccion;

    @Column(name = "comentario_satisfaccion", columnDefinition = "TEXT")
    private String comentarioSatisfaccion;

    @Column(name = "prioridad")
    private Integer prioridad = 3;

    @Column(name = "dias_transcurridos")
    private Integer diasTranscurridos;

    @Column(name = "activa")
    private Boolean activa = true;

    @Column(name = "fecha_limite_solucion")
    private LocalDateTime fechaLimiteSolucion;

    @Column(name = "horas_restantes")
    private Integer horasRestantes;

    @Column(name = "vencida")
    private Boolean vencida = false;

    // ========== CONSTRUCTORES ==========

    public Solicitud() {}

    public Solicitud(String codigo, String emailEstudiante, String cedulaEstudiante,
                     String nombreEstudiante, String tipoSolicitud, String descripcion) {
        this.codigo = codigo;
        this.emailEstudiante = emailEstudiante;
        this.cedulaEstudiante = cedulaEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.estado = "RADICADA";
        this.activa = true;
    }

    // ========== GETTERS Y SETTERS ==========
    public LocalDateTime getFechaLimiteSolucion() {
        return fechaLimiteSolucion;
    }

    public void setFechaLimiteSolucion(LocalDateTime fechaLimiteSolucion) {
        this.fechaLimiteSolucion = fechaLimiteSolucion;
    }

    public Integer getHorasRestantes() {
        return horasRestantes;
    }

    public void setHorasRestantes(Integer horasRestantes) {
        this.horasRestantes = horasRestantes;
    }

    public Boolean getVencida() {
        return vencida;
    }

    public void setVencida(Boolean vencida) {
        this.vencida = vencida;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmailResponsable() {
        return emailResponsable;
    }

    public void setEmailResponsable(String emailResponsable) {
        this.emailResponsable = emailResponsable;
    }

    public LocalDateTime getFechaRadicacion() {
        return fechaRadicacion;
    }

    public void setFechaRadicacion(LocalDateTime fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public LocalDateTime getFechaPrimeraRespuesta() {
        return fechaPrimeraRespuesta;
    }

    public void setFechaPrimeraRespuesta(LocalDateTime fechaPrimeraRespuesta) {
        this.fechaPrimeraRespuesta = fechaPrimeraRespuesta;
    }

    public Integer getCalificacionSatisfaccion() {
        return calificacionSatisfaccion;
    }

    public void setCalificacionSatisfaccion(Integer calificacionSatisfaccion) {
        this.calificacionSatisfaccion = calificacionSatisfaccion;
    }

    public String getComentarioSatisfaccion() {
        return comentarioSatisfaccion;
    }

    public void setComentarioSatisfaccion(String comentarioSatisfaccion) {
        this.comentarioSatisfaccion = comentarioSatisfaccion;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getDiasTranscurridos() {
        return diasTranscurridos;
    }

    public void setDiasTranscurridos(Integer diasTranscurridos) {
        this.diasTranscurridos = diasTranscurridos;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "codigo='" + codigo + '\'' +
                ", emailEstudiante='" + emailEstudiante + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", tipoSolicitud='" + tipoSolicitud + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaRadicacion=" + fechaRadicacion +
                ", prioridad=" + prioridad +
                '}';
    }

    public void calcularHorasRestantes() {
        if (this.fechaLimiteSolucion != null) {
            LocalDateTime ahora = LocalDateTime.now();
            long minutosRestantes = java.time.temporal.ChronoUnit.MINUTES.between(ahora, this.fechaLimiteSolucion);
            this.horasRestantes = (int) (minutosRestantes / 60);
            this.vencida = minutosRestantes <= 0;
        }
    }
}