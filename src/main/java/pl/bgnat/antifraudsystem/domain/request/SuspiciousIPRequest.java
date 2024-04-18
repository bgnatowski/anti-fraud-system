package pl.bgnat.antifraudsystem.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SuspiciousIPRequest(@NotNull
                                  @NotBlank
                                  @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")
                                  String ip) {
}
