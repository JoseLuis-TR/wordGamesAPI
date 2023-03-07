package com.api.wordgames.controller;

import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.model.Equipo;
import com.api.wordgames.services.EquipoServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoServices equipoServices;

    /**
     * Obtenemos todos los equipos
     *
     * @return lista de equipos
     */
    @GetMapping("/equipos")
    public ResponseEntity<Object> getAllEquipos(){
        List<Equipo> equipos = equipoServices.getAllEquipos();
        if (equipos.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(equipos);
        }
    }

    /**
     * Obtenemos un equipo en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el equipo
     */
    @GetMapping("equipo/{id}")
    public ResponseEntity<JsonResponse<Equipo>> getEquipoById(@PathVariable Long id){
        Optional<Equipo> equipoBuscado = equipoServices.getEquipoById(id);
        if (equipoBuscado.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El equipo no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            JsonResponse<Equipo> response = new JsonResponse<>(HttpStatus.OK, "El equipo existe", equipoBuscado.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Eliminamos un equipo en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el equipo, 204 si se elimina correctamente
     */
    @DeleteMapping("equipo/{id}")
    public ResponseEntity<JsonResponse<Equipo>> deleteEquipoById(@PathVariable Long id){
        Optional<Equipo> equipoBuscado = equipoServices.getEquipoById(id);
        if(equipoBuscado.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El equipo no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            equipoServices.deleteEquipo(equipoBuscado.get());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Creamos un equipo
     *
     * @param newEquipo
     * @return Error 400 si el equipo ya existe
     */
    @PostMapping("/equipo")
    public ResponseEntity<JsonResponse<Equipo>> createEquipo(@RequestBody Equipo newEquipo){
        return equipoServices.saveEquipo(newEquipo);
    }

    /**
     * Modificamos un equipo
     *
     * @param id
     * @param modEquipo
     * @return ResponseEntity con status y body, 404 si no encuentra el equipo, 200 si se actualiza correctamente
     */
    @PutMapping("/equipo/{id}")
    public ResponseEntity<JsonResponse<Equipo>> modifyEquipo(@PathVariable Long id, @RequestBody Equipo modEquipo){
        return equipoServices.updateEquipo(id, modEquipo);
    }

}
