package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    @Query("SELECT c FROM Client c WHERE c.cedulaOrNIT = :cedulaornit")
    Optional<Client> findByCedulaOrNIT(@Param("cedulaornit") String cedulaornit);

}
