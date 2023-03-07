package com.api.wordgames.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PartidaDTO {

    private Long id;

    private String jugadorId;

    private String juegoId;

    private Integer intentos;

    private Integer puntos;

    private String palabra;

    private LocalDateTime datetime;
}
