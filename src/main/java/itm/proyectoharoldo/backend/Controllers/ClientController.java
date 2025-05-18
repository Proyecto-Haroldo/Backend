package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Clients")
public class ClientController {

    private final ClientRepository repository;

    public ClientController(ClientRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Client> GetAllClients(){
        return repository.findAll();
    }

}
