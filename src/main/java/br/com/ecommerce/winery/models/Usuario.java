package br.com.ecommerce.winery.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Usuario")
@Builder
public class Usuario {
    @Id
    @GeneratedValue
    private int id;
    private String nome;

    @Pattern(regexp = "[0-9]{11}", message = "CPF deve conter apenas dígitos")
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;
    private String senha;
    private String confirmaSenha;

    @Enumerated(EnumType.STRING)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    private Status status;

}
