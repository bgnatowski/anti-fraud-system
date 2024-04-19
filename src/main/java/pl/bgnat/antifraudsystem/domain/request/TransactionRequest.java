package pl.bgnat.antifraudsystem.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.time.LocalDateTime;

@Builder
public record TransactionRequest(
        @NotNull
        @Positive
        Long amount,
        @NotBlank
        @NotNull
        @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$", message = "Invalid ip format")
        String ip,
        @NotNull
        @NotBlank
        @Size(min = 16, max = 16, message = "Invalid card number length. Should be 16 digit")
        String number,
        @NotNull
        @NotBlank
        @Size(min = 2, max = 4, message = "Invalid region code")
        @Pattern(regexp = "^(EAP|ECA|HIC|LAC|MENA|SA|SSA)$",
                message = "We currently support only: EAP|ECA|HIC|LAC|MENA|SA|SSA regions")
        String region,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(pattern = DateTimeUtils.ISO_DATE_TIME_PATTERN)
        @NotNull
        LocalDateTime date
) {
}
