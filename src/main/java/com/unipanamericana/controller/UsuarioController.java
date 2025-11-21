package com.unipanamericana.controller;

import com.unipanamericana.entity.Usuario;
import com.unipanamericana.repository.UsuarioRepository;
import com.unipanamericana.service.UsuarioService;
import com.unipanamericana.dto.CreateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "false")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ========== ENDPOINT ESPECIAL: RESET PASSWORD ==========

    @PostMapping("/reset-password/{email}/{newPassword}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @PathVariable String newPassword) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (usuario.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("error", "Usuario no encontrado")
                );
            }

            Usuario user = usuario.get();
            user.setContrasena(passwordEncoder.encode(newPassword));
            usuarioRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "mensaje", "Contraseña actualizada",
                    "usuario", user.getEmail()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== LOGIN ==========



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String contrasena) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (usuario.isEmpty()) {
                return ResponseEntity.status(401).body(
                        Map.of("error", "Usuario no encontrado", "success", false)
                );
            }

            Usuario user = usuario.get();

            if (!passwordEncoder.matches(contrasena, user.getContrasena())) {
                return ResponseEntity.status(401).body(
                        Map.of("error", "Contraseña incorrecta", "success", false)
                );
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Login exitoso");
            response.put("usuario", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "nombre", user.getNombre(),
                    "rol", user.getRol(),
                    "tipo", user.getTipoUsuario()
            ));
            response.put("token", "token-" + user.getId() + "-" + System.currentTimeMillis());

            // Verificar si es primer login y NO es admin
            boolean debesCambiarContrasena = user.getPrimerLogin() != null && user.getPrimerLogin() &&
                    !user.getRol().equals("ROL_ADMIN");

            response.put("debesCambiarContrasena", debesCambiarContrasena);

            // Si no es primer login o es admin, registrar el acceso
            if (!debesCambiarContrasena) {
                user.setUltimoAcceso(LocalDateTime.now());
                usuarioRepository.save(user);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", e.getMessage(), "success", false)
            );
        }
    }



    @PostMapping("/cambiar-contrasena-primer-login/{id}")
    public ResponseEntity<?> cambiarContrasenaPrimerLogin(
            @PathVariable Long id,
            @RequestParam String nuevaContrasena) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("error", "Usuario no encontrado")
                );
            }

            Usuario usuario = usuarioOpt.get();

            // Validar que sea primer login
            if (usuario.getPrimerLogin() == null || !usuario.getPrimerLogin()) {
                return ResponseEntity.status(400).body(
                        Map.of("error", "Este usuario ya cambió su contraseña")
                );
            }

            // Encriptar nueva contraseña
            usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
            usuario.setPrimerLogin(false); // Marcar que ya no es primer login
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuario.setUltimaModificacion(LocalDateTime.now());

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "mensaje", "Contraseña actualizada correctamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== OBTENER USUARIO POR EMAIL ==========

    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    // ========== OBTENER TODOS LOS USUARIOS ==========

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // ========== OBTENER USUARIO POR ID ==========

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    // ========== OBTENER USUARIOS POR TIPO ==========

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorTipo(@PathVariable String tipo) {
        List<Usuario> usuarios = usuarioRepository.findByTipoUsuario(tipo);
        return ResponseEntity.ok(usuarios);
    }

    // ========== ESTADÍSTICAS ==========

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_usuarios", usuarioRepository.count());
        stats.put("estudiantes", usuarioRepository.countByTipoUsuario("ESTUDIANTE"));
        stats.put("personal", usuarioRepository.countByTipoUsuario("PERSONAL"));
        stats.put("activos", usuarioRepository.countByActivoTrue());
        return ResponseEntity.ok(stats);
    }

    // ========== OBTENER ESTUDIANTES ==========

    @GetMapping("/estudiantes")
    public ResponseEntity<?> obtenerEstudiantes() {
        List<Usuario> estudiantes = usuarioService.obtenerEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }

    // ========== OBTENER PERSONAL ==========

    @GetMapping("/personal")
    public ResponseEntity<?> obtenerPersonal() {
        List<Usuario> personal = usuarioService.obtenerPersonal();
        return ResponseEntity.ok(personal);
    }

    // ========== CREAR USUARIO CON DTO ==========

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody CreateUserDTO dto) {
        try {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.status(400).body(
                        Map.of("error", "El email ya existe")
                );
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(dto.getEmail());
            usuario.setNombre(dto.getNombre());
            usuario.setCedula(dto.getCedula());
            usuario.setTipoUsuario(dto.getTipoUsuario());
            usuario.setRol(dto.getRol());
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            usuario.setActivo(true);
            usuario.setVerificado(true);

            Usuario guardado = usuarioRepository.save(usuario);

            return ResponseEntity.status(201).body(
                    Map.of(
                            "id", guardado.getId(),
                            "email", guardado.getEmail(),
                            "nombre", guardado.getNombre(),
                            "mensaje", "Usuario creado exitosamente"
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== CREAR ESTUDIANTE CON FACULTAD ==========

    @PostMapping("/estudiante")
    public ResponseEntity<?> crearEstudiante(
            @RequestParam String email,
            @RequestParam String nombre,
            @RequestParam String cedula,
            @RequestParam String facultad,
            @RequestParam String contrasena) {
        try {
            // Validar que no exista otro usuario con el mismo email
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(400).body(
                        Map.of("error", "El email ya está registrado")
                );
            }

            // Crear nuevo estudiante
            Usuario nuevoEstudiante = new Usuario();
            nuevoEstudiante.setEmail(email);
            nuevoEstudiante.setNombre(nombre);
            nuevoEstudiante.setCedula(cedula);
            nuevoEstudiante.setFacultad(facultad);
            nuevoEstudiante.setContrasena(passwordEncoder.encode(contrasena));
            nuevoEstudiante.setTipoUsuario("ESTUDIANTE");
            nuevoEstudiante.setRol("ESTUDIANTE");

            Usuario guardado = usuarioRepository.save(nuevoEstudiante);

            System.out.println(" Estudiante creado: " + guardado.getEmail());

            return ResponseEntity.status(201).body(guardado);
        } catch (Exception e) {
            System.err.println(" Error al crear estudiante: " + e.getMessage());
            return ResponseEntity.status(400).body(
                    Map.of("error", "Error al crear estudiante: " + e.getMessage())
            );
        }
    }

    // ========== ACTUALIZAR USUARIO ==========


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> existente = usuarioRepository.findById(id);
            if (existente.isPresent()) {
                Usuario usuarioActual = existente.get();

                //  Si la contraseña cambió, encriptarla
                if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                    // Verificar si es diferente a la actual (para no re-encriptar)
                    if (!usuario.getContrasena().startsWith("$2a$") && !usuario.getContrasena().startsWith("$2b$")) {
                        // Es texto plano, encriptar
                        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                    }
                }

                usuario.setId(id);
                Usuario actualizado = usuarioRepository.save(usuario);
                return ResponseEntity.ok(actualizado);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== ELIMINAR USUARIO ==========

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}