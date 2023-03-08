package com.api.wordgames.controller;

import com.api.wordgames.dto.JugadorDTO;
import com.api.wordgames.dto.JugadorModDTO;
import com.api.wordgames.dto.converter.JugadorDTOConverter;
import com.api.wordgames.model.Equipo;
import com.api.wordgames.repositories.EquipoRepository;
import com.api.wordgames.repositories.JugadorRepository;
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
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;

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
     * @param id Identificador del jugador a buscar
     * @return Error 404 si no encuentra el jugador
     */
    @GetMapping("jugador/{id}")
    public ResponseEntity<JsonResponse<JugadorDTO>> getJugadorById(@PathVariable Long id){
        Optional<Jugador> jugadorBuscado = jugadorServices.getJugadorById(id);
        if (jugadorBuscado.isEmpty()){
            JsonResponse<JugadorDTO> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            JugadorDTO jugadorDTO = jugadorDTOConverter.convertToDTO(jugadorBuscado.get());
            JsonResponse<JugadorDTO> response = new JsonResponse<>(HttpStatus.OK, "El jugador existe", jugadorDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Obtenemos una clasificación de jugadores por puntos
     *
     * @return lista de jugadores ordenada por puntos
     */
    @GetMapping("/jugadores/top")
    public ResponseEntity<List<?>> getTopJugadores(){
        List<Jugador> jugadores = jugadorRepository.findAllByOrderByPuntosDesc();
        if (jugadores.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            List<JugadorDTO> jugadorDTOList = jugadores.stream().map(jugadorDTOConverter::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(jugadorDTOList);
        }
    }

    /**
     * Obtenemos todos los jugadores que pertenecen a un equipo
     *
     * @param id Identificador del equipo
     * @return lista de jugadores
     */
    @GetMapping("/jugadores/equipo/{id}")
    public ResponseEntity<List<?>> getJugadoresByEquipo(@PathVariable Long id){
        List<Jugador> jugadores = jugadorRepository.findAllByEquipo_Id(id);
        if (jugadores.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            List<JugadorDTO> jugadorDTOList = jugadores.stream().map(jugadorDTOConverter::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(jugadorDTOList);
        }
    }

    /**
     * Eliminamos un jugador en base a su ID
     *
     * @param id Identificador del jugador a eliminar
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
     * @param newJugador Jugador a crear
     * @return Error 400 si el jugador ya existe
     */
    @PostMapping("/jugador")
    public ResponseEntity<JsonResponse<Jugador>> createJugador(@RequestBody JugadorModDTO newJugador){
        return jugadorServices.saveJugador(newJugador);
    }

    /**
     * Actualizamos un jugador
     * Función muy mejorable. No me gusta como está hecha.
     *
     * @param newJugador Jugador a actualizar
     * @param id Identificador del jugador a actualizar
     * @return Error 404 si no encuentra el jugador, 200 si se actualiza correctamente
     */
    @PutMapping("/jugador/{id}")
    public ResponseEntity<JsonResponse<Jugador>> updateJugador(@RequestBody JugadorModDTO newJugador, @PathVariable Long id){
        // Iniciamos las dos posibilidades de actualizar el equipo al que pertenece el jugador
        Integer eliminarEquipo = null;
        Equipo equipoJugador = null;
        // Si se recibe nulo no se realizará ninguna modificación al equipo del jugador
        if(newJugador.getEquipoId() == null){
            return jugadorServices.updateJugador(id, newJugador, equipoJugador, eliminarEquipo);
        // Si se recibe 0 se eliminará el equipo al que pertenece el jugador
        } else if(newJugador.getEquipoId() == 0) {
            eliminarEquipo = 0;
            return jugadorServices.updateJugador(id, newJugador, equipoJugador, eliminarEquipo);
        // Si se recibe un ID de equipo se busca el equipo y se actualiza el jugador
        } else {
            equipoJugador = equipoRepository.findById(newJugador.getEquipoId()).orElse(null);
            return jugadorServices.updateJugador(id, newJugador, equipoJugador, eliminarEquipo);
        }
    }
}
