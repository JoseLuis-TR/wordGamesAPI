package com.api.wordgames.controller;

import com.api.wordgames.dto.PartidaDTO;
import com.api.wordgames.dto.PartidaModDTO;
import com.api.wordgames.dto.converter.PartidaDTOConverter;
import com.api.wordgames.model.Juego;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.model.Partida;
import com.api.wordgames.repositories.JuegoRepository;
import com.api.wordgames.repositories.JugadorRepository;
import com.api.wordgames.repositories.PartidaRepository;
import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.services.PartidaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaRepository partidaRepository;
    private final PartidaServices partidaServices;
    private final PartidaDTOConverter partidaDTOConverter;
    private final JugadorRepository jugadorRepository;
    private final JuegoRepository juegoRepository;

    /**
     * Obtenemos todas las partidas
     *
     * @return lista de partidas
     */
    @GetMapping("/partidas")
    public ResponseEntity<List<?>> getAllPlays(){
        List<Partida> partidas = partidaServices.getAllPlays();
        if(partidas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            List<PartidaDTO> partidaDTOList = partidas.stream().map(partidaDTOConverter::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(partidaDTOList);
        }
    }

    /**
     * Obtenemos una partida por su ID
     *
     * @param id
     * @return Error 404 si no encuentra la partida
     */
    @GetMapping("/partida/{id}")
    public ResponseEntity<JsonResponse<Partida>> getPartidaById(@PathVariable Long id){
        Optional<Partida> partida = partidaServices.getPlayById(id);
        if(partida.isEmpty()){
            JsonResponse<Partida> error = new JsonResponse<>(HttpStatus.NOT_FOUND, "Partida no encontrada", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.OK, "Partida encontrada", partida.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Eliminamos una partida por su ID
     *
     * @param id
     * @return Error 404 si no encuentra la partida
     */
    @DeleteMapping("/partida/{id}")
    public ResponseEntity<JsonResponse<Partida>> deletePartidaById(@PathVariable Long id){
        Optional<Partida> partidaBuscada = partidaServices.getPlayById(id);
        if(partidaBuscada.isEmpty()){
            JsonResponse<Partida> error = new JsonResponse<>(HttpStatus.NOT_FOUND, "Partida no encontrada", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            partidaServices.deletePartida(partidaBuscada.get());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Creamos una partida
     *
     * @param newPartida
     * @return Error 400 si no se puede crear la partida
     */
    @PostMapping("/partida")
    public ResponseEntity<JsonResponse<Partida>> createPartida(@RequestBody PartidaModDTO newPartida){
        Jugador jugadorPartida = jugadorRepository.findById(newPartida.getJugadorId()).orElse(null);
        Juego juegoPartida = juegoRepository.findById(newPartida.getJuegoId()).orElse(null);
        if(jugadorPartida == null && juegoPartida == null){
            JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.NOT_FOUND, "No se ha encontrado el juego o el jugador", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            Partida partidaCreada = new Partida();
            partidaCreada.setJugador(jugadorPartida);
            partidaCreada.setJuego(juegoPartida);
            partidaCreada.setIntentos(newPartida.getIntentos());
            partidaCreada.setPuntos(newPartida.getPuntos());
            partidaCreada.setPalabra(newPartida.getPalabra());
            partidaCreada.setDatetime(LocalDateTime.now());
            System.out.println(partidaCreada.getPuntos().getClass());
            JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.CREATED, "Partida creada correctamente", partidaRepository.save(partidaCreada));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
