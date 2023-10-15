package br.com.ecommerce.winery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
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
    @NotEmpty
    @Size(min = 5, max= 50)
    private String nomeCliente;
    @Past
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    private String genero;
    @Column(nullable = false)
    @Size(min = 8, message = "Senha deve conter pelo menos 8 caracteres")
    private String senha;

    @Column(nullable = false)
    private String confirmaSenha;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente")
    private List<Endereco> endereco;



}
