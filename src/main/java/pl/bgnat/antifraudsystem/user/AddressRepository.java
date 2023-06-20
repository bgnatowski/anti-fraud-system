package pl.bgnat.antifraudsystem.user;

import org.springframework.data.jpa.repository.JpaRepository;
interface AddressRepository extends JpaRepository<Address, Long> {
}
