package com.unipanamericana.service;

import com.unipanamericana.entity.Solicitud;
import com.unipanamericana.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    // ========== CREAR ==========

    public Solicitud crearSolicitud(String codigo, String emailEstudiante, String cedulaEstudiante,
                                    String nombreEstudiante, String tipoSolicitud, String descripcion) {
        Solicitud solicitud = new Solicitud();
        solicitud.setCodigo(codigo);
        solicitud.setEmailEstudiante(emailEstudiante);
        solicitud.setCedulaEstudiante(cedulaEstudiante);
        solicitud.setNombreEstudiante(nombreEstudiante);
        solicitud.setTipoSolicitud(tipoSolicitud);
        solicitud.setDescripcion(descripcion);
        solicitud.setEstado("RADICADA");
        solicitud.setActiva(true);

        return solicitudRepository.save(solicitud);
    }

    // ========== OBTENER ==========

    public Optional<Solicitud> obtenerPorCodigo(String codigo) {
        return solicitudRepository.findById(codigo);
    }

    public List<Solicitud> obtenerTodas() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> obtenerMisSolicitudes(String email) {
        return solicitudRepository.findByEmailEstudiante(email);
    }

    public List<Solicitud> obtenerSolicitudesEstudiante(String email) {
        return solicitudRepository.findByEmailEstudiante(email);
    }

    public List<Solicitud> obtenerPorEstado(String estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public List<Solicitud> obtenerPorTipo(String tipo) {
        return solicitudRepository.findByTipoSolicitud(tipo);
    }

    public List<Solicitud> obtenerPorPrioridad(Integer prioridad) {
        return solicitudRepository.findByPrioridad(prioridad);
    }

    public List<Solicitud> obtenerSinAsignar() {
        return solicitudRepository.findSinAsignar();
    }

    public List<Solicitud> obtenerPorResponsable(String email) {
        return solicitudRepository.findByEmailResponsable(email);
    }

    public List<Solicitud> obtenerPendientesPorResponsable(String email) {
        return solicitudRepository.findPendientesPorResponsable(email);
    }

    // ========== BÚSQUEDAS COMBINADAS ==========

    public List<Solicitud> obtenerEstudiantePorEstado(String email, String estado) {
        return solicitudRepository.findByEmailEstudianteAndEstado(email, estado);
    }

    public List<Solicitud> obtenerResponsablePorEstado(String email, String estado) {
        List<Solicitud> solicitudes = solicitudRepository.findByEmailResponsable(email);
        solicitudes.removeIf(s -> !s.getEstado().equals(estado));
        return solicitudes;
    }

    public List<Solicitud> obtenerPorTipoYEstado(String tipo, String estado) {
        return solicitudRepository.findByTipoSolicitudAndEstado(tipo, estado);
    }

    // ========== BÚSQUEDAS AVANZADAS ==========

    public List<Solicitud> buscarPorDescripcion(String descripcion) {
        return solicitudRepository.searchByDescripcion(descripcion);
    }

    public List<Solicitud> obtenerPorCedula(String cedula) {
        return solicitudRepository.findByCedulaEstudiante(cedula);
    }

    public List<Solicitud> obtenerPorNombre(String nombre) {
        return solicitudRepository.findByNombreEstudianteContainsIgnoreCase(nombre);
    }

    public List<Solicitud> obtenerSinCalificar() {
        return solicitudRepository.findSinCalificar();
    }

    public List<Solicitud> obtenerResueltasOrdenadas() {
        return solicitudRepository.findResueltasOrderByFechaResolucion();
    }

    // ========== ACTUALIZAR ESTADO ==========

    public Solicitud cambiarEstado(String codigo, String nuevoEstado) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setEstado(nuevoEstado);
            return solicitudRepository.save(s);
        }
        return null;
    }

    public Solicitud asignarResponsable(String codigo, String emailResponsable) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setEmailResponsable(emailResponsable);
            s.setFechaAsignacion(LocalDateTime.now());
            return solicitudRepository.save(s);
        }
        return null;
    }

    public Solicitud establecerPrioridad(String codigo, Integer prioridad) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setPrioridad(prioridad);
            return solicitudRepository.save(s);
        }
        return null;
    }

    public Solicitud calificar(String codigo, Integer calificacion, String comentario) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setCalificacionSatisfaccion(calificacion);
            s.setComentarioSatisfaccion(comentario);
            return solicitudRepository.save(s);
        }
        return null;
    }

    public Solicitud resolver(String codigo, String descripcionResolucion) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setEstado("RESUELTA");
            s.setFechaResolucion(LocalDateTime.now());
            return solicitudRepository.save(s);
        }
        return null;
    }

    // ========== CONTEOS ==========

    public long contarTotal() {
        return solicitudRepository.count();
    }

    public long contarRadicadas() {
        return solicitudRepository.countRadicadas();
    }

    public long contarEnRevision() {
        return solicitudRepository.countEnRevision();
    }

    public long contarResueltas() {
        return solicitudRepository.countResueltas();
    }

    public long contarEsperandoInfo() {
        return solicitudRepository.countEsperandoInfo();
    }

    public long contarPorTipo(String tipo) {
        return solicitudRepository.countByTipoSolicitud(tipo);
    }

    public long contarPorEstado(String estado) {
        return solicitudRepository.countByEstado(estado);
    }

    public long contarPorEstudiante(String email) {
        return solicitudRepository.countByEmailEstudiante(email);
    }

    // ========== ESTADÍSTICAS ==========

    public Double obtenerCalificacionPromedio() {
        return solicitudRepository.findAverageCalificacion();
    }

    public List<?> estadisticasPorEstado() {
        return solicitudRepository.estadisticasPorEstado();
    }

    public List<?> estadisticasPorTipo() {
        return solicitudRepository.estadisticasPorTipo();
    }

    public List<?> estadisticasPorPrioridad() {
        return solicitudRepository.estadisticasPorPrioridad();
    }

    // ========== ELIMINAR ==========

    public void eliminarSolicitud(String codigo) {
        solicitudRepository.deleteById(codigo);
    }

    public void desactivarSolicitud(String codigo) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setActiva(false);
            solicitudRepository.save(s);
        }
    }
}