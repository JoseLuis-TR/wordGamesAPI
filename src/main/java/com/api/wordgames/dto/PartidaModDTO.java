package com.api.wordgames.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaModDTO {

    private Long jugadorId;

    private Long juegoId;

    private Integer intentos;
}
