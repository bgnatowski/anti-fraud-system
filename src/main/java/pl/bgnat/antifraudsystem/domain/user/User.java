package pl.bgnat.antifraudsystem.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.address.Address;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.phone.PhoneNumber;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Table(
        name = "_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_username_constraint",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "user_email_constraint",
                        columnNames = "email"
                )
        }
)
@Entity(name = "User")
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
    private String firstName;
    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    private String lastName;
    @Column(name = "username", nullable = false, columnDefinition = "TEXT")
    private String username;
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;
    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    private TemporaryAuthorization temporaryAuthorization;

    @OneToOne(mappedBy = "owner",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_account_id_user_id")
    )
    private Account account;

    @OneToOne(mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_address_id_user_id")
    )
    private Address address;

    @OneToOne(mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    @JoinColumn(
            name = "phone_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_phone_id_user_id")
    )
    private PhoneNumber phone;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    @Column(name = "has_account")
    private boolean hasAccount;
    @Column(name = "has_any_credit_card")
    private boolean hasAnyCreditCard;
    @Column(name = "number_of_credit_cards")
    private int numberOfCreditCards;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    Long getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }


    void lockAccount() {
        if (accountNonLocked) accountNonLocked = false;
    }

    void unlockAccount() {
        if (!accountNonLocked) accountNonLocked = true;
    }

    void increaseCreditCardNumber() {
        if (!hasAnyCreditCard) numberOfCreditCards = 1;
        else numberOfCreditCards++;
    }

    void decreaseCreditCardNumber() {
        if (numberOfCreditCards == 0)
            hasAnyCreditCard = false;
        else numberOfCreditCards--;
    }
}
