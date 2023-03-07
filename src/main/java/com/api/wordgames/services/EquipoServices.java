package com.api.wordgames.services;

import com.api.wordgames.model.Equipo;
import com.api.wordgames.repositories.EquipoRepository;
import com.api.wordgames.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServices {

    @Autowired
    private EquipoRepository equipoRepository;

    public List<Equipo> getAllEquipos(){
        return equipoRepository.findAll();
    }

    public Optional<Equipo> getEquipoById(Long id){
        return equipoRepository.findById(id);
    }

    public void deleteEquipo(Equipo equipo){
        equipoRepository.delete(equipo);
    }

    public ResponseEntity<JsonResponse<Equipo>> saveEquipo(Equipo newEquipo){
        List<Equipo> equiposExiste = equipoRepository.findByNombreEqualsIgnoreCase(newEquipo.getNombre());
        if(!equiposExiste.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El equipo ya existe", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }
        // Nos aseguramos de que el equipo no tenga puntos ni logo al crearse
        newEquipo.setPuntos(0);
        newEquipo.setLogo("");
        JsonResponse<Equipo> responseOk = new JsonResponse<>(HttpStatus.CREATED, "Equipo Creado", equipoRepository.save(newEquipo));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOk);
    }

    /**
     * Actualizamos un equipo en base a su ID
     *
     * @param id
     * @param equipo
     * @return ResponseEntity con status y body, 404 si no encuentra el equipo, 200 si se actualiza correctamente
     */
    public ResponseEntity<JsonResponse<Equipo>> updateEquipo(Long id, Equipo equipo){
        Optional<Equipo> equipoBuscado = equipoRepository.findById(id);
        List<Equipo> equiposExiste = equipoRepository.findByNombreEqualsIgnoreCase(equipo.getNombre());
        // Comprobamos que el equipo existe
        if(equipoBuscado.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.NOT_FOUND, "El equipo no existe", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
            // Comprobamos que el nombre del equipo no este en uso
        } else if(!equiposExiste.isEmpty()){
            JsonResponse<Equipo> responseError = new JsonResponse<>(HttpStatus.BAD_REQUEST, "El nombre del equipo ya esta en uso", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
        }else {
            // Comprobamos que datos deben ser mantenidos y cuales modificados
            if (equipo.getNombre() != null
                    && !equipo.getNombre().isEmpty()
                    && !equipo.getNombre().equals(equipoBuscado.get().getNombre())){
                equipoBuscado.get().setNombre(equipo.getNombre());
            }
            if(equipo.getLogo() != null
                    && !equipo.getLogo().equals(equipoBuscado.get().getLogo())){
                equipoBuscado.get().setLogo(equipo.getLogo());
            }
            JsonResponse<Equipo> responseOk = new JsonResponse<>(HttpStatus.OK, "Equipo Modificado", equipoRepository.save(equipoBuscado.get()));
            return ResponseEntity.status(HttpStatus.OK).body(responseOk);
        }
    }
}
