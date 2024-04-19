package pl.bgnat.antifraudsystem.domain.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = DateTimeUtils.ISO_DATE_TIME_PATTERN)
    LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    TransactionStatus status;

}
