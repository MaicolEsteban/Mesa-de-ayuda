package com.unipanamericana.repository;

import com.unipanamericana.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // ========== BÚSQUEDAS POR SOLICITUD ==========

    List<Evento> findByCodigoSolicitud(String codigoSolicitud);

    List<Evento> findByCodigoSolicitudOrderByFechaEventoAsc(String codigoSolicitud);

    List<Evento> findByCodigoSolicitudOrderByFechaEventoDesc(String codigoSolicitud);

    // ========== BÚSQUEDAS POR TIPO ==========

    List<Evento> findByTipoEvento(String tipoEvento);

    List<Evento> findByTipoEventoOrderByFechaEventoDesc(String tipoEvento);

    // ========== BÚSQUEDAS POR USUARIO ==========

    List<Evento> findByUsuario(String usuario);

    List<Evento> findByUsuarioOrderByFechaEventoDesc(String usuario);

    // ========== BÚSQUEDAS POR ESTADO ==========

    List<Evento> findByEstadoNuevo(String estadoNuevo);

    List<Evento> findByEstadoAnteriorAndEstadoNuevo(String estadoAnterior, String estadoNuevo);

    // ========== BÚSQUEDAS COMBINADAS ==========

    List<Evento> findByCodigoSolicitudAndTipoEvento(String codigoSolicitud, String tipoEvento);

    List<Evento> findByCodigoSolicitudAndUsuario(String codigoSolicitud, String usuario);

    // ========== BÚSQUEDAS POR FECHAS ==========

    List<Evento> findByFechaEventoAfter(LocalDateTime fecha);

    List<Evento> findByFechaEventoBetween(LocalDateTime inicio, LocalDateTime fin);

    // ========== BÚSQUEDAS AVANZADAS ==========

    @Query("SELECT e FROM Evento e WHERE e.descripcion LIKE %?1%")
    List<Evento> searchByDescripcion(String descripcion);

    @Query("SELECT e FROM Evento e WHERE e.codigoSolicitud = ?1 AND e.tipoEvento IN ('RADICACION', 'RESOLUCION') ORDER BY e.fechaEvento ASC")
    List<Evento> findHitos(String codigoSolicitud);

    @Query("SELECT e FROM Evento e WHERE e.codigoSolicitud = ?1 AND e.tipoEvento = 'CAMBIO_ESTADO' ORDER BY e.fechaEvento ASC")
    List<Evento> findCambiosEstado(String codigoSolicitud);

    // ========== CONTEOS ==========

    long countByCodigoSolicitud(String codigoSolicitud);

    long countByTipoEvento(String tipoEvento);

    long countByUsuario(String usuario);

    long countByCodigoSolicitudAndTipoEvento(String codigoSolicitud, String tipoEvento);

    // ========== ESTADÍSTICAS ==========

    @Query("SELECT e.tipoEvento as tipo, COUNT(e) as total FROM Evento e GROUP BY e.tipoEvento")
    List<?> estadisticasPorTipo();

    @Query("SELECT e.usuario as usuario, COUNT(e) as total FROM Evento e GROUP BY e.usuario")
    List<?> estadisticasPorUsuario();

    // ========== BÚSQUEDAS ESPECIALES ==========

    @Query("SELECT e FROM Evento e WHERE e.codigoSolicitud = ?1 AND e.tipoEvento = 'RADICACION'")
    Evento findRadicacion(String codigoSolicitud);

    // Helper methods - Estos se implementan en el servicio
    default Evento findUltimaResolucion(String codigoSolicitud) {
        List<Evento> eventos = findByCodigoSolicitudAndTipoEvento(codigoSolicitud, "RESOLUCION");
        return eventos.isEmpty() ? null : eventos.get(eventos.size() - 1);
    }

    default Evento findUltimoEvento(String codigoSolicitud) {
        List<Evento> eventos = findByCodigoSolicitudOrderByFechaEventoDesc(codigoSolicitud);
        return eventos.isEmpty() ? null : eventos.get(0);
    }

    default List<Evento> findTodayEvents() {
        // Se implementa en el servicio
        return List.of();
    }

    default List<Evento> findLastWeekEvents() {
        // Se implementa en el servicio
        return List.of();
    }

    default List<Evento> findCurrentMonthEvents() {
        // Se implementa en el servicio
        return List.of();
    }

    default long countEventosPorUsuarioHoy(String usuario) {
        // Se implementa en el servicio
        return 0;
    }

    default List<?> estadisticasPorFecha() {
        // Se implementa en el servicio
        return List.of();
    }
}