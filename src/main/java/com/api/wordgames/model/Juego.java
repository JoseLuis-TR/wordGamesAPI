package com.api.wordgames.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "juego")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 75, nullable = false, unique = true)
    private String nombre;

    @Column(name = "instrucciones", length = 500, nullable = false)
    private String instrucciones;

    @Column(name = "intentosmax", nullable = false)
    private Integer intentosmax;

    @Enumerated(EnumType.STRING)
    @Column(name="dificultad", nullable = false)
    private Dificultad dificultad;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Partida> partidas;
}
