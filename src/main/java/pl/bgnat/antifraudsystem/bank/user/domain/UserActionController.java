package pl.bgnat.antifraudsystem.bank.user.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.bank.HttpResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.ConfirmEmailRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user")
class UserActionController {
    private final UserFacade userManager;

    @PutMapping("/role")
        // manualna zmiana roli przez admina
    ResponseEntity<HttpResponse> changeRole(@RequestBody UserUpdateRoleRequest updateRequest) {
        UserDTO updatedUser = userManager.changeRole(updateRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("updatedUser", updatedUser))
                        .message("User role updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PutMapping("/access")
        //manualne zablokowanie/odblokowanie konta przez admina/supporta
    ResponseEntity<HttpResponse> changeAccess(@RequestBody UserUnlockRequest updateRequest) {
        UserUnlockResponse updatedUser = userManager.changeLock(updateRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("updatedUser", updatedUser))
                        .message("User access updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/email/confirm") // all - kazdy moze potwierdzać email
    public ResponseEntity<HttpResponse> confirmEmail(@RequestBody ConfirmEmailRequest confirmEmailRequest) {
        UserEmailConfirmedResponse confirmedResponse = userManager.confirmUserEmail(confirmEmailRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("confirmedResponse", confirmedResponse))
                        .message(confirmedResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
