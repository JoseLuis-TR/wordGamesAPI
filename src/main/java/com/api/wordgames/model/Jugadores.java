package com.api.wordgames.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "jugador")
public class Jugadores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin", columnDefinition = "TINYINT(1)")
    private boolean admin;

    @Column(name = "nombreusu", length = 50)
    private String nombreusu;

    @Column(name = "clave", length = 255)
    private String clave;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "puntos")
    private int puntos;

    @ManyToOne
    @JoinColumn(name="id_equipo")
    private Equipos equipo;
}
