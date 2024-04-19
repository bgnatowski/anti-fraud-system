package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.domain.request.CreditCardAccessRequest;
import pl.bgnat.antifraudsystem.domain.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.domain.response.CreditCardActionResponse;
import pl.bgnat.antifraudsystem.domain.response.HttpResponse;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/credit-card/")
class CreditCardController {
    private final CreditCardService creditCardService;

    @GetMapping("/{card-number}")
    ResponseEntity<HttpResponse> getCreditCard(@PathVariable("card-number") String cardNumber) {
        CreditCardDTO creditCardDTO = creditCardService.getCreditCardDTOByNumber(cardNumber);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardDTO", creditCardDTO))
                        .message("Credit card retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/list")
        //todo support-admin only
    ResponseEntity<HttpResponse> getAllCreditCards() {
        List<CreditCardDTO> creditCardsList = creditCardService.getAllCreditCards();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardsList", creditCardsList))
                        .message("Credit cards list retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/activate")
        //merchant
    ResponseEntity<HttpResponse> activeCreditCard(@Valid @RequestBody CreditCardAccessRequest creditCardActiveRequest) {
        CreditCardActionResponse creditCardActivationResponse = creditCardService.activeCreditCard(creditCardActiveRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardActivationResponse", creditCardActivationResponse))
                        .message(creditCardActivationResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/changePin")
        //merchant
    ResponseEntity<HttpResponse> changePin(@Valid @RequestBody CreditCardChangePinRequest creditCardActiveRequest) {
        CreditCardActionResponse creditCardChangePinResponse = creditCardService.changePin(creditCardActiveRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardActivationResponse", creditCardChangePinResponse))
                        .message(creditCardChangePinResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/restrict")
        //merchant
    ResponseEntity<HttpResponse> restrictCreditCard(@RequestBody @Valid CreditCardAccessRequest creditCardAccessRequest) {
        CreditCardActionResponse creditCardRestrictResponse = creditCardService.restrict(creditCardAccessRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardRestrictResponse", creditCardRestrictResponse))
                        .message(creditCardRestrictResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete")
        //merchant
    ResponseEntity<HttpResponse> deleteCreditCard(@Valid @RequestBody CreditCardAccessRequest creditCardDeleteRequest) {
        CreditCardActionResponse creditCardDeleteResponse = creditCardService.delete(creditCardDeleteRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("creditCardDeleteResponse", creditCardDeleteResponse))
                        .message(creditCardDeleteResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
