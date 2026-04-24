package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByLegalName(String legalName);

    @Query("SELECT u FROM User u WHERE u.cedulaOrNIT = :cedulaornit")
    Optional<User> findByCedulaOrNIT(@Param("cedulaornit") String cedulaornit);

    @Query("SELECT u FROM User u WHERE u.cedulaOrNIT = :cedulaornit")
    List<User> findAllByCedulaOrNIT(@Param("cedulaornit") String cedulaornit);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.specialities WHERE u.userId = :id")
    Optional<User> findUserWithSpecialities(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.specialities")
    List<User> findAllWithSpecialities();
}
