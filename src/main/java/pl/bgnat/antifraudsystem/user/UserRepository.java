package pl.bgnat.antifraudsystem.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findUserByUsername(String username);
	boolean existsUserByUsername(String username);
	boolean existsUserByEmail(String email);

	@Query("""
			SELECT 
				CASE
				WHEN count(u.id) > 0 THEN true
				ELSE false END
			FROM User u
			WHERE u.phone.number = ?1
				""")
	boolean existsUserByPhoneNumer(String number);
	void deleteUserByUsername(String username);
}
