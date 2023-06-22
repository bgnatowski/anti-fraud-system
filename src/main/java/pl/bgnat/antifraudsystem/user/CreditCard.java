package pl.bgnat.antifraudsystem.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.user.enums.Country;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "CreditCard")
@Table(name = "credit_card", uniqueConstraints = {
		@UniqueConstraint(
				name = "credit_card_card_number_constraint",
				columnNames = "card_number"
		),
})
class CreditCard {
	@Id
	@SequenceGenerator(name = "credit_card_id_sequence",sequenceName = "credit_card_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_card_id_sequence")
	@Column(name = "id", updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "account_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_account_credit_card")
	)
	private Account account;

	@ManyToOne
	@JoinColumn(
			name = "owner_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_credit_card")
	)
	private User owner;

	@Column(name = "card_number", nullable = false, columnDefinition = "TEXT", length = 16, unique = true)
	private String cardNumber;

	@Column(name = "cvv", nullable = false, columnDefinition = "TEXT", length = 3)
	private String cvv;

	@Column(name ="pin", nullable = false, columnDefinition = "TEXT", length = 4)
	private String pin;

	@Column(name = "expiration_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private LocalDate expirationDate;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private LocalDateTime createdAt;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@Column(name = "is_blocked", nullable = false)
	private boolean isBlocked;

	@Column(name = "country", nullable = false)
	@Enumerated(EnumType.STRING)
	private Country country;


	@Override
	public String toString() {
		return "CreditCard{" +
				"id=" + id +
				", cardNumber='" + cardNumber + '\'' +
				", pin='" + pin + '\'' +
				", createdAt=" + createdAt +
				", isActive=" + isActive +
				'}';
	}
}
