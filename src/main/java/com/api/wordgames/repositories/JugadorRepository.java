package com.api.wordgames.repositories;

import com.api.wordgames.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    List<Jugador> findByNombreusuEqualsIgnoreCase(String nombreusu);
}
