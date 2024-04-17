package pl.bgnat.antifraudsystem.bank.user.domain;

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
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_phone_number_id")
	)
	private User user;

	@Column(name = "number", nullable = false)
	private String number;

	@Override
	public String toString() {
		return "PhoneNumber{" +
				"id=" + id +
				", number='" + number + '\'' +
				'}';
	}

	public static String[] extractAreaCodeAndNumber(String phoneNumber) {
		String areaCode = "";
		String number = "";
		if (phoneNumber.startsWith("+")) {
			int areaCodeEndIndex = phoneNumber.indexOf(" ", 1);
			if (areaCodeEndIndex != -1) {
				areaCode = phoneNumber.substring(0, areaCodeEndIndex);
				number = phoneNumber.substring(areaCodeEndIndex);
			}
		}
		// Remove any spaces from the phone number
		String sanitizedPhoneNumber = number.replaceAll("\\s", "");
		// Remove any non-digit characters
		String extractedDigits = sanitizedPhoneNumber.replaceAll("\\D", "");

		return new String[] { areaCode, extractedDigits };
	}
}
