package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/credit-card/")
class AccountController {
	private final AccountService accountService;
	private final Clock clock;

	// get all accounts - admin
	// get account for user by: username - merchant/support/admin
	// pay into account merchant/support/admin
	// withdraw(in window) //support
	// transfer


}
