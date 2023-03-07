package com.api.wordgames.services;

import com.api.wordgames.model.Dificultad;
import com.api.wordgames.model.Juego;
import com.api.wordgames.repositories.JuegoRepository;
import com.api.wordgames.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoServices {

    /**
     * Repositorio de Juegos
     * @see JuegoRepository
     */
    @Autowired
    private JuegoRepository juegoRepository;

    /**
     * Obtener todos los juegos
     *
     * @return lista de juegos
     */
    public List<Juego> getAllJuegos(){
        return juegoRepository.findAll();
    }

    /**
     * Obtener un juego en base a su ID
     *
     * @param id
     * @return Juego o error 404 si no encuentra el juego
     */
    public Optional<Juego> getJuegoById(Long id){
        return juegoRepository.findById(id);
    }

    /**
     * Elimina un juego en base a su ID
     *
     * @param juego
     */
    public void deleteJuego (Juego juego){
        juegoRepository.delete(juego);
    }

    /**
     * Crea un nuevo juego
     *
     * @param newJuego
     * @return ResponseEntity con el status y el body, 401 si el nombre del juego ya existe,
     *          201 si se crea correctamente
     */
    public ResponseEntity<JsonResponse<Juego>> saveJuego(Juego newJuego){
        List<Juego> juegoBuscado = juegoRepository.findByNombreEqualsIgnoreCase(newJuego.getNombre());
        if (!juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El juego ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        } else {
            JsonResponse<Juego> response = new JsonResponse<>(HttpStatus.CREATED, "El juego se ha creado correctamente", juegoRepository.save(newJuego));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    /**
     * Actualiza un juego
     *
     * @param juego
     * @return ResponseEntity con el status y el body, 404 si no encuentra el juego,
     *          400 si el nombre del juego ya existe, 200 si se actualiza correctamente
     */
    public ResponseEntity<JsonResponse<Juego>> updateJuego(Long id, Juego juego){
        Optional<Juego> juegoBuscado = juegoRepository.findById(id);
        List<Juego> juegoExiste = juegoRepository.findByNombreEqualsIgnoreCase(juego.getNombre());
        if (juegoBuscado.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El juego no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        } else if(!juegoExiste.isEmpty()){
            JsonResponse<Juego> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nombre del juego ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            if(juego.getNombre() != null
                    && !juego.getNombre().isEmpty()
                    && !juego.getNombre().equals(juegoBuscado.get().getNombre())){
                juegoBuscado.get().setNombre(juego.getNombre());
            }
            if(juego.getInstrucciones() != null
                    && !juego.getInstrucciones().isEmpty()
                    && !juego.getInstrucciones().equals(juegoBuscado.get().getInstrucciones())){
                juegoBuscado.get().setInstrucciones(juego.getInstrucciones());
            }
            if(juego.getIntentosmax() != null
                    && juego.getIntentosmax() > 0
                    && !juego.getIntentosmax().equals(juegoBuscado.get().getIntentosmax())){
                juegoBuscado.get().setIntentosmax(juego.getIntentosmax());
            }
            if(juego.getDificultad() != null){
                try{
                    Dificultad.valueOf(juego.getDificultad().toString());
                    if (!juego.getDificultad().equals(juegoBuscado.get().getDificultad())) {
                        juegoBuscado.get().setDificultad(juego.getDificultad());
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
