package com.api.wordgames.repositories;

import com.api.wordgames.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByNombreEqualsIgnoreCase(String nombre);

    List<Equipo> findAllByOrderByPuntosDesc();
}
