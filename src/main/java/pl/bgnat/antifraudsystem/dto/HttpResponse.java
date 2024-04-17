package pl.bgnat.antifraudsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record HttpResponse(String timeStamp,
						   int statusCode,
						   HttpStatus status,
						   String message,
						   Map<?, ?> data) {

}