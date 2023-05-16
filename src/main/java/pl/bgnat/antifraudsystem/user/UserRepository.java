package pl.bgnat.antifraudsystem.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findUserByUsername(String username);
	boolean existsUserByUsername(String username);
	void deleteUserByUsername(String username);

}
