package exercise.kadai10th.controller;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.requestform.AirportRequestForm;
import exercise.kadai10th.response.AirportResponse;
import exercise.kadai10th.response.AllAirportResponse;
import exercise.kadai10th.service.AirportService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/airports/codes/{airportCode}")
    ResponseEntity<AirportResponse> getAirportByCode(
            @PathVariable("airportCode") @Size(min = 3, max = 3, message = "Number of letters has to be 3")
            String airportCode) {
        return ResponseEntity
                .ok(new AirportResponse("Successfully found", airportService.getAirportByCode(airportCode)));
    }

    @GetMapping("/airports/prefectures")
    ResponseEntity<AllAirportResponse> getAirportsByPrefName(@RequestParam(value = "prefName") String prefName) {
        return ResponseEntity
                .ok(new AllAirportResponse(
                        "All airports in " + prefName + " listed", airportService.getAirportsByPrefName(prefName)
                ));
    }

    @GetMapping("/airports")
    ResponseEntity<AllAirportResponse> getAllAirports() {
        return ResponseEntity
                .ok(new AllAirportResponse("All airports listed", airportService.getAllAirports()));
    }

    @PostMapping("/airports")
    ResponseEntity<AirportResponse> createAirport(@RequestBody @Validated AirportRequestForm airportRequestForm,
                                                  UriComponentsBuilder uriBuilder) {
        String airportCode = airportRequestForm.getAirportCode();
        AirportEntity airportEntity = airportService.createAirport(
                airportCode,
                airportRequestForm.getAirportName(),
                airportRequestForm.getPrefCode());
        URI uri = uriBuilder
                .path("/airports/" + airportCode)
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(new AirportResponse("Successfully created", airportEntity));
    }

    @PatchMapping("/airports/{airportCode}")
    ResponseEntity<Void> updateAirport(@RequestBody @Validated AirportRequestForm airportRequestForm) {
        airportService.updateAirport(
                airportRequestForm.getAirportCode(),
                airportRequestForm.getAirportName(),
                airportRequestForm.getPrefCode());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/airports/{airportCode}")
    ResponseEntity<Void> deleteAirport(
            @PathVariable("airportCode") @Size(min = 3, max = 3, message = "Number of letters has to be 3")
            String airportCode) {
        airportService.deleteAirport(airportCode);
        return ResponseEntity.noContent().build();
    }

}
