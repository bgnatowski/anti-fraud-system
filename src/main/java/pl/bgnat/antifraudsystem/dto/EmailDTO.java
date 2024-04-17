package pl.bgnat.antifraudsystem.dto;

import lombok.Builder;

@Builder
public record EmailDTO(String to, String subject, String content) {

}
