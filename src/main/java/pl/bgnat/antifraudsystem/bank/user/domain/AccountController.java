package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/account/")
class AccountController {
	private final AccountService accountService;

	// get all accounts - admin
	// get account for user by: username - merchant/support/admin
	// pay into account merchant/support/admin
	// transfer
}
