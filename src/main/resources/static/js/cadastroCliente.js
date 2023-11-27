$(document).ready(function () {

    var cliente = {
        nome: '',
        genero: '',
        dataNascimento: '',
        cpf: '',
        email: '',
        senha: '',
        confirmaSenha: '',
        enderecos: []
    };
    
    $('#adicionarEnderecoButton').on('click', function () {
        var endereco2 = {
            logradouro: $('#inputLogradouro').val(),
            numero: $('#inputNumero').val(),
            complemento: $('#inputComplemento').val(),
            bairro: $('#inputBairro').val(),
            cep: $('#inputCep').val(),
            uf: $('#inputUf').val(),
            cidade: $('#inputCidade').val(),
            idCliente : null,
        };
    
        cliente.enderecos.push(endereco2);
    
        $('#inputLogradouro').val('');
        $('#inputNumero').val('');
        $('#inputComplemento').val('');
        $('#inputBairro').val('');
        $('#inputCep').val('');
        $('#inputUf').val('');
        $('#inputCidade').val('');
    
        alert('Endereço adicionado com sucesso!');
    
   
        $('#enderecoModal').modal('hide');
    });
    
    
    $('#formCadastroCliente').on('submit', function (event) {
        event.preventDefault();
    
        var clienteData = {
                nome: document.getElementById('nome').value,
                genero: document.getElementById('inputGenero').value,
                dataNascimento: document.getElementById('dataNascimento').value,
                cpf: document.getElementById('cpf').value,
                email: document.getElementById('email').value,
                senha: document.getElementById('senha').value,
                confirmaSenha: document.getElementById('confirmaSenha').value,
                enderecos: cliente.enderecos.slice() 
            };
    
        var endereco1 = {
            logradouro: $('#logradouro').val(),
            numero: $('#numero').val(),
            complemento: $('#complemento').val(),
            bairro: $('#bairro').val(),
            cep: $('#cep').val(),
            uf: $('#uf').val(),
            cidade: $('#cidade').val(),
            idCliente : null,
        };
        clienteData.enderecos.push(endereco1);
    
      
    
            console.log(JSON.stringify(clienteData));
            $.ajax({
                type: 'POST',
                url: '/Winery/cadastrarCliente',
                data: JSON.stringify(clienteData),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    $('#modalMessages').empty();
    
                    if (data.startsWith('Sucesso:')) {
                    var mensagemSucesso = data.replace('Sucesso: ', '');
                    $('#modalMessages').append('<div class="alert alert-success" role="alert">' + mensagemSucesso + '</div>');
    
                    setTimeout(function () {
                        $('#modalMessages .alert').fadeOut('slow', function () {
                            $(this).remove();
    
                            window.location.reload(true);
                        });
                    }, 3500);
    
                    }
                },
                error: function (xhr) {
                    $('#modalMessages').empty();
    
                    var mensagemErro = xhr.responseText;
                    
                    $('#modalMessages').append('<div class="alert alert-danger" role="alert">' + mensagemErro + '</div>');
                    setTimeout(function () {
                        $('#modalMessages .alert').fadeOut('slow', function() {
                            $(this).remove();
                        });
                    }, 3500);
                  }
            });
        });
    });
    
    
    
    document.addEventListener("DOMContentLoaded", () => {
      addEventListeners();
    });
    
    const addEventListeners = () => {
      document
        .getElementById("cep")
        .addEventListener("blur", handleCEPBlur());
      document
        .getElementById("inputCep")
        .addEventListener("blur", handleCEPBlur());
    };
    
    const handleCEPBlur = () => (e) => {
      if (e.target.value.length >= 8) {
        var valor = e.target.id.toLowerCase();  
    console.log(valor)
        if (valor.startsWith("input")) {
          fetchAddress2();
        } else {
          fetchAddress();
        }
        
      }
    };
    
    
    const fetchAddress = () => {
      let cep = document.getElementById('cep');
    
      fetch(`https://viacep.com.br/ws/${cep.value}/json/`)
        .then((response) => response.json())
        .then((data) => {
          if (data.erro) {
            alert("CEP não encontrado!");
          } else {
            document.getElementById('logradouro').value = data.logradouro;
            document.getElementById('bairro').value = data.bairro;
            document.getElementById('cidade').value = data.localidade;
            document.getElementById('uf').value = data.uf;
          }
        })
        .catch(() => {
          alert("Erro ao buscar o CEP!");
        });
    };
    
    const fetchAddress2 = () => {
      let cep = document.getElementById('inputCep');
      
    
      fetch(`https://viacep.com.br/ws/${cep.value}/json/`)
        .then((response) => response.json())
        .then((data) => {
          if (data.erro) {
            alert("CEP não encontrado!");
          } else {
            document.getElementById('inputLogradouro').value = data.logradouro;
            document.getElementById('inputBairro').value = data.bairro;
            document.getElementById('inputCidade').value = data.localidade;
            document.getElementById('inputUf').value = data.uf;
          }
        })
        .catch(() => {
          alert("Erro ao buscar o CEP!");
        });
    };
    
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
      var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
      });
    
    