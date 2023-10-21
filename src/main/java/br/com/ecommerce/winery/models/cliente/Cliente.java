package br.com.ecommerce.winery.models.cliente;

import br.com.ecommerce.winery.models.backoffice.Grupo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCliente;

    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{11}", message = "CPF deve conter apenas dígitos")
    private String cpf;
    @NotEmpty
    @Size(min = 5, max = 50)
    @Column(name = "nome")
    private String nome;
    @Past
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Column(unique = true, nullable = false)
    private String email;
    private String genero;
    @Column(nullable = false)
    @Size(min = 8, message = "Senha deve conter pelo menos 8 caracteres")
    private String senha;

    @Column(nullable = false)
    private String confirmaSenha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
    @Enumerated(EnumType.STRING)
    private Grupo grupo;
}
