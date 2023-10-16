package br.com.ecommerce.winery.models.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ViaCEP {
    @Id
    private int id;
    @JsonProperty("cep")
    private String cep;
    @JsonProperty("logradouro")
    private String logradouro;
    @JsonProperty("complemento")
    private String complemento;
    private String bairro;
    @JsonProperty("localidade")
    private String cidade;
    private String uf;
    @JsonProperty("ibge")
    private String codigoIBGE;
    @JsonProperty("gia")
    private String gia;
    @JsonProperty("ddd")
    private String ddd;
    @JsonProperty("siafi")
    private String codigoSIAFI;
}
