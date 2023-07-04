package pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "SuspiciousIP")
@Table(name = "suspicious_ip")
class SuspiciousIP {
	@Id
	@SequenceGenerator(name = "suspicious_ip_id_sequence", sequenceName = "suspicious_ip_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspicious_ip_id_sequence")
	Long id;
	@Column(name = "ip", nullable = false)
	String ip;


}



