package pl.bgnat.antifraudsystem.domain.email;

import lombok.Builder;

@Builder
public record EmailDTO(String to, String subject, String content) {

}
