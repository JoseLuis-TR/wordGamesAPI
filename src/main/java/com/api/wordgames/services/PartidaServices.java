package com.api.wordgames.services;

import com.api.wordgames.dto.PartidaModDTO;
import com.api.wordgames.model.Dificultad;
import com.api.wordgames.model.Juego;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.model.Partida;
import com.api.wordgames.repositories.JuegoRepository;
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
    @Autowired
    private JuegoRepository juegoRepository;

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
     * @param id Identificador de la partida
     * @return Error 404 si no encuentra la partida
     */
    public Optional<Partida> getPlayById(Long id){
        return partidaRepository.findById(id);
    }

    /**
     * Elimina una partida en base a su ID
     *
     * @param partida Partida a eliminar
     */
    public void deletePartida (Partida partida) {
        partidaRepository.delete(partida);
    }

    /**
     * Guarda una partida
     *
     * @param partida Partida a guardar
     */
    public ResponseEntity<JsonResponse<Partida>> savePartida (Jugador jugadorPartida, Juego juegoPartida, PartidaModDTO partida) {
        if(partida.getIntentos() == null
                || partida.getPalabra() == null
                || jugadorPartida == null){
            JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.BAD_REQUEST, "No se han enviado todos los datos", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            Partida partidaCreada = new Partida();
            partidaCreada.setJugador(jugadorPartida);
            partidaCreada.setJuego(juegoPartida);
            partidaCreada.setIntentos(partida.getIntentos());
            partidaCreada.setPuntos(calculoPuntosPalabra(partida.getPalabra(), partida.getIntentos(), juegoPartida.getIntentosmax() ,juegoPartida.getDificultad()));
            partidaCreada.setPalabra(partida.getPalabra());
            partidaCreada.setDatetime(LocalDateTime.now());
            JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.CREATED, "Partida creada correctamente", partidaRepository.save(partidaCreada));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    /**
     * Actualiza una partida
     *
     * @param modPartida Partida a modificar
     * @param partidaBuscada Partida a modificar
     * @return ResponseEntity con el status y el body, 400 si el nombre del jugador ya existe,
     *          201 si se modifica correctamente
     */
    public ResponseEntity<JsonResponse<Partida>> updatePartida(PartidaModDTO modPartida, Partida partidaBuscada){
        partidaBuscada.setIntentos(modPartida.getIntentos());
        partidaBuscada.setPuntos(calculoPuntosPalabra(modPartida.getPalabra(), modPartida.getIntentos(), partidaBuscada.getJuego().getIntentosmax() ,partidaBuscada.getJuego().getDificultad()));
        partidaBuscada.setPalabra(modPartida.getPalabra());
        partidaBuscada.setDatetime(LocalDateTime.now());
        JsonResponse<Partida> response = new JsonResponse<>(HttpStatus.CREATED, "Partida modificada correctamente", partidaRepository.save(partidaBuscada));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------- MÉTODOS AUXILIARES ----------------------
    /**
     * Calcula los puntos de una partida a partir de diversos multiplicadores basados en los intentos sobrantes
     * y la dificultad del juego
     *
     * @param palabra Palabra a adivinar
     * @param intentos Intentos realizados
     * @param intentosMax Intentos máximos
     * @param dificultad Dificultad del juego
     * @return Puntos de la partida
     */
    public Integer calculoPuntosPalabra(String palabra, Integer intentos, Integer intentosMax, Dificultad dificultad){
        int puntos = 0;
        Integer multiplicadorDificultad = multiplicadorDificultad(dificultad);
        Float multiplicadorDistintasLetras = multiplicadorDistintasLetras(palabra);
        Integer intentosRestantes = intentosMax - intentos;
        puntos = (int) (intentosRestantes * multiplicadorDificultad * multiplicadorDistintasLetras);
        return puntos;
    }

    /**
     * Calculo del multiplicador por dificultad del juego
     *
     * @param dificultad Dificultad del juego
     * @return Multiplicador por dificultad
     */
    public Integer multiplicadorDificultad(Dificultad dificultad){
        int multiplicadorDificultad = 0;
        switch (dificultad){
            case FACIL:
                multiplicadorDificultad = 1;
                break;
            case NORMAL:
                multiplicadorDificultad = 2;
                break;
            case DIFICIL:
                multiplicadorDificultad = 3;
                break;
        }
        return multiplicadorDificultad;
    }

    /**
     * Calculo del multiplicador por cantidad de letras distintas dentro de la palabra
     *
     * @param palabra Palabra a adivinar
     * @return Multiplicador por distintas letras
     */
    public Float multiplicadorDistintasLetras(String palabra){
        int distintasLetras = 0;
        for (int i = 0; i < palabra.length(); i++) {
            if(palabra.indexOf(palabra.charAt(i)) == i){
                distintasLetras++;
            }
        }
        return (float) (1 + (0.1 * distintasLetras));
    }
}
