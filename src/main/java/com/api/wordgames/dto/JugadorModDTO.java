package com.api.wordgames.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase usada para modificar un jugador o crear uno nuevo y evitar problemas a la hora de añadir un equipo
 */
@Getter @Setter
public class JugadorModDTO {

    private Boolean admin;
    private String nombreusu;
    private String clave;
    private String avatar;
    private Integer puntos;
    private Long equipoId;
}
