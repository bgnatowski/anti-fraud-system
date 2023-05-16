package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.response.SuspiciousIpDeleteResponse;

import java.util.List;

@RestController
@RequestMapping
class SuspiciousIPController {
	private final SuspiciousIPService ipService;

	public SuspiciousIPController(SuspiciousIPService ipService) {
		this.ipService = ipService;
	}
	@PostMapping( "/api/antifraud/suspicious-ip")
	ResponseEntity<SuspiciousIP> addSuspiciousIp(@RequestBody SuspiciousIPRequest suspiciousIPRequest){
		SuspiciousIP addedSuspiciousIp = ipService.addSuspiciousIp(suspiciousIPRequest);
		return new ResponseEntity(addedSuspiciousIp, HttpStatus.CREATED);
	}

	@DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
	ResponseEntity<SuspiciousIpDeleteResponse> deleteSuspiciousIpByIpAddress(@PathVariable("ip") String ipAddress){
		return ResponseEntity.ok(ipService.deleteSuspiciousIpByIpAddress(ipAddress));
	}

	@GetMapping("/api/antifraud/suspicious-ip")
	ResponseEntity<List<SuspiciousIP>> getAllSuspiciousIPs(){
		return ResponseEntity.ok(ipService.getAllSuspiciousIPs());
	}
}
