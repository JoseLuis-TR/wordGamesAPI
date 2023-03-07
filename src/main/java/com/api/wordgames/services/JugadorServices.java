package com.api.wordgames.services;

import com.api.wordgames.dto.JugadorModDTO;
import com.api.wordgames.model.Equipo;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.repositories.JugadorRepository;
import com.api.wordgames.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JugadorServices {

    /**
     * Repositorio de Jugadores
     * @see JugadorRepository
     */
    @Autowired
    private JugadorRepository jugadorRepository;
    private EquipoServices equipoServices;

    /**
     * Obtenemos todos los jugadores
     *
     * @return lista de jugadores
     */
    public List<Jugador> getAllJugadores(){
        return jugadorRepository.findAll();
    }

    /**
     * Obtenemos un jugador en base a su ID
     *
     * @param id
     * @return Jugador o error 404 si no encuentra el jugador
     */
    public Optional<Jugador> getJugadorById(Long id){
        return jugadorRepository.findById(id);
    }

    /**
     * Elimina un jugador en base a su ID
     *
     * @param jugador
     */
    public void deleteJugador (Jugador jugador){
        jugadorRepository.delete(jugador);
    }

    /**
     * Crea un nuevo jugador
     *
     * @param newJugador
     * @return ResponseEntity con el status y el body, 400 si el nombre del jugador ya existe,
     *          201 si se crea correctamente
     */
    public ResponseEntity<JsonResponse<Jugador>> saveJugador(JugadorModDTO newJugador){
        List<Jugador> jugadorExiste = jugadorRepository.findByNombreusuEqualsIgnoreCase(newJugador.getNombreusu());
        // Comprobamos que el jugador no exista
        if (!jugadorExiste.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El jugador ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }
        // Creamos el jugador
        Jugador jugadorCreado = new Jugador();
        jugadorCreado.setNombreusu(newJugador.getNombreusu());
        jugadorCreado.setClave(newJugador.getClave());
        jugadorCreado.setAdmin(newJugador.getAdmin());
        // Nos aseguramos de mantener en la creación los valores por defecto
        jugadorCreado.setPuntos(0);
        jugadorCreado.setAvatar("");
        jugadorCreado.setEquipo(null);
        JsonResponse<Jugador> response = new JsonResponse<>(HttpStatus.CREATED, "El jugador se ha creado correctamente", jugadorRepository.save(jugadorCreado));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Modifica un jugador
     *
     * @param jugadorMod
     * @return ResponseEntity con el status y el body, 404 si el jugador no existe, 400 si el nombre de usuario ya existe,
     * 200 si se modifica correctamente
     */
    public ResponseEntity<JsonResponse<Jugador>> updateJugador(Long id, JugadorModDTO jugadorMod){
        Optional<Jugador> jugadorBuscado = getJugadorById(id);
        List<Jugador> jugadorExiste = jugadorRepository.findByNombreusuEqualsIgnoreCase(jugadorMod.getNombreusu());
        // Comprobamos que el jugador exista
        if (jugadorBuscado.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
            // Comprobamos que el nick de usuario no este en uso por otro usuario
        } else if (!jugadorExiste.isEmpty() && !Objects.equals(jugadorBuscado.get().getNombreusu(), jugadorMod.getNombreusu())) {
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nick de usuario ya esta en uso", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            // Actualizamos los datos del jugador que se han modificado en la petición
            if(jugadorMod.getNombreusu() != null
                    && !jugadorMod.getNombreusu().isEmpty()){
                jugadorBuscado.get().setNombreusu(jugadorMod.getNombreusu());
            }
            if (jugadorMod.getAdmin() != jugadorBuscado.get().getAdmin()) {
                jugadorBuscado.get().setAdmin(jugadorMod.getAdmin());
            }
            if (jugadorMod.getAvatar() != null
                    && !jugadorMod.getAvatar().isEmpty()) {
                jugadorBuscado.get().setAvatar(jugadorMod.getAvatar());
            }
            if(jugadorMod.getClave() != null
                    && !jugadorMod.getClave().isEmpty()){
                jugadorBuscado.get().setClave(jugadorMod.getClave());
            }
            Equipo equipoJugador = null;
            // Si no se recibe id de equipo o el id es menor de 1, no se modifica el equipo
            if(jugadorMod.getEquipoId() != null
                    && jugadorMod.getEquipoId() > -1){
                // Si el id es 0, se elimina el equipo
                if(jugadorMod.getEquipoId() == 0) {
                    jugadorBuscado.get().setEquipo(null);
                } else {
                    equipoJugador = equipoServices.getEquipoById(jugadorMod.getEquipoId()).orElse(null);
                    jugadorBuscado.get().setEquipo(equipoJugador == null ? jugadorBuscado.get().getEquipo() : equipoJugador);
                }
            }
            JsonResponse<Jugador> response = new JsonResponse<>(HttpStatus.OK, "El jugador se ha actualizado correctamente", jugadorRepository.save(jugadorBuscado.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

}
