package com.espe.micro.asignaturas.controllers;

import com.espe.micro.asignaturas.models.entities.Asignatura;
import com.espe.micro.asignaturas.services.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    @Autowired
    AsignaturaService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Asignatura asignatura, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            result.getFieldErrors().forEach(
                    error -> errors.put(
                            error.getField(), error.getDefaultMessage()
                            ));
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(asignatura));
    }

    @GetMapping
    public List<Asignatura> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> buscarPorId(@PathVariable Long id) {
        Optional<Asignatura> asignaturaOptional = service.findById(id);
        if (asignaturaOptional.isPresent()) {
            return ResponseEntity.ok().body(asignaturaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Asignatura asignatura, @PathVariable Long id) {
        Optional<Asignatura> asignaturaOptional = service.findById(id);
        if (asignaturaOptional.isPresent()) {
            Asignatura asignaturaDB = asignaturaOptional.get();
            asignaturaDB.setNombre(asignatura.getNombre());
            asignaturaDB.setCodigo(asignatura.getCodigo());
            asignaturaDB.setDescripcion(asignatura.getDescripcion());
            asignaturaDB.setCreditos(asignatura.getCreditos());
            asignaturaDB.setNivel(asignatura.getNivel());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(asignaturaDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Asignatura> asignaturaOptional = service.findById(id);
        if (asignaturaOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}