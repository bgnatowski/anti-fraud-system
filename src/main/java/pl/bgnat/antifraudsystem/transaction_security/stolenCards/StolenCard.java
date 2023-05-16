package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "StolenCard")
@Table(name = "stolen_card")
class StolenCard {
	@Id
	@SequenceGenerator(name = "stolen_card_id_sequence", sequenceName = "stolen_card_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stolen_card_id_sequence")
	Long id;
	@Column(name = "number", nullable = false)
	String number;
}
