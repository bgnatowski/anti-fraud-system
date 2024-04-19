package pl.bgnat.antifraudsystem.domain.cards.stolencard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.domain.request.StolenCardRequest;
import pl.bgnat.antifraudsystem.domain.response.StolenCardDeleteResponse;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/stolencard")
@RequiredArgsConstructor
class StolenCardController {
    private final StolenCardService stolenCardService;

    @PostMapping
    ResponseEntity<StolenCard> addStolenCard(@RequestBody @Valid StolenCardRequest stolenCardRequest) {
        StolenCard addedStolenCard = stolenCardService.addStolenCard(stolenCardRequest);
        return ResponseEntity.ok(addedStolenCard);
    }

    @DeleteMapping("/{number}")
    ResponseEntity<StolenCardDeleteResponse> deleteStolenCard(@PathVariable("number") String number) {
        return ResponseEntity.ok(stolenCardService.deleteStolenCardByNumber(number));
    }

    @GetMapping
    ResponseEntity<List<StolenCard>> getAllStolenCards() {
        return ResponseEntity.ok(stolenCardService.getAllStolenCards());
    }
}
