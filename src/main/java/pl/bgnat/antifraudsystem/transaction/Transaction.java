package pl.bgnat.antifraudsystem.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.transaction.dto.Region;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionStatus;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "transaction")
@Entity(name = "Transaction")
class Transaction {
	@Id
	@SequenceGenerator(name = "transaction_id_sequence", sequenceName = "transaction_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_sequence")
	Long id;
	@Column(name = "amount", nullable = false)
	Long amount;
	@Column(name = "ip", nullable = false)
	String ipAddress;
	@Column(name = "number", nullable = false)
	String cardNumber;
	@Column(name = "region", nullable = false)
	@Enumerated(EnumType.STRING)
	Region region;
	@Column(name = "date")
	LocalDateTime date;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	TransactionStatus status;

	public Transaction(Long amount, String ipAddress, String cardNumber, Region region, LocalDateTime date) {
		this.amount = amount;
		this.ipAddress = ipAddress;
		this.cardNumber = cardNumber;
		this.region = region;
		this.date = date;
	}

}
