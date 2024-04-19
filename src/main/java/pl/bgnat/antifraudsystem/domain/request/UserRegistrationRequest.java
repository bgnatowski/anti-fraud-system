package pl.bgnat.antifraudsystem.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

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
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Invalid username characters (only letters and digits)")
        @Size(min = 4, max = 20, message = "Should be min 4 characters length and max 20")
        String username,
        @NonNull
        @NotBlank
        String password,
        @NonNull
        @NotBlank
        @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "Invalid phone number format")
        String phoneNumber,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonFormat(pattern = "dd/MM/yyyy")
//        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        LocalDate dateOfBirth) {
}
