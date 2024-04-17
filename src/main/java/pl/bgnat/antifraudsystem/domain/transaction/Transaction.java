package pl.bgnat.antifraudsystem.domain.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.dto.TransactionRegion;
import pl.bgnat.antifraudsystem.dto.TransactionStatus;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "transaction")
@Entity(name = "Transaction")
public class Transaction {
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
	TransactionRegion region;
	@Column(name = "date")
	LocalDateTime date;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	TransactionStatus status;

	public Transaction(Long amount, String ipAddress, String cardNumber, TransactionRegion region, LocalDateTime date) {
		this.amount = amount;
		this.ipAddress = ipAddress;
		this.cardNumber = cardNumber;
		this.region = region;
		this.date = date;
	}

}
