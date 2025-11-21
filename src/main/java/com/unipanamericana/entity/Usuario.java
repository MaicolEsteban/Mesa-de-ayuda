package com.unipanamericana.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad Usuario - Representa a los usuarios del sistema
 * Puede ser un estudiante o personal administrativo
 *
 * Atributos:
 * - id: Identificador único
 * - email: Email único del usuario
 * - nombre: Nombre completo
 * - cedula: Número de cédula
 * - tipoUsuario: ESTUDIANTE o PERSONAL
 * - facultad: Facultad a la que pertenece
 * - rol: ROL_ESTUDIANTE, ROL_PERSONAL, ROL_ADMIN, etc.
 * - contrasena: Contraseña (será encriptada en producción)
 * - activo: Si el usuario puede acceder
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 15)
    private String cedula;

    @Column(nullable = false, length = 20)
    private String tipoUsuario; // ESTUDIANTE, PERSONAL

    @Column(length = 100)
    private String facultad; // Ej: Ingeniería, Administración, etc.

    @Column(length = 100)
    private String departamento; // Departamento si es personal (Ej: Registro y Control)

    @Column(nullable = false, length = 50)
    private String rol;
    // ROL_ESTUDIANTE: Acceso básico (crear solicitudes, ver estado)
    // ROL_PERSONAL: Acceso a panel de administración (ver, asignar, cambiar estado)
    // ROL_ADMIN: Acceso completo (crear usuarios, ver reportes, configuración)

    @Column(nullable = false, length = 255)
    private String contrasena; // Se almacenará encriptada en producción

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime ultimoAcceso;

    private LocalDateTime ultimaModificacion;

    @Column(nullable = false)
    private Boolean primerLogin = true;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false)
    private Boolean verificado = false; // Si verificó su email

    private LocalDateTime fechaVerificacion;

    // ==================== CONSTRUCTORES ====================

    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
        this.verificado = false;
    }

    public Usuario(String email, String nombre, String cedula, String tipoUsuario, String rol) {
        this();
        this.email = email;
        this.nombre = nombre;
        this.cedula = cedula;
        this.tipoUsuario = tipoUsuario;
        this.rol = rol;
    }

    public Usuario(String email, String nombre, String cedula, String tipoUsuario,
                   String facultad, String rol, String contrasena) {
        this(email, nombre, cedula, tipoUsuario, rol);
        this.facultad = facultad;
        this.contrasena = contrasena;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public Boolean getPrimerLogin() {
        return primerLogin;
    }

    public void setPrimerLogin(Boolean primerLogin) {
        this.primerLogin = primerLogin;
    }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public LocalDateTime getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(LocalDateTime ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }

    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }

    // ==================== MÉTODOS DE NEGOCIO ====================

    /**
     * Verifica si el usuario es estudiante
     */
    public Boolean esEstudiante() {
        return "ESTUDIANTE".equals(this.tipoUsuario);
    }

    /**
     * Verifica si el usuario es personal administrativo
     */
    public Boolean esPersonal() {
        return "PERSONAL".equals(this.tipoUsuario);
    }

    /**
     * Verifica si el usuario tiene rol de administrador
     */
    public Boolean esAdmin() {
        return "ROL_ADMIN".equals(this.rol);
    }

    /**
     * Verifica si el usuario tiene permisos para ver solicitudes
     */
    public Boolean puedeVerSolicitudes() {
        return this.activo && (this.rol.contains("ROL_PERSONAL") ||
                this.rol.equals("ROL_ADMIN"));
    }

    /**
     * Verifica si el usuario puede crear solicitudes
     */
    public Boolean puedeCrearSolicitud() {
        return this.activo && this.esEstudiante();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}