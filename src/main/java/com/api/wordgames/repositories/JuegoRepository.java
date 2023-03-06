package com.api.wordgames.repositories;

import com.api.wordgames.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    List<Juego> findByNombreEqualsIgnoreCase(String nombre);
}
