package com.api.wordgames.controller;

import com.api.wordgames.model.Dificultad;
import com.api.wordgames.repositories.JuegoRepository;
import com.api.wordgames.model.Juego;
import com.api.wordgames.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoRepository juegoRepository;

    /**
     * Obtener todos los juegos
     *
     * @return lista de juegos
     */
    @GetMapping("/juegos")
    public ResponseEntity<Object> getAllJuegos(){
        List<Juego> juegos = juegoRepository.findAll();
        if (juegos.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(juegos);
        }
    }

    /**
     * Obtener un juego en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el juego
     */
    @GetMapping("/juego/{id}")
    public ResponseEntity<Object> getJuegoById(@PathVariable Long id){
        Optional<Juego> juegoBuscado = juegoRepository.findById(id);
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            JsonResponse<Juego> response = new JsonResponse<>(HttpStatus.OK, "El juego existe", juegoBuscado.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Eliminar un juego en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el juego, 204 si se elimina correctamente
     */
    @DeleteMapping("/juego/{id}")
    public ResponseEntity<Object> deleteJuegoById(@PathVariable Long id){
        Optional<Juego> juegoBuscado = juegoRepository.findById(id);
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            juegoRepository.delete(juegoBuscado.get());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Crear un juego
     *
     * @param juego
     * @return Error 400 si el juego ya existe, 201 si se crea correctamente
     */
    @PostMapping("/juego")
    public ResponseEntity<Object> createJuego(@RequestBody Juego juego){
        List<Juego> juegoBuscado = juegoRepository.findByNombreEqualsIgnoreCase(juego.getNombre());
        if (!juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El juego ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        } else {
            JsonResponse<Juego> response = new JsonResponse<>(HttpStatus.CREATED, "El juego se ha creado correctamente", juegoRepository.save(juego));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    /**
     * Actualizar un juego
     *
     * @param id
     * @param modJuego
     * @return Error 404 si no encuentra el juego, 200 si se actualiza correctamente
     */
    @PutMapping("/juego/{id}")
    public ResponseEntity<Object> updateJuego(@PathVariable Long id, @RequestBody Juego modJuego){
        Optional<Juego> juegoBuscado = juegoRepository.findById(id);
        List<Juego> juegoExiste = juegoRepository.findByNombreEqualsIgnoreCase(modJuego.getNombre());
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else if(!juegoExiste.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nombre del juego ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            if(modJuego.getNombre() != null
                    && !modJuego.getNombre().isEmpty()
                    && !modJuego.getNombre().equals(juegoBuscado.get().getNombre())){
                juegoBuscado.get().setNombre(modJuego.getNombre());
            }
            if(modJuego.getInstrucciones() != null
                    && !modJuego.getInstrucciones().isEmpty()
                    && !modJuego.getInstrucciones().equals(juegoBuscado.get().getInstrucciones())){
                juegoBuscado.get().setInstrucciones(modJuego.getInstrucciones());
            }
            if(modJuego.getIntentosmax() != null
                    && modJuego.getIntentosmax() > 0
                    && !modJuego.getIntentosmax().equals(juegoBuscado.get().getIntentosmax())){
                juegoBuscado.get().setIntentosmax(modJuego.getIntentosmax());
            }
            if(modJuego.getDificultad() != null){
                try{
                    Dificultad.valueOf(modJuego.getDificultad().toString());
                    if (!modJuego.getDificultad().equals(juegoBuscado.get().getDificultad())) {
                        juegoBuscado.get().setDificultad(modJuego.getDificultad());
                    }
                } catch (IllegalArgumentException e){
                    JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "La dificultad no es correcta", null);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
                }
            }
            JsonResponse<Juego> response = new JsonResponse<>(HttpStatus.OK, "El juego se ha actualizado correctamente", juegoRepository.save(juegoBuscado.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
