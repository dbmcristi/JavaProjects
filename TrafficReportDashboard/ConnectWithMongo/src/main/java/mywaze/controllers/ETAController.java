package mywaze.controllers;

import mywaze.dto.ETA;
import mywaze.locationsdto.RepresentationDto;
import mywaze.mapper.ETAMapper;
import mywaze.service.ETAService;
import mywaze.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eta")
public class ETAController {

    @Autowired
    private ETAService service;
    @Autowired
    private ETAMapper mapper;

    @PostMapping(value = "/")
    public ETA addETA(@RequestBody ETA eta) {
        mywaze.model.ETA model = mapper.toModel(eta);
        mywaze.model.ETA savedModel = service.save(model);
        return mapper.toDto(savedModel);
    }
}