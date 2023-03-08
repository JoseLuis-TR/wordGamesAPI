package com.api.wordgames.controller;

import com.api.wordgames.dto.JuegoDTO;
import com.api.wordgames.dto.JuegoModDTO;
import com.api.wordgames.dto.converter.JuegoDTOConverter;
import com.api.wordgames.model.Juego;
import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.services.JuegoServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoServices juegoServices;

    /**
     * Obtener todos los juegos
     *
     * @return lista de juegos o 204 si no hay juegos
     */
    @GetMapping("/juegos")
    public ResponseEntity<List<?>> getAllJuegos(){
        List<Juego> juegos = juegoServices.getAllJuegos();
        if (juegos.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            JuegoDTOConverter converter = new JuegoDTOConverter();
            List<JuegoDTO> juegoDTOList = juegos.stream().map(converter::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(juegoDTOList);
        }
    }

    /**
     * Obtener un juego en base a su ID
     *
     * @param id Identificador del juego a buscar
     * @return ResponseEntity con el status y el body, en caso de error,
     *          el body contiene el mensaje de error y el status 404
     */
    @GetMapping("/juego/{id}")
    public ResponseEntity<Object> getJuegoById(@PathVariable Long id){
        Optional<Juego> juegoBuscado = juegoServices.getJuegoById(id);
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            JsonResponse<Juego> response = new JsonResponse<>(HttpStatus.OK, "El juego existe", juegoBuscado.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Elimina un juego en base a su ID
     *
     * @param id Identificador del juego a eliminar
     * @return ResponseEntity con el status y el body, 404 si no encuentra el juego, 204 si se elimina correctamente
     */
    @DeleteMapping("/juego/{id}")
    public ResponseEntity<Object> deleteJuegoById(@PathVariable Long id){
        Optional<Juego> juegoBuscado = juegoServices.getJuegoById(id);
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            juegoServices.deleteJuego(juegoBuscado.get());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Crear un juego
     *
     * @param juego Juego a crear
     * @return ResponseEntity con el status y el body, en caso de error, el body contiene el mensaje de error
     *          y el status 400 si el nombre del juego ya existe, en caso de éxito el body contiene el juego creado y el status 201
     */
    @PostMapping(value = "/juego")
    public ResponseEntity<JsonResponse<Juego>> createJuego(@RequestBody JuegoModDTO juego){
        return juegoServices.saveJuego(juego);
    }

    /**
     * Actualizar un juego
     *
     * @param id Identificador del juego a actualizar
     * @param modJuego Juego con los datos a modificar
     * @return ResponseEntity con el status y el body, 404 si no encuentra el juego,
     *          400 si el nombre del juego ya existe, 200 si se actualiza correctamente
     */
    @PutMapping("/juego/{id}")
    public ResponseEntity<JsonResponse<Juego>> updateJuego(@PathVariable Long id, @RequestBody JuegoModDTO modJuego){
        return juegoServices.updateJuego(id, modJuego);
    }
}
