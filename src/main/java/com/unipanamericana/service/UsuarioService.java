package com.unipanamericana.service;

import com.unipanamericana.entity.Usuario;
import com.unipanamericana.repository.UsuarioRepository;

import com.unipanamericana.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service para la lógica de negocio de Usuarios
 *
 * Responsabilidades:
 * - Crear usuarios
 * - Autenticar usuarios
 * - Buscar usuarios
 * - Gestionar roles y permisos
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==================== CREAR USUARIOS ====================

    /**
     * Crea un nuevo usuario
     */
    public Usuario crearUsuario(String email, String nombre, String cedula,
                                String tipoUsuario, String rol, String contrasena) {
        Usuario usuario = new Usuario(email, nombre, cedula, tipoUsuario, rol);
        usuario.setContrasena(contrasena); // En producción, encriptar
        return usuarioRepository.save(usuario);
    }

    /**
     * Crea un nuevo usuario de estudiante
     */
    public Usuario crearEstudiante(String email, String nombre, String cedula,
                                   String facultad, String contrasena) {
        Usuario usuario = new Usuario(
                email,
                nombre,
                cedula,
                "ESTUDIANTE",
                facultad,
                "ROL_ESTUDIANTE",
                PasswordUtil.encriptarContrasena(contrasena)
        );
        return usuarioRepository.save(usuario);
    }

    /**
     * Crea un nuevo usuario de personal administrativo
     */
    public Usuario crearPersonal(String email, String nombre, String cedula,
                                 String facultad, String departamento,
                                 String contrasena) {
        Usuario usuario = new Usuario(
                email,
                nombre,
                cedula,
                "PERSONAL",
                facultad,
                "ROL_PERSONAL",
                PasswordUtil.encriptarContrasena(contrasena)
        );
        usuario.setDepartamento(departamento);
        return usuarioRepository.save(usuario);
    }

    /**
     * Crea un nuevo administrador
     */
    public Usuario crearAdmin(String email, String nombre, String cedula, String contrasena) {
        Usuario usuario = new Usuario(email, nombre, cedula, "PERSONAL", "ROL_ADMIN");
        usuario.setContrasena(PasswordUtil.encriptarContrasena(contrasena));
        usuario.setDepartamento("Administración");
        return usuarioRepository.save(usuario);
    }

    // ==================== AUTENTICACIÓN ====================

    /**
     * Autentica un usuario (email y contraseña)
     * En producción, encriptar contraseñas con BCrypt
     */
    public Optional<Usuario> autenticar(String email, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Validar contraseña contra el hash
            if (PasswordUtil.validarContrasena(contrasena, usuario.getContrasena())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    /**
     * Verifica si un usuario está activo
     */
    public boolean usuarioActivo(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndActivoTrue(email);
        return usuario.isPresent();
    }

    /**
     * Registra el último acceso de un usuario
     */
    public Usuario registrarAcceso(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setUltimoAcceso(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    // ==================== BÚSQUEDAS ====================

    /**
     * Obtiene un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene un usuario por cédula
     */
    public Optional<Usuario> obtenerPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }

    /**
     * Obtiene todos los estudiantes activos
     */
    public List<Usuario> obtenerEstudiantes() {
        return usuarioRepository.findByTipoUsuarioAndActivoTrue("ESTUDIANTE");
    }

    /**
     * Obtiene todo el personal administrativo activo
     */
    public List<Usuario> obtenerPersonal() {
        return usuarioRepository.findByTipoUsuarioAndActivoTrueOrderByNombreAsc("PERSONAL");
    }

    /**
     * Obtiene todos los administradores activos
     */
    public List<Usuario> obtenerAdmins() {
        return usuarioRepository.findAllAdmins();
    }

    /**
     * Obtiene todos los usuarios activos
     */
    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Obtiene personal de una facultad específica
     */
    public List<Usuario> obtenerPersonalPorFacultad(String facultad) {
        return usuarioRepository.findActivePersonalByFacultad(facultad);
    }

    /**
     * Obtiene usuarios de un departamento específico
     */
    public List<Usuario> obtenerPorDepartamento(String departamento) {
        return usuarioRepository.findActiveUsersByDepartment(departamento);
    }

    /**
     * Busca usuarios por nombre o email
     */
    public List<Usuario> buscar(String termino) {
        return usuarioRepository.searchByNameOrEmail(termino);
    }

    /**
     * Obtiene usuarios verificados
     */
    public List<Usuario> obtenerVerificados() {
        return usuarioRepository.findByVerificadoTrueAndActivoTrue();
    }

    /**
     * Obtiene usuarios no verificados
     */
    public List<Usuario> obtenerNoVerificados() {
        return usuarioRepository.findByVerificadoFalseAndActivoTrue();
    }

    /**
     * Obtiene todos los responsables (personal activo)
     */
    public List<Usuario> obtenerResponsables() {
        return usuarioRepository.findAllActiveResponsables();
    }

    // ==================== ACTUALIZAR INFORMACIÓN ====================

    /**
     * Actualiza el nombre de un usuario
     */
    public Usuario actualizarNombre(String email, String nuevoNombre) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(nuevoNombre);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    /**
     * Actualiza el teléfono de un usuario
     */
    public Usuario actualizarTelefono(String email, String telefono) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setTelefono(telefono);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    /**
     * Cambia la contraseña de un usuario
     * En producción, encriptar con BCrypt
     */
    public Usuario cambiarContrasena(String email, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(nuevaContrasena);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    /**
     * Cambia el rol de un usuario
     */
    public Usuario cambiarRol(String email, String nuevoRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setRol(nuevoRol);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    /**
     * Verifica el email de un usuario
     */
    public Usuario verificarEmail(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setVerificado(true);
            usuario.setFechaVerificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    // ==================== GESTIÓN DE ESTADO ====================

    /**
     * Desactiva un usuario (borrado lógico)
     */
    public Usuario desactivarUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    /**
     * Activa un usuario
     */
    public Usuario activarUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(true);
            usuario.setUltimaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado: " + email);
    }

    // ==================== VERIFICACIONES DE PERMISOS ====================

    /**
     * Verifica si un usuario puede crear solicitudes
     */
    public boolean puedeCrearSolicitud(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndActivoTrue(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return usuario.puedeCrearSolicitud();
        }

        return false;
    }

    /**
     * Verifica si un usuario puede procesar solicitudes
     */
    public boolean puedeVerSolicitudes(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndActivoTrue(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return usuario.puedeVerSolicitudes();
        }

        return false;
    }

    /**
     * Verifica si un usuario es administrador
     */
    public boolean esAdmin(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndActivoTrue(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return usuario.esAdmin();
        }

        return false;
    }

    // ==================== ESTADÍSTICAS ====================

    /**
     * Obtiene el total de usuarios activos
     */
    public Long obtenerTotalActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    /**
     * Obtiene el total de estudiantes
     */
    public Long obtenerTotalEstudiantes() {
        return usuarioRepository.countByTipoUsuarioAndActivoTrue("ESTUDIANTE");
    }

    /**
     * Obtiene el total de personal
     */
    public Long obtenerTotalPersonal() {
        return usuarioRepository.countActivePersonal();
    }

    /**
     * Obtiene el total de usuarios no verificados
     */
    public Long obtenerNoVerificadosCount() {
        return (long) usuarioRepository.findByVerificadoFalseAndActivoTrue().size();
    }
}