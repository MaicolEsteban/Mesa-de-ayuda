package com.unipanamericana.controller;

import com.unipanamericana.entity.Solicitud;
import com.unipanamericana.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unipanamericana.service.EmailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "false")
public class SolicitudController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @GetMapping
    public ResponseEntity<?> obtenerTodas() {
        List<Solicitud> solicitudes = solicitudRepository.findAll();
        // Calcular horas restantes para cada solicitud
        solicitudes.forEach(s -> s.calcularHorasRestantes());
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigo) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            // Calcular horas restantes
            s.calcularHorasRestantes();
            return ResponseEntity.ok(s);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/estudiante")
    public ResponseEntity<?> obtenerMisSolicitudes(@RequestParam String email) {
        List<Solicitud> solicitudes = solicitudRepository.findByEmailEstudiante(email);
        // Calcular horas restantes
        solicitudes.forEach(s -> s.calcularHorasRestantes());

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("solicitudes", solicitudes);
        response.put("total", solicitudes.size());

        return ResponseEntity.ok(response);
    }

    // Obtener solicitudes asignadas a un revisor
    @GetMapping("/responsable/{email}")
    public ResponseEntity<?> obtenerSolicitudesPorResponsable(@PathVariable String email) {
        System.out.println("📡 Obteniendo solicitudes para revisor: " + email);
        List<Solicitud> solicitudes = solicitudRepository.findByEmailResponsable(email);
        // Calcular horas restantes
        solicitudes.forEach(s -> s.calcularHorasRestantes());
        System.out.println("✅ Solicitudes encontradas: " + solicitudes.size());
        return ResponseEntity.ok(solicitudes);
    }

    // ========== OBTENER SOLICITUDES POR TIPO DE USUARIO ==========

    @GetMapping("/porUsuario")
    public ResponseEntity<?> obtenerSolicitudesPorUsuario(
            @RequestParam String email,
            @RequestParam String tipoUsuario) {

        List<Solicitud> solicitudes;

        System.out.println("\n=== FILTRO DE SOLICITUDES ===");
        System.out.println("Email: " + email);
        System.out.println("Tipo Usuario: " + tipoUsuario);

        // Mapear PERSONAL a ADMINISTRADOR
        if ("ADMINISTRADOR".equalsIgnoreCase(tipoUsuario) || "PERSONAL".equalsIgnoreCase(tipoUsuario)) {
            // Administrador ve TODAS las solicitudes
            System.out.println("✅ Modo: ADMINISTRADOR (ve todas)");
            solicitudes = solicitudRepository.findAll();
            System.out.println("Total solicitudes: " + solicitudes.size());

        } else if ("REVISOR".equalsIgnoreCase(tipoUsuario)) {
            // Revisor solo ve solicitudes ASIGNADAS específicamente a él
            System.out.println("✅ Modo: REVISOR (ve solo asignadas)");

            // Obtener TODAS las solicitudes
            List<Solicitud> todas = solicitudRepository.findAll();
            System.out.println("Total solicitudes en BD: " + todas.size());

            // Filtrar SOLO las que ESTÁN asignadas a este revisor (NO NULL, EXACTA coincidencia)
            solicitudes = todas.stream()
                    .filter(s -> s.getEmailResponsable() != null &&
                            !s.getEmailResponsable().isEmpty() &&
                            s.getEmailResponsable().trim().equals(email.trim()))
                    .collect(Collectors.toList());

            System.out.println("Solicitudes asignadas a " + email + ": " + solicitudes.size());


            // Debug: mostrar emails responsables
            System.out.println("Detalles de filtrado:");
            todas.forEach(s -> {
                System.out.println("  - Código: " + s.getCodigo() +
                        ", Responsable: " + s.getEmailResponsable() +
                        ", Coincide: " + (s.getEmailResponsable() != null && s.getEmailResponsable().equals(email)));
            });

        } else if ("ESTUDIANTE".equalsIgnoreCase(tipoUsuario)) {
            // Estudiante solo ve sus propias solicitudes
            System.out.println("✅ Modo: ESTUDIANTE (ve solo sus solicitudes)");
            solicitudes = solicitudRepository.findByEmailEstudiante(email);
            System.out.println("Total solicitudes del estudiante: " + solicitudes.size());

        } else {
            System.out.println(" Tipo de usuario no reconocido: " + tipoUsuario);
            solicitudes = new java.util.ArrayList<>();
        }

        //Calcular horas restantes
        solicitudes.forEach(s -> s.calcularHorasRestantes());

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("tipo_usuario", tipoUsuario);
        response.put("solicitudes", solicitudes);
        response.put("total", solicitudes.size());

        System.out.println("Total retornado: " + solicitudes.size());
        System.out.println("============================\n");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable String estado) {
        List<Solicitud> solicitudes = solicitudRepository.findByEstado(estado);
        // ⭐ NUEVO: Calcular horas restantes
        solicitudes.forEach(s -> s.calcularHorasRestantes());
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorTipo(@PathVariable String tipo) {
        List<Solicitud> solicitudes = solicitudRepository.findByTipoSolicitud(tipo);
        // Calcular horas restantes
        solicitudes.forEach(s -> s.calcularHorasRestantes());
        return ResponseEntity.ok(solicitudes);
    }

    // ========== MÉTRICAS ==========

    @GetMapping("/metricas/dashboard")
    public ResponseEntity<?> metricasDashboard() {
        Map<String, Object> metricas = new HashMap<>();

        metricas.put("total", solicitudRepository.count());
        metricas.put("radicadas", solicitudRepository.countByEstado("RADICADA"));
        metricas.put("en_revision", solicitudRepository.countByEstado("EN_REVISION"));
        metricas.put("resueltas", solicitudRepository.countByEstado("RESUELTA"));
        metricas.put("esperando_info", solicitudRepository.countByEstado("ESPERANDO_INFO"));
        metricas.put("sin_asignar", solicitudRepository.findSinAsignar().size());
        metricas.put("calificacion_promedio", solicitudRepository.findAverageCalificacion());

        return ResponseEntity.ok(metricas);
    }

    @GetMapping("/metricas/por-estado")
    public ResponseEntity<?> metricasPorEstado() {
        List<?> estadisticas = solicitudRepository.estadisticasPorEstado();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/metricas/por-tipo")
    public ResponseEntity<?> metricasPorTipo() {
        List<?> estadisticas = solicitudRepository.estadisticasPorTipo();
        return ResponseEntity.ok(estadisticas);
    }

    //  Obtener solicitudes vencidas
    @GetMapping("/vencidas")
    public ResponseEntity<?> obtenerSolicitudesVencidas() {
        List<Solicitud> vencidas = solicitudRepository.findByVencidaTrue();
        // Calcular horas restantes
        vencidas.forEach(s -> s.calcularHorasRestantes());
        return ResponseEntity.ok(vencidas);
    }

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody Solicitud solicitud) {
        try {
            // Calcular fecha límite (72 horas = 3 días)
            solicitud.setFechaRadicacion(LocalDateTime.now());
            solicitud.setFechaLimiteSolucion(LocalDateTime.now().plusHours(72));
            solicitud.calcularHorasRestantes();

            Solicitud guardada = solicitudRepository.save(solicitud);

            // Enviar email al estudiante
            try {
                emailService.enviarEmailCreacionSolicitud(
                        guardada.getEmailEstudiante(),
                        guardada.getNombreEstudiante(),
                        guardada.getCodigo()
                );
            } catch (Exception ex) {
                System.err.println("⚠️ Error enviando email: " + ex.getMessage());
            }

            return ResponseEntity.status(201).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarSolicitud(
            @PathVariable String codigo,
            @RequestBody Solicitud solicitud) {
        try {
            Optional<Solicitud> existente = solicitudRepository.findById(codigo);
            if (existente.isPresent()) {
                solicitud.setCodigo(codigo);
                // Calcular horas restantes
                solicitud.calcularHorasRestantes();
                Solicitud actualizada = solicitudRepository.save(solicitud);
                return ResponseEntity.ok(actualizada);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminarSolicitud(@PathVariable String codigo) {
        try {
            Optional<Solicitud> solicitud = solicitudRepository.findById(codigo);
            if (solicitud.isPresent()) {
                solicitudRepository.deleteById(codigo);
                return ResponseEntity.ok(Map.of("mensaje", "Solicitud eliminada"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== ASIGNAR RESPONSABLE ==========

    @PutMapping("/{codigo}/asignar")
    public ResponseEntity<?> asignarResponsable(
            @PathVariable String codigo,
            @RequestParam String emailResponsable) {
        try {
            Optional<Solicitud> solicitudOpt = solicitudRepository.findById(codigo);
            if (solicitudOpt.isPresent()) {
                Solicitud solicitud = solicitudOpt.get();
                solicitud.setEmailResponsable(emailResponsable);
                // Calcular horas restantes
                solicitud.calcularHorasRestantes();
                Solicitud actualizada = solicitudRepository.save(solicitud);

                // Enviar email al responsable (opcional)
                try {
                    emailService.enviarEmailAsignacionResponsable(
                            emailResponsable,
                            codigo,
                            solicitud.getNombreEstudiante()
                    );
                } catch (Exception ex) {
                    System.err.println(" Error enviando email: " + ex.getMessage());
                }

                return ResponseEntity.ok(actualizada);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // ========== CAMBIAR ESTADO ==========

    @PutMapping("/{codigo}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable String codigo,
            @RequestParam String estado) {
        try {
            Optional<Solicitud> solicitudOpt = solicitudRepository.findById(codigo);
            if (solicitudOpt.isPresent()) {
                Solicitud solicitud = solicitudOpt.get();
                String estadoAnterior = solicitud.getEstado();
                solicitud.setEstado(estado);
                // Calcular horas restantes
                solicitud.calcularHorasRestantes();
                Solicitud actualizada = solicitudRepository.save(solicitud);

                // Enviar email al estudiante notificando cambio de estado (opcional)
                try {
                    emailService.enviarEmailCambioEstado(
                            solicitud.getEmailEstudiante(),
                            solicitud.getNombreEstudiante(),
                            codigo,
                            estado
                    );
                } catch (Exception ex) {
                    System.err.println(" Error enviando email: " + ex.getMessage());
                }

                return ResponseEntity.ok(actualizada);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}