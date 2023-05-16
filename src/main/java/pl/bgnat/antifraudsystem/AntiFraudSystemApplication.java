package pl.bgnat.antifraudsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntiFraudSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiFraudSystemApplication.class, args);
		System.out.println(calculateLuhnChecksum(400000112233556L));
	}


	public static int calculateLuhnChecksum(long num) {
		if (num < 0) {
			throw new IllegalArgumentException("Non-negative numbers only.");
		}
		final var numStr = String.valueOf(num);

		var sum = 0;
		var isOddPosition = true;
		// We loop on digits in numStr from right to left.
		for (var i = numStr.length() - 1; i >= 0; i--) {
			final var digit = Integer.parseInt(Character.toString(numStr.charAt(i)));
			final var substituteDigit = (isOddPosition ? 2 : 1) * digit;

			final var tensPlaceDigit = substituteDigit / 10;
			final var onesPlaceDigit = substituteDigit % 10;
			sum += tensPlaceDigit + onesPlaceDigit;

			isOddPosition = !isOddPosition;
		}
		final var checksumDigit = (10 - (sum % 10)) % 10;
		// Outermost modulus handles edge case `num = 0`.
		return checksumDigit;
	}

}
