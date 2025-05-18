package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository  extends JpaRepository<Client, Long> {
}
