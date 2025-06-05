package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @Column(name="clientid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(name="cedulaornit", nullable = false, unique = true)
    private String cedulaOrNIT;

    @Column(name="legalname", nullable = false)
    private String legalName;

    @Column(name="clienttype", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<AiClientAnalysis> aiAnalyses;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientAnswer> clientAnswers;

}
