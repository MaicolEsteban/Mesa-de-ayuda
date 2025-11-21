package com.unipanamericana.dto;

public class CreateUserDTO {
    private String email;
    private String nombre;
    private String cedula;
    private String tipoUsuario; // ESTUDIANTE, PERSONAL
    private String rol;
    private String contrasena;

    public CreateUserDTO() {}

    public CreateUserDTO(String email, String nombre, String cedula, String tipoUsuario, String rol, String contrasena) {
        this.email = email;
        this.nombre = nombre;
        this.cedula = cedula;
        this.tipoUsuario = tipoUsuario;
        this.rol = rol;
        this.contrasena = contrasena;
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
}