package com.api.wordgames.services;

import com.api.wordgames.dto.PartidaModDTO;
import com.api.wordgames.model.Juego;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.model.Partida;
import com.api.wordgames.repositories.PartidaRepository;
import com.api.wordgames.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PartidaServices {

    /**
     * Repositorio de Partidas
     * @see PartidaRepository
     */
    @Autowired
    private PartidaRepository partidaRepository;

    /**
     * Obtenemos todas las partidas
     *
     * @return lista de partidas
     */
    public List<Partida> getAllPlays(){
        return partidaRepository.findAll();
    }

    /**
     * Obtenemos una partida por su ID
     *
     * @param id
     * @return Error 404 si no encuentra la partida
     */
    public Optional<Partida> getPlayById(Long id){
        return partidaRepository.findById(id);
    }

    /**
     * Elimina una partida en base a su ID
     *
     * @param partida
     */
    public void deletePartida (Partida partida) {
        partidaRepository.delete(partida);
    }

    /**
     * Guarda una partida
     *
     * @param partida
     */
    public ResponseEntity<JsonResponse<Partida>> savePartida (Jugador jugadorPart, Juego juegoPart, PartidaModDTO partida) {
        Partida partidaCreada = new Partida();
        partidaCreada.setJugador(jugadorPart);
        partidaCreada.setJuego(juegoPart);
        partidaCreada.setIntentos(partida.getIntentos());
        partidaCreada.setPuntos(partida.getPuntos());
        partidaCreada.setPalabra(partida.getPalabra());
        partidaCreada.setDatetime(LocalDateTime.now());
        JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.CREATED, "Partida creada correctamente", partidaRepository.save(partidaCreada));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
