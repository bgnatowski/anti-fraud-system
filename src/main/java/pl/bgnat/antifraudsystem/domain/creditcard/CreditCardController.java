package pl.bgnat.antifraudsystem.domain.creditcard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.dto.CreditCardDTO;
import pl.bgnat.antifraudsystem.dto.HttpResponse;
import pl.bgnat.antifraudsystem.dto.request.CreditCardActivationRequest;
import pl.bgnat.antifraudsystem.dto.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.dto.request.CreditCardDeleteRequest;
import pl.bgnat.antifraudsystem.dto.request.CreditCardRestrictRequest;
import pl.bgnat.antifraudsystem.dto.response.CreditCardActivationResponse;
import pl.bgnat.antifraudsystem.dto.response.CreditCardChangePinResponse;
import pl.bgnat.antifraudsystem.dto.response.CreditCardDeleteResponse;
import pl.bgnat.antifraudsystem.dto.response.CreditCardRestrictResponse;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/credit-card/")
class CreditCardController {
    private final CreditCardService creditCardService;

    @GetMapping("/{card-number}")
        //support-admin only
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
        //support-admin only
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
    ResponseEntity<HttpResponse> activeCreditCard(@RequestBody CreditCardActivationRequest creditCardActiveRequest) {
        CreditCardActivationResponse creditCardActivationResponse = creditCardService.activeCreditCard(creditCardActiveRequest);
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
    ResponseEntity<HttpResponse> changePin(@RequestBody CreditCardChangePinRequest creditCardActiveRequest) {
        CreditCardChangePinResponse creditCardChangePinResponse = creditCardService.changePin(creditCardActiveRequest);
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
    ResponseEntity<HttpResponse> restrictCreditCard(@RequestBody CreditCardRestrictRequest creditCardRestrictRequest) {
        CreditCardRestrictResponse creditCardRestrictResponse = creditCardService.restrict(creditCardRestrictRequest);
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
    ResponseEntity<HttpResponse> deleteCreditCard(@RequestBody CreditCardDeleteRequest creditCardDeleteRequest) {
        CreditCardDeleteResponse creditCardDeleteResponse = creditCardService.delete(creditCardDeleteRequest);
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
