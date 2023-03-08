package com.api.wordgames.dto;

import com.api.wordgames.model.Dificultad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JuegoDTO {

    private Long id;
    private Dificultad dificultad;
    private String nombre;
    private String instrucciones;
    private Integer intentosmax;
}
