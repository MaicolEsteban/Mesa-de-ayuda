package com.unipanamericana.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_solicitud", nullable = false)
    private String codigoSolicitud;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "estado_anterior")
    private String estadoAnterior;

    @Column(name = "estado_nuevo")
    private String estadoNuevo;

    @Column(name = "fecha_evento", nullable = false, updatable = false)
    private LocalDateTime fechaEvento = LocalDateTime.now();

    @Column(name = "tiempo_desde_radicacion")
    private Long tiempoDesdeRadicacion;

    @Column(name = "tipo_evento_old")
    private String tipoEventoOld;

    // ========== CONSTRUCTORES ==========

    public Evento() {}

    public Evento(String codigoSolicitud, String tipoEvento, String descripcion, String usuario) {
        this.codigoSolicitud = codigoSolicitud;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.fechaEvento = LocalDateTime.now();
    }

    // ========== GETTERS Y SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoSolicitud() {
        return codigoSolicitud;
    }

    public void setCodigoSolicitud(String codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Long getTiempoDesdeRadicacion() {
        return tiempoDesdeRadicacion;
    }

    public void setTiempoDesdeRadicacion(Long tiempoDesdeRadicacion) {
        this.tiempoDesdeRadicacion = tiempoDesdeRadicacion;
    }

    public String getTipoEventoOld() {
        return tipoEventoOld;
    }

    public void setTipoEventoOld(String tipoEventoOld) {
        this.tipoEventoOld = tipoEventoOld;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", codigoSolicitud='" + codigoSolicitud + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", usuario='" + usuario + '\'' +
                ", estadoAnterior='" + estadoAnterior + '\'' +
                ", estadoNuevo='" + estadoNuevo + '\'' +
                ", fechaEvento=" + fechaEvento +
                '}';
    }
}