package pl.bgnat.antifraudsystem.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.user.enums.Country;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Account")
@Table(name = "account",
		uniqueConstraints = {
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
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_account_id")
	)
	private User owner;

	@OneToMany(cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	@JoinTable(
			name="account_credit_cards",
			joinColumns = @JoinColumn(
					name="account_id",
					referencedColumnName = "id",
					foreignKey = @ForeignKey(name = "fk_account_id_credit_card_id")
			),
			inverseJoinColumns = @JoinColumn(
					name="credit_card_id",
					referencedColumnName = "id",
					foreignKey = @ForeignKey(name = "fk_credit_card_id_account_id")
			)
	)
	private final Set<CreditCard> creditCards = new HashSet<>();

	@Column(name = "country", nullable = false)
	@Enumerated(EnumType.STRING)
	private Country country;

	@Column(name = "iban", nullable = false, length = 34)
	private String iban;
	@Column(name = "balance", nullable = false)
	private Double balance;
	@Column(name = "create_date", nullable = false)
	private LocalDateTime createDate;
	@Column(name = "is_active", nullable = false)
	private boolean isActive;


	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", iban='" + iban + '\'' +
				", balance=" + balance +
				", createDate=" + createDate +
				", isActive=" + isActive +
				'}';
	}

	private void removeCreditCards() {
		creditCards.clear();
	}
}
