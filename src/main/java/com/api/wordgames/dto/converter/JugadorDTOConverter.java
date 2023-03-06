package com.api.wordgames.dto.converter;

import com.api.wordgames.dto.JugadorDTO;
import com.api.wordgames.model.Jugador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

@Component
@RequiredArgsConstructor
public class JugadorDTOConverter {

    private final ModelMapper modelMapper;

    public JugadorDTO convertToDTO(Jugador jugador){
        return modelMapper.map(jugador,JugadorDTO.class);
    }
}
