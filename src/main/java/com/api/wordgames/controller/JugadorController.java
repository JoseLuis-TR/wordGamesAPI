package com.api.wordgames.controller;

import com.api.wordgames.dto.JugadorDTO;
import com.api.wordgames.dto.JugadorModDTO;
import com.api.wordgames.dto.converter.JugadorDTOConverter;
import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.services.JugadorServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorServices jugadorServices;
    private final JugadorDTOConverter jugadorDTOConverter;

    /**
     * Obtenemos todos los jugadores
     *
     * @return lista de jugadores
     */
    @GetMapping("/jugadores")
    public ResponseEntity<List<?>> getAllJugadores(){
        List<Jugador> jugadores = jugadorServices.getAllJugadores();
        if (jugadores.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            List<JugadorDTO> jugadorDTOList = jugadores.stream().map(jugadorDTOConverter::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(jugadorDTOList);
        }
    }

    /**
     * Obtenemos un jugador en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el jugador
     */
    @GetMapping("jugador/{id}")
    public ResponseEntity<JsonResponse<Jugador>> getJugadorById(@PathVariable Long id){
        Optional<Jugador> jugadorBuscado = jugadorServices.getJugadorById(id);
        if (jugadorBuscado.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            JsonResponse<Jugador> response = new JsonResponse<>(HttpStatus.OK, "El jugador existe", jugadorBuscado.get());
            System.out.println(jugadorBuscado.get().getEquipo());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Eliminamos un jugador en base a su ID
     *
     * @param id
     * @return Error 404 si no encuentra el jugador, 204 si se elimina correctamente
     */
    @DeleteMapping("jugador/{id}")
    public ResponseEntity<JsonResponse<Jugador>> deleteJugadorById(@PathVariable Long id){
        Optional<Jugador> jugadorBuscado = jugadorServices.getJugadorById(id);
        if(jugadorBuscado.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            jugadorServices.deleteJugador(jugadorBuscado.get());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Creamos un jugador
     *
     * @param newJugador
     * @return Error 400 si el jugador ya existe
     */
    @PostMapping("/jugador")
    public ResponseEntity<JsonResponse<Jugador>> createJugador(@RequestBody JugadorModDTO newJugador){
        return jugadorServices.saveJugador(newJugador);
    }

    /**
     * Actualizamos un jugador
     *
     * @param newJugador
     * @param id
     * @return Error 404 si no encuentra el jugador, 200 si se actualiza correctamente
     */
    @PutMapping("/jugador/{id}")
    public ResponseEntity<JsonResponse<Jugador>> updateJugador(@RequestBody JugadorModDTO newJugador, @PathVariable Long id){
        return jugadorServices.updateJugador(id, newJugador);
    }
}
