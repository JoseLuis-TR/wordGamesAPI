package com.api.wordgames.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JugadorModDTO {

    private Boolean admin;
    private String nombreusu;
    private String clave;
    private String avatar;
    private Integer puntos;
    private Long equipoId;
}
