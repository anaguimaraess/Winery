document.addEventListener("DOMContentLoaded", () => {
    document
        .getElementById("inputCEP")
        .addEventListener("blur", handleCEPBlur());
});


const handleCEPBlur = () => (e) => {
    if (e.target.value.length >= 8) {

        fetchAddress2(e);

    }
};


const fetchAddress2 = () => {
    let cep = document.getElementById('inputCEP');

    fetch(`https://viacep.com.br/ws/${cep.value}/json/`)
        .then((response) => response.json())
        .then((data) => {
            if (data.erro) {
                alert("CEP não encontrado!");
            } else {
                document.getElementById('inputAddress').value = data.logradouro;
                document.getElementById('inputBairro').value = data.bairro;
                document.getElementById('inputCity').value = data.localidade;
                document.getElementById('inputEstado').value = data.uf;
            }
        })
        .catch(() => {
            alert("Erro ao buscar o CEP!");
        });
};

$('#enderecoModal').on('submit', function (event) {
event.preventDefault();

var endereco = {
logradouro: document.getElementById('inputAddress').value,
numero: document.getElementById('inputNumero').value,
complemento: document.getElementById('inputComplemento').value,
bairro: document.getElementById('inputBairro').value,
cep: document.getElementById('inputCEP').value,
uf: document.getElementById('inputEstado').value,
cidade: document.getElementById('inputCity').value,
idCliente: document.getElementById('idClienteEndereco').value
};



$.ajax({
    type: 'POST',
    url: '/cliente/adicionarEndereco',
    data: JSON.stringify(endereco),
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


$(document).ready(function() {
$('input[type="radio"]').click(function() {
var enderecoId = $(this).val();
var confirmar = confirm("Deseja definir este endereço como padrão?");
if (confirmar) {
    $.ajax({
        url: '/cliente/definirPadrao/'+enderecoId,
        type: 'POST',
        success: function(response) {
            alert('Endereço definido como padrão com sucesso.');
        },
        error: function() {
            alert('Ocorreu um erro ao definir o endereço como padrão.');
        }
    });
}
});
});
