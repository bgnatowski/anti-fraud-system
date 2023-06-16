package pl.bgnat.antifraudsystem.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Account")
@Table(name = "account",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "account_card_number_constraint",
						columnNames = "card_number"
				),
				@UniqueConstraint(
						name = "account_iban_constraint",
						columnNames = "iban"
				)
		})
class Account {
	@Id
	@SequenceGenerator(name = "account_id_sequence", sequenceName = "account_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_sequence")
	private Long id;

	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_account_id")
	)
	private User owner;

	@Column(name = "card_number", nullable = false, length = 15, unique = true)
	private String cardNumber;

	@OneToMany(mappedBy = "account",
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
	)
	private final List<CreditCard> cards = new ArrayList<>();

	@Column(name = "iban", nullable = false, length = 34, unique = true)
	private String iban;
	@Column(name = "balance", nullable = false)
	private Double balance;
	@Column(name = "create_date", nullable = false)
	private LocalDateTime createDate;
	@Column(name = "is_active", nullable = false)
	private boolean isActive;
}
