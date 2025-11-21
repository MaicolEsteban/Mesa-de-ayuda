package com.unipanamericana.repository;

import com.unipanamericana.entity.Solicitud;
import com.unipanamericana.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, String> {

    // ========== BÚSQUEDAS POR EMAIL ==========


    List<Solicitud> findByEmailEstudiante(String emailEstudiante);

    List<Solicitud> findByEmailEstudianteAndActiva(String emailEstudiante, Boolean activa);

    List<Solicitud> findByEmailEstudianteOrderByFechaRadicacionDesc(String emailEstudiante);

    List<Solicitud> findByEmailResponsable(String emailResponsable);

    List<Solicitud> findByEmailResponsableAndActiva(String emailResponsable, Boolean activa);

    // ========== SOLICITUDES ASIGNADAS A UN REVISOR (SIN NULL) ==========

    @Query("SELECT s FROM Solicitud s WHERE s.emailResponsable IS NOT NULL AND s.emailResponsable = ?1")
    List<Solicitud> findAsignadasARevisor(String emailResponsable);

    // ========== BÚSQUEDAS POR ESTADO ==========

    List<Solicitud> findByEstado(String estado);

    List<Solicitud> findByEstadoAndActiva(String estado, Boolean activa);

    List<Solicitud> findByEstadoOrderByFechaRadicacionAsc(String estado);

    List<Solicitud> findByEstadoOrderByPrioridad(String estado);

    // ========== BÚSQUEDAS POR TIPO ==========

    List<Solicitud> findByTipoSolicitud(String tipoSolicitud);

    List<Solicitud> findByTipoSolicitudAndActiva(String tipoSolicitud, Boolean activa);

    List<Solicitud> findByTipoSolicitudAndEstado(String tipoSolicitud, String estado);

    // ========== BÚSQUEDAS POR PRIORIDAD ==========

    List<Solicitud> findByPrioridad(Integer prioridad);

    List<Solicitud> findByPrioridadAndActiva(Integer prioridad, Boolean activa);

    // ========== BÚSQUEDAS POR FECHAS ==========

    List<Solicitud> findByFechaRadicacionAfter(LocalDateTime fecha);

    List<Solicitud> findByFechaRadicacionBetween(LocalDateTime inicio, LocalDateTime fin);

    // ========== BÚSQUEDAS COMBINADAS ==========

    List<Solicitud> findByEmailEstudianteAndEstado(String emailEstudiante, String estado);

    List<Solicitud> findByEmailResponsableAndEstado(String emailResponsable, String estado);

    List<Solicitud> findByEstadoAndEmailResponsableIsNull(String estado);

    // ========== BÚSQUEDAS AVANZADAS ==========

    @Query("SELECT s FROM Solicitud s WHERE s.descripcion LIKE %?1%")
    List<Solicitud> searchByDescripcion(String descripcion);

    List<Solicitud> findByCedulaEstudiante(String cedula);

    List<Solicitud> findByNombreEstudianteContainsIgnoreCase(String nombre);

    // ========== CONTEOS ==========

    long countByEstado(String estado);

    long countByEstadoAndActiva(String estado, Boolean activa);

    long countByEmailEstudiante(String emailEstudiante);

    long countByEmailResponsable(String emailResponsable);

    long countByTipoSolicitud(String tipoSolicitud);

    long countByPrioridad(Integer prioridad);

    // ========== ESTADÍSTICAS ==========

    @Query("SELECT s.estado as estado, COUNT(s) as total FROM Solicitud s WHERE s.activa = true GROUP BY s.estado")
    List<?> estadisticasPorEstado();

    @Query("SELECT s.tipoSolicitud as tipo, COUNT(s) as total FROM Solicitud s WHERE s.activa = true GROUP BY s.tipoSolicitud")
    List<?> estadisticasPorTipo();

    @Query("SELECT s.prioridad as prioridad, COUNT(s) as total FROM Solicitud s WHERE s.activa = true GROUP BY s.prioridad")
    List<?> estadisticasPorPrioridad();

    // ========== HELPER METHODS ==========

    default List<Solicitud> findByEmailEstudianteActivaOrderByFechaRadicacionDesc(String email) {
        return findByEmailEstudianteAndActivaOrderByFechaRadicacionDesc(email, true);
    }

    default List<Solicitud> findByEmailEstudianteAndActivaOrderByFechaRadicacionDesc(String email, Boolean activa) {
        List<Solicitud> solicitudes = findByEmailEstudianteAndActiva(email, activa);
        solicitudes.sort((a, b) -> b.getFechaRadicacion().compareTo(a.getFechaRadicacion()));
        return solicitudes;
    }

    default List<Solicitud> findByEstadoActivaOrderByFechaRadicacion(String estado) {
        return findByEstadoAndActivaOrderByFechaRadicacionAsc(estado, true);
    }

    default List<Solicitud> findByEstadoAndActivaOrderByFechaRadicacionAsc(String estado, Boolean activa) {
        return findByEstadoAndActiva(estado, activa);
    }

    default List<Solicitud> findSinAsignar() {
        return findByEstadoAndEmailResponsableIsNull("RADICADA");
    }

    default List<Solicitud> findPendientesPorResponsable(String email) {
        List<Solicitud> solicitudes = findByEmailResponsableAndEstado(email, "RADICADA");
        solicitudes.addAll(findByEmailResponsableAndEstado(email, "EN_REVISION"));
        return solicitudes;
    }

    default List<Solicitud> findByPrioridadUrgente(Integer prioridad) {
        List<Solicitud> solicitudes = findByEstado("RADICADA");
        solicitudes.sort((a, b) -> a.getPrioridad().compareTo(b.getPrioridad()));
        return solicitudes;
    }

    default List<Solicitud> findSinCalificar() {
        List<Solicitud> solicitudes = findByEstado("RESUELTA");
        solicitudes.removeIf(s -> s.getCalificacionSatisfaccion() != null);
        return solicitudes;
    }

    default Double findAverageCalificacion() {
        List<Solicitud> solicitudes = findAll();
        long count = solicitudes.stream().filter(s -> s.getCalificacionSatisfaccion() != null).count();
        if (count == 0) return 0.0;
        double sum = solicitudes.stream()
                .filter(s -> s.getCalificacionSatisfaccion() != null)
                .mapToInt(Solicitud::getCalificacionSatisfaccion)
                .sum();
        return sum / count;
    }

    default List<Solicitud> findResueltasOrderByFechaResolucion() {
        List<Solicitud> solicitudes = findByEstado("RESUELTA");
        solicitudes.sort((a, b) -> {
            if (a.getFechaResolucion() == null) return 1;
            if (b.getFechaResolucion() == null) return -1;
            return b.getFechaResolucion().compareTo(a.getFechaResolucion());
        });
        return solicitudes;
    }

    default List<Solicitud> findSinResponsableActivo() {
        List<Solicitud> solicitudes = findByEstado("RADICADA");
        solicitudes.addAll(findByEstado("EN_REVISION"));
        solicitudes.removeIf(s -> s.getEmailResponsable() != null);
        return solicitudes;
    }

    default long countRadicadas() {
        return countByEstado("RADICADA");
    }

    default long countEnRevision() {
        return countByEstado("EN_REVISION");
    }

    default long countResueltas() {
        return countByEstado("RESUELTA");
    }

    default long countEsperandoInfo() {
        return countByEstado("ESPERANDO_INFO");
    }

    List<Solicitud> findByVencidaTrue();

}