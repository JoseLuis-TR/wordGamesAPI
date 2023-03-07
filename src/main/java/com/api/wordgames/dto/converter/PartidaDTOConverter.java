package com.api.wordgames.dto.converter;

import com.api.wordgames.dto.PartidaDTO;
import com.api.wordgames.model.Partida;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartidaDTOConverter {

    private final ModelMapper modelMapper;

    public PartidaDTO convertToDTO (Partida partida){
        return modelMapper.map(partida, PartidaDTO.class);
    }
}
