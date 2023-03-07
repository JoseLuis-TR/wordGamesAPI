package com.api.wordgames.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "intentos", nullable = false)
    private Integer intentos;

    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @Column(name = "palabra", length = 50, nullable = false)
    private String palabra;

    @Column(name = "Datetime" , nullable = false)
    private LocalDateTime datetime;

    @JsonBackReference
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="id_jugador")
    private Jugador jugador;

    @JsonBackReference
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="id_juego")
    private Juego juego;
}
