package pl.bgnat.antifraudsystem.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "PhoneNumber")
@Table(name = "phone_number",
	uniqueConstraints = {
			@UniqueConstraint(
					name = "phone_number_constraint",
					columnNames = "number"
			)
	})
class PhoneNumber {
	@Id
	@SequenceGenerator(name = "phone_number_id_sequence", sequenceName = "phone_number_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_number_id_sequence")
	private Long id;

	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_phone_number_id")
	)
	private User user;

	@Column(name = "areaCode", nullable = false)
	private String areaCode;

	@Column(name = "number", nullable = false, unique = true)
	private String number;

	@Override
	public String toString() {
		return "PhoneNumber{" +
				"id=" + id +
				", areaCode='" + areaCode + '\'' +
				", number='" + number + '\'' +
				'}';
	}

	public static String[] extractAreaCodeAndNumber(String phoneNumber) {
		// Remove any spaces from the phone number
		String sanitizedPhoneNumber = phoneNumber.replaceAll("\\s", "");

		// Extract the area code
		String areaCode = "";
		if (sanitizedPhoneNumber.startsWith("+")) {
			int areaCodeEndIndex = sanitizedPhoneNumber.indexOf(" ", 1);
			if (areaCodeEndIndex != -1) {
				areaCode = sanitizedPhoneNumber.substring(1, areaCodeEndIndex);
			}
		}

		// Remove any non-digit characters
		String extractedDigits = sanitizedPhoneNumber.replaceAll("\\D", "");

		return new String[] { areaCode, extractedDigits };
	}
}
