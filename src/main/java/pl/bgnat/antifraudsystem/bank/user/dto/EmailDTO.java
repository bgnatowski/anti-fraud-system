package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;

@Builder
public record EmailDTO(String to, String subject, String content) {

}
