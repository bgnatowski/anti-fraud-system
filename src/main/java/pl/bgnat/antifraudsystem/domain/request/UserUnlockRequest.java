package pl.bgnat.antifraudsystem.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserUnlockRequest(
        @NonNull
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Invalid username characters (only letters and digits)")
        @Size(min = 4, max = 20, message = "Should be min 4 characters length and max 20")
        String username,
        @NotNull
        @NotBlank
        @Pattern(regexp = "^(LOCK|UNLOCK)$", message = "Invalid operation. There are only LOCK/UNLOCK options")
        String operation
) {
    public static final String UNLOCK = "UNLOCK";
    public static final String LOCK = "LOCK";
}
