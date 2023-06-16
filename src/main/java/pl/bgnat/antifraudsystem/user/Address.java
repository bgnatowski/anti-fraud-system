package pl.bgnat.antifraudsystem.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Address")
@Table(name = "address")
class Address {
	@Id
	@SequenceGenerator(name = "address_id_sequence", sequenceName = "address_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_sequence")
	private Long id;

	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_user_id_address_id")
	)
	private User user;

	@Column(name = "addres_line_1", nullable = false)
	private String addressLine1;
	@Column(name = "addres_line_2")
	private String addressLine2;
	@Column(name = "postal_code", nullable = false)
	private String postalCode;
	@Column(name = "city", nullable = false)
	private String city;
	@Column(name = "state", nullable = false)
	private String state;
	@Column(name = "country") //to enum
	private String country;
	@Column(name = "country_code") //to enum
	private String countryCode;
}