package br.com.ecommerce.winery.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String email;
    private Grupo grupo;
    private String status;

}
