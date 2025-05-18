package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<Client>> GetAllClients(){
        return ResponseEntity.ok(clientRepository.findAll());
    }

    @GetMapping("/{clientid}")
    public ResponseEntity<Client> GetClientById(@PathVariable Long clientid){
        Client client = clientRepository.findById(clientid).orElse(null);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }

}
