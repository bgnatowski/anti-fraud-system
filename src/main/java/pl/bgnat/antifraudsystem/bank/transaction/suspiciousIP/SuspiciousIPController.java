package pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP.dto.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP.dto.SuspiciousIpDeleteResponse;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
class SuspiciousIPController {
	private final SuspiciousIPService ipService;

	public SuspiciousIPController(SuspiciousIPService ipService) {
		this.ipService = ipService;
	}
	@PostMapping
	ResponseEntity<SuspiciousIP> addSuspiciousIp(@RequestBody SuspiciousIPRequest suspiciousIPRequest){
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
