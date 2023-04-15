package exercise.kadai10th.controller;

import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.requestform.PrefectureRequestForm;
import exercise.kadai10th.response.AllPrefectureResponse;
import exercise.kadai10th.response.PrefectureResponse;
import exercise.kadai10th.service.PrefectureService;
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
public class PrefectureController {

    private final PrefectureService prefectureService;

    @GetMapping("/prefectures/{prefCode}")
    ResponseEntity<PrefectureResponse> getPrefByCode(
            @PathVariable("prefCode") @Size(min = 2, max = 2, message = "Number of letters has to be 2")
            String prefCode) {
        return ResponseEntity.ok(new PrefectureResponse("Successfully found", prefectureService.getPrefByCode(prefCode)));
    }

    @GetMapping("/prefectures")
    ResponseEntity<PrefectureResponse> getPrefByName(@RequestParam(value = "prefName") String prefName) {
        return ResponseEntity.ok(new PrefectureResponse("Successfully found", prefectureService.getPrefByName(prefName)));
    }

    @GetMapping("/prefectures")
    ResponseEntity<AllPrefectureResponse> getAllPrefs() {
        return ResponseEntity.ok(new AllPrefectureResponse("All prefectures listed", prefectureService.getAllPrefs()));
    }

    @PostMapping("/prefectures")
    ResponseEntity<PrefectureResponse> createPref(
            @RequestBody @Validated PrefectureRequestForm prefectureRequestForm, UriComponentsBuilder uriBuilder) {
        String prefCode = prefectureRequestForm.getPrefCode();
        PrefectureEntity prefectureEntity = prefectureService.createPref(prefCode, prefectureRequestForm.getPrefName());
        URI uri = uriBuilder
                .path("/" + prefCode)
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(new PrefectureResponse("Successfully created", prefectureEntity));
    }

    @PatchMapping("/prefectures/{prefCode}")
    ResponseEntity<Void> updatePref(
            @PathVariable("prefCode") @RequestBody @Validated PrefectureRequestForm prefectureRequestForm) {
        prefectureService.updatePref(prefectureRequestForm.getPrefCode(), prefectureRequestForm.getPrefName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/prefectures/{prefCode}")
    ResponseEntity<Void> deletePref(
            @PathVariable("prefCode") @Size(min = 2, max = 2, message = "Number of letters has to be 2")
            String prefCode) {
        prefectureService.deletePref(prefCode);
        return ResponseEntity.noContent().build();
    }

}
