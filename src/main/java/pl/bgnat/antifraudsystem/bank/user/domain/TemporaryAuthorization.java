package pl.bgnat.antifraudsystem.bank.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity(name = "TemporaryAuthorization")
@Table(name = "temporary_authorization")
class TemporaryAuthorization {
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
			foreignKey = @ForeignKey(name = "fk_user_id_temporary_authorization_id")
	)
	private User user;

	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@Column(name = "code", length = 5)
	private String code;

	@Override
	public String toString() {
		return "TemporaryAuthorization{" +
				"id=" + id +
				", expirationDate=" + expirationDate +
				", code='" + code + '\'' +
				'}';
	}
}
