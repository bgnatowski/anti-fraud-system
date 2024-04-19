package pl.bgnat.antifraudsystem.domain.susip;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.domain.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.domain.response.SuspiciousIpDeleteResponse;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
@RequiredArgsConstructor
class SuspiciousIPController {
	private final SuspiciousIPService ipService;

	@PostMapping
	ResponseEntity<SuspiciousIP> addSuspiciousIp(@RequestBody @Valid SuspiciousIPRequest suspiciousIPRequest){
		SuspiciousIP addedSuspiciousIp = ipService.addSuspiciousIp(suspiciousIPRequest);
		return ResponseEntity.ok(addedSuspiciousIp);
	}

	@DeleteMapping("/{ip}")
	ResponseEntity<SuspiciousIpDeleteResponse> deleteSuspiciousIpByIpAddress(@PathVariable("ip") String ipAddress){
		return ResponseEntity.ok(ipService.deleteSuspiciousIpByIpAddress(ipAddress));
	}

	@GetMapping
	ResponseEntity<List<SuspiciousIP>> getAllSuspiciousIPs(){
		return ResponseEntity.ok(ipService.getAllSuspiciousIPs());
	}
}
