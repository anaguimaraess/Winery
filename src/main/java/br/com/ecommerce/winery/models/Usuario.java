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
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{11}", message = "CPF deve conter apenas dígitos")
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private String confirmaSenha;

    @Enumerated(EnumType.STRING)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    private Status status;

}
