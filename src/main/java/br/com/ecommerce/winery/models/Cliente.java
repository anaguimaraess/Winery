package br.com.ecommerce.winery.models;

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
    @Size(min = 5, max= 50)
    @Column(name = "nome")
    private String nome;
    @Past
    @NotNull
    @JsonFormat
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

    @NotNull(message = "Inserir todos os dados do endereço!")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente")
    private List<Endereco> endereco;




}
