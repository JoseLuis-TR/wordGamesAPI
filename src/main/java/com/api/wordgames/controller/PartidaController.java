package com.api.wordgames.controller;

import com.api.wordgames.repositories.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaRepository partidaRepository;
}
