package com.api.wordgames.controller;

import com.api.wordgames.dto.JugadorDTO;
import com.api.wordgames.dto.JugadorModDTO;
import com.api.wordgames.dto.converter.JugadorDTOConverter;
import com.api.wordgames.model.Equipo;
import com.api.wordgames.repositories.EquipoRepository;
import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.model.Jugador;
import com.api.wordgames.repositories.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorRepository jugadorRepository;
    private final JugadorDTOConverter jugadorDTOConverter;
    private final EquipoRepository equipoRepository;

    /**
     * Obtenemos todos los jugadores
     *
     * @return lista de jugadores
     */
    @GetMapping("/jugadores")
    public ResponseEntity<List<?>> getAllJugadores(){
        List<Jugador> jugadores = jugadorRepository.findAll();
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
        Optional<Jugador> jugadorBuscado = jugadorRepository.findById(id);
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
        Optional<Jugador> jugadorBuscado = jugadorRepository.findById(id);
        if(jugadorBuscado.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else {
            jugadorRepository.deleteById(id);
            JsonResponse<Jugador> response = new JsonResponse<>(HttpStatus.NO_CONTENT, "El jugador se ha eliminado correctamente", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
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
     * Actualizamos un jugador
     *
     * @param newJugador
     * @param id
     * @return Error 404 si no encuentra el jugador, 200 si se actualiza correctamente
     */
    @PutMapping("/jugador/{id}")
    public ResponseEntity<JsonResponse<Jugador>> updateJugador(@RequestBody JugadorModDTO newJugador, @PathVariable Long id){
        Optional<Jugador> jugadorBuscado = jugadorRepository.findById(id);
        List<Jugador> jugadorExiste = jugadorRepository.findByNombreusuEqualsIgnoreCase(newJugador.getNombreusu());
        // Comprobamos que el jugador exista
        if (jugadorBuscado.isEmpty()){
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El jugador no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        // Comprobamos que el nick de usuario no este en uso por otro usuario
        } else if (!jugadorExiste.isEmpty() && !Objects.equals(jugadorBuscado.get().getNombreusu(), newJugador.getNombreusu())) {
            JsonResponse<Jugador> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nick de usuario ya esta en uso", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            // Actualizamos los datos del jugador que se han modificado en la petición
            if(newJugador.getNombreusu() != null
                    && !newJugador.getNombreusu().isEmpty()){
                jugadorBuscado.get().setNombreusu(newJugador.getNombreusu());
            }
            if (newJugador.getAdmin() != jugadorBuscado.get().getAdmin()) {
                jugadorBuscado.get().setAdmin(newJugador.getAdmin());
            }
            if (newJugador.getAvatar() != null
                    && !newJugador.getAvatar().isEmpty()) {
                jugadorBuscado.get().setAvatar(newJugador.getAvatar());
            }
            if(newJugador.getClave() != null
                    && !newJugador.getClave().isEmpty()){
                jugadorBuscado.get().setClave(newJugador.getClave());
            }
            Equipo equipoJugador = null;
            // Si no se recibe id de equipo o el id es menor de 1, no se modifica el equipo
            if(newJugador.getEquipoId() != null
                    && newJugador.getEquipoId() > -1){
                // Si el id es 0, se elimina el equipo
                if(newJugador.getEquipoId() == 0) {
                    jugadorBuscado.get().setEquipo(null);
                } else {
                    equipoJugador = equipoRepository.findById(newJugador.getEquipoId()).orElse(null);
                    jugadorBuscado.get().setEquipo(equipoJugador == null ? jugadorBuscado.get().getEquipo() : equipoJugador);
                }
            }
            JsonResponse<Jugador> response = new JsonResponse<>(HttpStatus.OK, "El jugador se ha actualizado correctamente", jugadorRepository.save(jugadorBuscado.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
