package pl.bgnat.antifraudsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TemporaryAuthorizationDTO(Long id,
										String code,
										LocalDateTime expirationDate) {
}
