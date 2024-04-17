package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
