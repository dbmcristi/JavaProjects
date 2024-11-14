package mywaze.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import mywaze.locationsdto.AlertLocation;
import mywaze.locationsdto.Locations;
import mywaze.locationsdto.RepresentationDto;
import mywaze.mapper.ETAMapper;
import mywaze.mapper.WazeMapper;
import mywaze.model.ETA;
import mywaze.model.WazeRoot;
import mywaze.service.ETAService;
import mywaze.service.ItemService;
import mywaze.service.WazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class GeoMapController {
    @Autowired
    WazeMapper wazeMapper;
    @Autowired
    private ItemService service;
    @Autowired
    private ETAService etaService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ETAMapper etaMapper;
    @Autowired
    private WazeService wazeService;
    @Value("${wazepath}")
    String url;

    @ModelAttribute("etaDto")
    public mywaze.dto.ETA newEta() {
        return new mywaze.dto.ETA();
    }

    @RequestMapping(value = "/mvc/geoMap")
    public String geoMap(Model model) {
        List<ETA> etaList = etaService.findAll();
        WazeRoot locations = service.findLast();
        Locations results = wazeMapper.toLocationDto(locations);
        List<RepresentationDto> res = new ArrayList<>();
        res.addAll(transform(results, etaList));
        model.addAttribute("representations", res);//incarcarea atributului pe Model(ce vreau sa transmit catre ui)
        return "geoMap";//redirecteaza la templates/geoMap.html
    }

    @RequestMapping(value = "/mvc/filter")
    public String filter(Model model, @RequestParam String startDate, @RequestParam String endDate) {
        List<ETA> etaList = etaService.findAll();

        List<WazeRoot> locations = service.findByDate(startDate, endDate);

        var res = locations.stream()
                .map(elem -> wazeMapper.toLocationDto(elem))
                .map(location -> transform(location, etaList))
                .flatMap(e -> e.stream().distinct())
                .collect(Collectors.toList());

        model.addAttribute("representations", res);//incarcarea atributului pe Model(ce vreau sa transmit catre ui)
        return "geoMap";//redirecteaza la templates/geoMap.html
    }

    @GetMapping(value = "/mvc/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public String importFromWaze(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        wazeService.saveWazeFile(result);
        return geoMap(model);
    }

    @RequestMapping(value = "/mvc/eta", method = RequestMethod.POST)
    public String addEta(@ModelAttribute("etaDto") mywaze.dto.ETA etaDto, Model model) {
        if (etaDto.getDate().isEmpty()) {
            etaDto.setDate(null);
        }
        mywaze.model.ETA modelEto = etaMapper.toModel(etaDto);
        etaService.save(modelEto);
        return geoMap(model);
    }

    private List<RepresentationDto> transform(Locations e, List<ETA> etaList) {
        /*
         * adds alerts and jams to representationDTOs
         * */
        String SEP = " ";
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        List<RepresentationDto> representationDtos = new ArrayList<>();

        if (e.getAlertLocations() != null) {
            representationDtos.addAll(
                    e.getAlertLocations().stream().map(f -> {
                        RepresentationDto representationDto = new RepresentationDto();
                        representationDto.setLatitude(f.getLocation().y);
                        representationDto.setLongitude(f.getLocation().x);
                        Comparator<ETA> comp = new Comparator<ETA>() {
                            @Override
                            public int compare(ETA eta1, ETA eta2) {
                                var dif = eta2.getTime() - eta1.getTime();
                                return (int) dif;
                            }
                        };
                        var firstLocation = etaList.stream()
                                .filter(eta -> filter(eta, f))
                                .sorted(comp)
                                .findFirst();
                        if (firstLocation.isPresent()) {
                            if (firstLocation.get().getDate() != null) {
                                representationDto.setEta(sm.format(firstLocation.get().getDate()));
                            }
                        }
                        representationDto.setMessage(f.getCity() + SEP + f.getStreet() + SEP + f.getType() + SEP + f.getSubtype());
                        return representationDto;
                    }).toList());
        }
        if (e.getJamLocations() != null) {
            representationDtos.addAll(
                    e.getJamLocations().stream().flatMap(f1 -> {
                        return f1.getLine().stream().map(f2 -> {
                            RepresentationDto representationDto = new RepresentationDto();
                            representationDto.setLatitude(f2.getY());
                            representationDto.setLongitude(f2.getX());
                            representationDto.setMessage(f1.getCity() + SEP + f1.getStreet());
                            return representationDto;
                        });

                    }).toList());
        }
        return representationDtos;
    }

    private boolean filter(ETA eta, AlertLocation f) {
        if (eta.getLocation().getX() == f.getLocation().x
                && eta.getLocation().getY() == f.getLocation().y
                && eta.getType().equals(f.getType())
                && eta.getSubtype().equals(f.getSubtype())) {
            return true;
        }
        return false;
    }

}
