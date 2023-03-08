package com.api.wordgames.dto.converter;

import com.api.wordgames.dto.JuegoDTO;
import com.api.wordgames.model.Juego;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JuegoDTOConverter {

    private final ModelMapper modelMapper;

    public JuegoDTO convertToDTO(Juego juego){
        return modelMapper.map(juego, JuegoDTO.class);
    }
}
