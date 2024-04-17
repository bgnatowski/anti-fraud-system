package pl.bgnat.antifraudsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;

@Builder
public record UserRegistrationRequest(
        @NonNull
        @NotBlank
        String firstName,
        @NonNull
        @NotBlank
        String lastName,
        @NonNull
        @NotBlank
        @Email
        String email,
        @NonNull
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$")
        @Size(min = 4, max = 20)
        String username,
        @NonNull
        @NotBlank
        String password,
        @NonNull
        @NotBlank
        @Pattern(regexp = "^(\\+?(\\d{1,3})[ -]?)?(\\(?\\d{1,4}\\)?[ -]?)?(\\d{1,4}[ -]?)?(\\d{1,4})?$\n")
        String phoneNumber,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//        @JsonFormat(pattern = "MM/dd/yyyy")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth) {
}
