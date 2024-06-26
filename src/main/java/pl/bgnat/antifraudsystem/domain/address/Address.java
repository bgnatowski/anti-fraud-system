package pl.bgnat.antifraudsystem.domain.address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.domain.enums.Country;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Address")
@Table(name = "address")
public class Address {
	@Id
	@SequenceGenerator(name = "address_id_sequence", sequenceName = "address_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_sequence")
	@Column(name = "id")
	private Long id;

	@OneToOne(
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_address_id")
	)
	private User user;

	@Column(name = "address_line_1", nullable = false)
	private String addressLine1;
	@Column(name = "address_line_2")
	private String addressLine2;
	@Column(name = "postal_code", nullable = false)
	private String postalCode;
	@Column(name = "city", nullable = false)
	private String city;
	@Column(name = "state", nullable = false)
	private String state;
	@Column(name = "country", nullable = false)
	@Enumerated(EnumType.STRING)
	private Country country;

	@Override
	public String toString() {
		return "Address{" +
				"id=" + id +
				", addressLine1='" + addressLine1 + '\'' +
				", addressLine2='" + addressLine2 + '\'' +
				", postalCode='" + postalCode + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country=" + country +
				'}';
	}


}
