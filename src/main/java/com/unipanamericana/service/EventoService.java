package com.unipanamericana.service;

import com.unipanamericana.entity.Evento;
import com.unipanamericana.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // ========== CREAR ==========

    public Evento crearEvento(String codigoSolicitud, String tipoEvento, String descripcion,
                              String usuario, String estadoAnterior, String estadoNuevo) {
        Evento evento = new Evento();
        evento.setCodigoSolicitud(codigoSolicitud);
        evento.setTipoEvento(tipoEvento);
        evento.setDescripcion(descripcion);
        evento.setUsuario(usuario);
        evento.setEstadoAnterior(estadoAnterior);
        evento.setEstadoNuevo(estadoNuevo);
        evento.setFechaEvento(LocalDateTime.now());

        return eventoRepository.save(evento);
    }

    public Evento registrarRadicacion(String codigoSolicitud, String usuario) {
        return crearEvento(codigoSolicitud, "RADICACION",
                "Solicitud radicada exitosamente", usuario, null, "RADICADA");
    }

    public Evento registrarAsignacion(String codigoSolicitud, String usuario, String responsable) {
        return crearEvento(codigoSolicitud, "ASIGNACION",
                "Asignada a: " + responsable, usuario, "RADICADA", "EN_REVISION");
    }

    public Evento registrarCambioEstado(String codigoSolicitud, String estadoAnterior,
                                        String estadoNuevo, String usuario, String descripcion) {
        return crearEvento(codigoSolicitud, "CAMBIO_ESTADO", descripcion,
                usuario, estadoAnterior, estadoNuevo);
    }

    public Evento registrarResolucion(String codigoSolicitud, String usuario, String descripcion) {
        return crearEvento(codigoSolicitud, "RESOLUCION", descripcion,
                usuario, "EN_REVISION", "RESUELTA");
    }

    public Evento registrarComentario(String codigoSolicitud, String usuario, String comentario) {
        return crearEvento(codigoSolicitud, "COMENTARIO", comentario, usuario, null, null);
    }

    // ========== OBTENER ==========

    public Optional<Evento> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    public List<Evento> obtenerTimeline(String codigoSolicitud) {
        return eventoRepository.findByCodigoSolicitudOrderByFechaEventoAsc(codigoSolicitud);
    }

    public List<Evento> obtenerTimelineDescendente(String codigoSolicitud) {
        return eventoRepository.findByCodigoSolicitudOrderByFechaEventoDesc(codigoSolicitud);
    }

    public List<Evento> obtenerHitos(String codigoSolicitud) {
        return eventoRepository.findHitos(codigoSolicitud);
    }

    public List<Evento> obtenerCambiosEstado(String codigoSolicitud) {
        return eventoRepository.findCambiosEstado(codigoSolicitud);
    }

    public List<Evento> obtenerPorTipo(String tipo) {
        return eventoRepository.findByTipoEventoOrderByFechaEventoDesc(tipo);
    }

    public List<Evento> obtenerPorUsuario(String usuario) {
        return eventoRepository.findByUsuarioOrderByFechaEventoDesc(usuario);
    }

    public List<Evento> obtenerPorEstadoNuevo(String estado) {
        return eventoRepository.findByEstadoNuevo(estado);
    }

    public List<Evento> obtenerPorCodigoYTipo(String codigo, String tipo) {
        return eventoRepository.findByCodigoSolicitudAndTipoEvento(codigo, tipo);
    }

    public List<Evento> obtenerPorCodigoYUsuario(String codigo, String usuario) {
        return eventoRepository.findByCodigoSolicitudAndUsuario(codigo, usuario);
    }

    // ========== BÚSQUEDAS ESPECIALES ==========

    public Evento obtenerRadicacion(String codigoSolicitud) {
        return eventoRepository.findRadicacion(codigoSolicitud);
    }

    public Evento obtenerUltimaResolucion(String codigoSolicitud) {
        return eventoRepository.findUltimaResolucion(codigoSolicitud);
    }

    public Evento obtenerUltimoEvento(String codigoSolicitud) {
        return eventoRepository.findUltimoEvento(codigoSolicitud);
    }

    // ========== BÚSQUEDAS AVANZADAS ==========

    public List<Evento> buscar(String q) {
        return eventoRepository.searchByDescripcion(q);
    }

    public List<Evento> obtenerCambios(String estadoAnterior, String estadoNuevo) {
        return eventoRepository.findByEstadoAnteriorAndEstadoNuevo(estadoAnterior, estadoNuevo);
    }

    // ========== CONTEOS ==========

    public long contarTotal() {
        return eventoRepository.count();
    }

    public long contarPorSolicitud(String codigoSolicitud) {
        return eventoRepository.countByCodigoSolicitud(codigoSolicitud);
    }

    public long contarPorTipo(String tipo) {
        return eventoRepository.countByTipoEvento(tipo);
    }

    public long contarPorUsuario(String usuario) {
        return eventoRepository.countByUsuario(usuario);
    }

    public long contarAsignacionesPorSolicitud(String codigoSolicitud) {
        return eventoRepository.countByCodigoSolicitudAndTipoEvento(codigoSolicitud, "ASIGNACION");
    }

    // ========== ESTADÍSTICAS ==========

    public List<?> estadisticasPorTipo() {
        return eventoRepository.estadisticasPorTipo();
    }

    public List<?> estadisticasPorUsuario() {
        return eventoRepository.estadisticasPorUsuario();
    }

    // ========== ELIMINAR ==========

    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}