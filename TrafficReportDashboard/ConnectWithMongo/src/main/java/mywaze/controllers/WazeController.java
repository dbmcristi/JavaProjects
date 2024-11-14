package mywaze.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import mywaze.dto.Alert;
import mywaze.dto.Root;
import mywaze.locationsdto.AlertLocation;
import mywaze.locationsdto.Locations;
import mywaze.locationsdto.RepresentationDto;
import mywaze.mapper.WazeMapper;

import mywaze.model.ETA;
import mywaze.model.Location;
import mywaze.model.WazeRoot;
import mywaze.service.ETAService;
import mywaze.service.ItemService;
import mywaze.service.WazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/waze")
public class WazeController {
    @Autowired
    WazeMapper wazeMapper;

    @Autowired
    ObjectMapper objectMapper;
    //https://www.waze.com/row-partnerhub-api/partners/11315398994/waze-feeds/d15f3f75-64f0-4455-8bdd-a9b1fc1d1d44?format=1 mai mic
//    final String url = "https://www.waze.com/row-partnerhub-api/partners/11315398994/waze-feeds/d15f3f75-64f0-4455-8bdd-a9b1fc1d1d44?format=1";

    @Value("${wazepath}")
    String url;
    @Autowired
    private ItemService service;
    @Autowired
    private ETAService etaService;
    @Autowired
    private WazeService wazeService;

    @GetMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public String importFile() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        wazeService.saveWazeFile(result);
        return result;
    }

    @GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public Root getFile() {
//        System.out.println(result);
        Root result = null;
        try {
            result = objectMapper.readValue(new File("src/main/resources/waze.json"), Root.class);
            System.out.println(result);
            var wazeroot = wazeMapper.toModel(result);
            service.save(wazeroot);
            service.findAll();

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
        //  List<String> jsonLines = null; // TO DO
        //  return service.importTo(collection, jsonLines);
    }

    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepresentationDto> getLocations(@RequestParam String begin, @RequestParam String end) throws ParseException {
        String pattern = "dd-MM-yyyy";

        //  String patternMongo = "yyyy-MM-dd'T'HH:mm:ssZ";
//2024-07-31 16:32:00:000
        // SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        // try {
        //    Date dateBegin = dateFormat.parse(begin);
        //    Date dateEnd = dateFormat.parse(end);
        List<ETA> etaList = etaService.findAll();
        List<WazeRoot> locations = service.findByDate(begin, end);
        System.out.println(locations);
        List<Locations> results = wazeMapper.toLocationListDto(locations);
        List<RepresentationDto> res = new ArrayList<>();

        for (Locations e : results) {
            res.addAll(transform(e, etaList));//facem mergeul
        }
        return res;
        //    } catch (ParseException e) {
        //        e.printStackTrace();
        //        throw e;
        //    }
    }

    /***
     * afisam locatiile din ultimul json primit in db
     * @return List<RepresentationDto>
     */
    @GetMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepresentationDto> viewLocations() {
        List<ETA> etaList = etaService.findAll();
        WazeRoot locations = service.findLast();
        Locations results = wazeMapper.toLocationDto(locations);
        List<RepresentationDto> res = new ArrayList<>();

        res.addAll(transform(results, etaList));//facem mergeul
        return res;
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "cristi";
    }

    /**
     * face mergeul
     *
     * @param e
     * @param etaList
     * @return
     */
    private List<RepresentationDto> transform(Locations e, List<ETA> etaList) {
        String SEP = ", ";
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        List<RepresentationDto> representationDtos = new ArrayList<>();

        if (e.getAlertLocations() != null) {
            representationDtos.addAll(
                    e.getAlertLocations().stream().map(f -> {
                        RepresentationDto representationDto = new RepresentationDto();
                        representationDto.setLatitude(f.getLocation().y);
                        representationDto.setLongitude(f.getLocation().x);
                        var firstLocation = etaList.stream().filter(eta -> filter(eta, f)).findFirst();
                        if (firstLocation.isPresent()) {
                            representationDto.setEta(sm.format(firstLocation.get().getDate()));
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
