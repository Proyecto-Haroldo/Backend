package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.ClientDTO;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientRepository.findAll().stream()
                .map(client -> new ClientDTO(
                        client.getClientId(),
                        client.getCedulaOrNIT(),
                        client.getLegalName(),
                        client.getClientType(),
                        client.getEmail(),
                        client.getSector(),
                        client.getRole() != null ? client.getRole().getName() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> new ClientDTO(
                        client.getClientId(),
                        client.getCedulaOrNIT(),
                        client.getLegalName(),
                        client.getClientType(),
                        client.getEmail(),
                        client.getSector(),
                        client.getRole() != null ? client.getRole().getName() : null
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
