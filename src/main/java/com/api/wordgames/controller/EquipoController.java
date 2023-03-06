package com.api.wordgames.controller;

import com.api.wordgames.repositories.JugadorRepository;
import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.model.Equipo;
import com.api.wordgames.repositories.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;

    /**
     * Obtenemos todos los equipos
     *
     * @return lista de equipos
     */
    @GetMapping("/equipos")
    public ResponseEntity<Object> getAllEquipos(){
        List<Equipo> equipos = equipoRepository.findAll();
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
        Optional<Equipo> equipoBuscado = equipoRepository.findById(id);
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
        Optional<Equipo> equipoBuscado = equipoRepository.findById(id);
        if(equipoBuscado.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El equipo no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            equipoRepository.delete(equipoBuscado.get());
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
        List<Equipo> equiposExiste = equipoRepository.findByNombreEqualsIgnoreCase(newEquipo.getNombre());
        if(!equiposExiste.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El equipo ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }
        newEquipo.setPuntos(0);
        newEquipo.setLogo("");
        JsonResponse<Equipo> responseOk = new JsonResponse<>(HttpStatus.CREATED, "Equipo Creado", equipoRepository.save(newEquipo));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOk);
    }

    /**
     * Modificamos un equipo
     *
     * @param id
     * @param newEquipo
     * @return Error 404 si no encuentra el equipo
     */
    @PutMapping("/equipo/{id}")
    public ResponseEntity<JsonResponse<Equipo>> modifyEquipo(@PathVariable Long id, @RequestBody Equipo newEquipo){
        Optional<Equipo> equipoBuscado = equipoRepository.findById(id);
        List<Equipo> equiposExiste = equipoRepository.findByNombreEqualsIgnoreCase(newEquipo.getNombre());
        if(equipoBuscado.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El equipo no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else if(!equiposExiste.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nombre del equipo ya esta en uso", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            if (newEquipo.getNombre() != null
                    && !newEquipo.getNombre().isEmpty()
                    && !newEquipo.getNombre().equals(equipoBuscado.get().getNombre())){
                equipoBuscado.get().setNombre(newEquipo.getNombre());
            }
            if(newEquipo.getLogo() != null
                    && !newEquipo.getLogo().equals(equipoBuscado.get().getLogo())){
                equipoBuscado.get().setLogo(newEquipo.getLogo());
            }
            JsonResponse<Equipo> responseOk = new JsonResponse<>(HttpStatus.OK, "Equipo Modificado", equipoRepository.save(equipoBuscado.get()));
            return ResponseEntity.status(HttpStatus.OK).body(responseOk);
        }
    }

}
