package com.api.wordgames.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase necesaria para el convertidor de Jugador a JugadorDTO
 */
@Getter @Setter
public class JugadorDTO {

    private Long id;
    private Boolean admin;
    private String nombreusu;
    private String clave;
    private String avatar;
    private Integer puntos;
    private Long equipoId;
}
