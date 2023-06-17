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
@Table(name = "phone_number")
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

	@Column(name = "number", nullable = false, unique = true)
	private String number;

	public String getFullNumber() {
		return "+48 " + number;
	}
}
