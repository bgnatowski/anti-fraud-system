package pl.bgnat.antifraudsystem.domain.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import pl.bgnat.antifraudsystem.domain.enums.Role;

import java.util.Optional;

@Transactional
@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    boolean existsUserByRole(Role email);

//	@Query("""
//			SELECT
//				CASE
//				WHEN count(u.id) > 0 THEN true
//				ELSE false END
//			FROM User u
//			WHERE u.phone.number = ?1
//				""")
//	boolean existsUserByPhoneNumer(String number);

    boolean existsUserByPhone_Number(String phone_number);

    @Modifying
    void deleteUserByUsername(String username);
}
