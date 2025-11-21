package com.unipanamericana.controller;

import com.unipanamericana.entity.Evento;
import com.unipanamericana.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "false")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<Evento> eventos = eventoRepository.findAll();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        if (evento.isPresent()) {
            return ResponseEntity.ok(evento.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/solicitud/{codigo}")
    public ResponseEntity<?> obtenerTimeline(@PathVariable String codigo) {
        List<Evento> eventos = eventoRepository.findByCodigoSolicitudOrderByFechaEventoAsc(codigo);

        Map<String, Object> response = new HashMap<>();
        response.put("codigo_solicitud", codigo);
        response.put("eventos", eventos);
        response.put("total", eventos.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/solicitud/{codigo}/desc")
    public ResponseEntity<?> obtenerTimelineDescendente(@PathVariable String codigo) {
        List<Evento> eventos = eventoRepository.findByCodigoSolicitudOrderByFechaEventoDesc(codigo);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorTipo(@PathVariable String tipo) {
        List<Evento> eventos = eventoRepository.findByTipoEventoOrderByFechaEventoDesc(tipo);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable String usuario) {
        List<Evento> eventos = eventoRepository.findByUsuarioOrderByFechaEventoDesc(usuario);
        return ResponseEntity.ok(eventos);
    }

    @PostMapping
    public ResponseEntity<?> crearEvento(@RequestBody Evento evento) {
        try {
            Evento guardado = eventoRepository.save(evento);
            return ResponseEntity.status(201).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEvento(
            @PathVariable Long id,
            @RequestBody Evento evento) {
        try {
            Optional<Evento> existente = eventoRepository.findById(id);
            if (existente.isPresent()) {
                evento.setId(id);
                Evento actualizado = eventoRepository.save(evento);
                return ResponseEntity.ok(actualizado);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        try {
            if (eventoRepository.existsById(id)) {
                eventoRepository.deleteById(id);
                return ResponseEntity.ok(Map.of("mensaje", "Evento eliminado"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}