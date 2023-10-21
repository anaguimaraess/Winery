package br.com.ecommerce.winery.models.cliente;

import br.com.ecommerce.winery.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEndereco;
    @NotNull
    private String logradouro;
    @NotNull
    private int numero;
    @NotNull
    private String complemento;
    @NotNull
    private String bairro;
    @NotNull
    private String cidade;
    private String cep;
    @NotNull
    private String uf;
    @Enumerated(EnumType.STRING)
    private FlagEndereco flagEndereco;
    @NotNull
    private boolean principal;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;}
