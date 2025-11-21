package com.unipanamericana.repository;

import com.unipanamericana.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Usuario
 * Proporciona métodos para acceder y manipular usuarios del sistema
 *
 * Los usuarios pueden ser:
 * - ESTUDIANTE: pueden crear y ver sus propias solicitudes
 * - PERSONAL: pueden ver y procesar solicitudes de sus facultades
 * - ADMIN: acceso completo al sistema
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // ==================== BÚSQUEDAS BÁSICAS ====================

    /**
     * Busca un usuario por su email (usado para login)
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por su cédula
     */
    Optional<Usuario> findByCedula(String cedula);

    /**
     * Busca un usuario por email y contraseña (para autenticación)
     */
    Optional<Usuario> findByEmailAndContrasena(String email, String contrasena);

    /**
     * Busca un usuario activo por email
     */
    Optional<Usuario> findByEmailAndActivoTrue(String email);

    // ==================== BÚSQUEDAS POR TIPO DE USUARIO ====================

    /**
     * Busca todos los estudiantes
     */
    List<Usuario> findByTipoUsuario(String tipoUsuario);

    /**
     * Busca todos los estudiantes activos
     */
    List<Usuario> findByTipoUsuarioAndActivoTrue(String tipoUsuario);

    /**
     * Busca todos los personal administrativo
     */
    List<Usuario> findByTipoUsuarioOrderByNombreAsc(String tipoUsuario);

    /**
     * Busca todo el personal administrativo activo
     */
    List<Usuario> findByTipoUsuarioAndActivoTrueOrderByNombreAsc(String tipoUsuario);

    // ==================== BÚSQUEDAS POR ROL ====================

    /**
     * Busca todos los usuarios con un rol específico
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca todos los administradores activos
     */
    List<Usuario> findByRolAndActivoTrueOrderByNombreAsc(String rol);

    /**
     * Busca personal que puede procesar solicitudes
     */
    List<Usuario> findByRolContainsAndActivoTrueOrderByNombreAsc(String rol);

    // ==================== BÚSQUEDAS POR FACULTAD ====================

    /**
     * Busca todos los usuarios de una facultad específica
     */
    List<Usuario> findByFacultad(String facultad);

    /**
     * Busca personal activo de una facultad
     */
    List<Usuario> findByFacultadAndActivoTrueAndTipoUsuario(
            String facultad, String tipoUsuario);

    /**
     * Busca personal administrativo de una facultad
     */
    List<Usuario> findByFacultadAndTipoUsuarioAndActivoTrueOrderByNombreAsc(
            String facultad, String tipoUsuario);

    // ==================== BÚSQUEDAS POR DEPARTAMENTO ====================

    /**
     * Busca todo el personal de un departamento específico
     */
    List<Usuario> findByDepartamento(String departamento);

    /**
     * Busca personal activo de un departamento
     */
    List<Usuario> findByDepartamentoAndActivoTrueOrderByNombreAsc(String departamento);

    // ==================== BÚSQUEDAS DE ESTADO ====================

    /**
     * Busca todos los usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca todos los usuarios inactivos
     */
    List<Usuario> findByActivoFalse();

    /**
     * Busca usuarios verificados (email confirmado)
     */
    List<Usuario> findByVerificadoTrueAndActivoTrue();

    /**
     * Busca usuarios no verificados
     */
    List<Usuario> findByVerificadoFalseAndActivoTrue();

    // ==================== CONTEOS ====================

    /**
     * Cuenta cuántos usuarios hay en total
     */
    long countByActivoTrue();

    /**
     * Cuenta cuántos estudiantes hay
     */
    long countByTipoUsuarioAndActivoTrue(String tipoUsuario);

    /**
     * Cuenta cuánto personal hay en una facultad
     */
    long countByFacultadAndTipoUsuarioAndActivoTrue(
            String facultad, String tipoUsuario);

    /**
     * Cuenta cuántos usuarios hay de un tipo específico (sin filtro de activo)
     */
    long countByTipoUsuario(String tipoUsuario);

    // ==================== QUERIES PERSONALIZADAS ====================

    /**
     * Query JPQL: Busca personal que puede procesar solicitudes (activos)
     */
    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = 'PERSONAL' AND u.activo = true " +
            "ORDER BY u.nombre ASC")
    List<Usuario> findAllActivePersonal();

    /**
     * Query JPQL: Busca administradores del sistema
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'ROL_ADMIN' AND u.activo = true")
    List<Usuario> findAllAdmins();

    /**
     * Query JPQL: Busca personal activo de una facultad específica
     */
    @Query("SELECT u FROM Usuario u WHERE u.facultad = :facultad AND u.tipoUsuario = 'PERSONAL' " +
            "AND u.activo = true ORDER BY u.nombre ASC")
    List<Usuario> findActivePersonalByFacultad(@Param("facultad") String facultad);

    /**
     * Query JPQL: Busca usuarios activos de un departamento
     */
    @Query("SELECT u FROM Usuario u WHERE u.departamento = :departamento AND u.activo = true " +
            "ORDER BY u.nombre ASC")
    List<Usuario> findActiveUsersByDepartment(@Param("departamento") String departamento);

    /**
     * Query JPQL: Busca usuarios que aún no han verificado su email
     */
    @Query("SELECT u FROM Usuario u WHERE u.verificado = false AND u.activo = true")
    List<Usuario> findUnverifiedUsers();

    /**
     * Query JPQL: Cuenta cuánto personal activo hay en total
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = 'PERSONAL' AND u.activo = true")
    long countActivePersonal();

    /**
     * Query JPQL: Busca usuarios por búsqueda de texto (nombre o email)
     */
    @Query("SELECT u FROM Usuario u WHERE (u.nombre LIKE %:searchTerm% OR u.email LIKE %:searchTerm%) " +
            "AND u.activo = true")
    List<Usuario> searchByNameOrEmail(@Param("searchTerm") String searchTerm);

    /**
     * Query JPQL: Obtiene todos los responsables únicos que procesan solicitudes
     */
    @Query("SELECT DISTINCT u FROM Usuario u WHERE u.tipoUsuario = 'PERSONAL' AND u.activo = true")
    List<Usuario> findAllActiveResponsables();

    /**
     * Query JPQL: Busca personal de una facultad y departamento específico
     */
    @Query("SELECT u FROM Usuario u WHERE u.facultad = :facultad AND u.departamento = :departamento " +
            "AND u.tipoUsuario = 'PERSONAL' AND u.activo = true ORDER BY u.nombre ASC")
    List<Usuario> findPersonalByFacultadAndDepartamento(
            @Param("facultad") String facultad,
            @Param("departamento") String departamento);
}