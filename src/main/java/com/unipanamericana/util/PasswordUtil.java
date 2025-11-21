package com.unipanamericana.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidades para encriptar y validar contraseñas
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encripta una contraseña
     */
    public static String encriptarContrasena(String contrasena) {
        return encoder.encode(contrasena);
    }

    /**
     * Valida una contraseña contra un hash
     */
    public static boolean validarContrasena(String contrasena, String hash) {
        return encoder.matches(contrasena, hash);
    }
}