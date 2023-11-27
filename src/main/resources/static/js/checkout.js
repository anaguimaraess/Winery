
function exibirFormaDePagamento() {
    const formaPagamentoValor = localStorage.getItem('formaPagamento');
    const formaPagamentoValorElement = document.getElementById('formaPagamentoSelecionadaValor');

    const formaPagamentoMap = {
        pix: "PIX",
        boleto: "Boleto Bancário",
        cartaoDeCredito: "Cartão de Crédito",
    };

    if (formaPagamentoValorElement && formaPagamentoValor) {
        const formaPagamentoTexto = formaPagamentoMap[formaPagamentoValor] || formaPagamentoValor;
        formaPagamentoValorElement.innerText = formaPagamentoTexto;
    }
}

function getFormaPagamentoText(formaPagamento) {
    const formaPagamentoMap = {
        cartaoDeCredito: "Cartão de Crédito",
    };

    return formaPagamentoMap[formaPagamento] || formaPagamento;
}


document.addEventListener("DOMContentLoaded", () => {
    exibirFormaDePagamento()
    exibirProdutosNoCheckout()
    recuperarEndereco();
    recuperarValores();
})

function exibirProdutosNoCheckout() {
    const carrinho = JSON.parse(localStorage.getItem('carrinho'));
    const carrinhoLista = document.getElementById("carrinho-lista");

    carrinho.forEach((produto) => {
        const li = document.createElement("li");
        li.classList.add(
            "list-group-item",
            "d-flex",
            "justify-content-between",
            "align-items-center",
            "border",
            "border-top",
            "border-end-0",
            "border-bottom-0",
            "border-start-0",
            "text-white"
        );

        // Adiciona a estilização inline
        li.style.backgroundColor = "#2c263a";
        li.style.borderColor = "gray";

        // Criação de spans para nome, quantidade e preço
        const nomeSpan = document.createElement("span");
        const quantidadeSpan = document.createElement("span");
        const precoSpan = document.createElement("span");

        // Adiciona as classes necessárias para estilização
        nomeSpan.classList.add("nome-produto", "mr-3");
        quantidadeSpan.classList.add("quantidade-produto", "mr-3");
        precoSpan.classList.add("preco-produto");

        // Define os valores dos spans
        nomeSpan.textContent = produto.nome;
        quantidadeSpan.textContent = `Qtd: ${produto.quantidade}`;
        precoSpan.textContent = `R$${produto.preco}`;

        // Adiciona os spans ao <li>
        li.appendChild(nomeSpan);
        li.appendChild(quantidadeSpan);
        li.appendChild(precoSpan);

        // Adiciona o <li> à <ul>
        carrinhoLista.appendChild(li);
    });
}




function recuperarEndereco() {
    let pedido = JSON.parse(localStorage.getItem('pedido'));
    var enderecoSalvo = pedido.endereco;


    // Preencha os campos com os dados do endereço, se disponíveis
    document.getElementById('ruaLogado').innerText = 'Rua ' + (enderecoSalvo ? enderecoSalvo.logradouro : 'Endereço não disponível');
    document.getElementById('bairroLogado').innerText = enderecoSalvo ? enderecoSalvo.bairro : 'Endereço não disponível';
    document.getElementById('cepLogado').innerText = enderecoSalvo ? enderecoSalvo.cep : 'Endereço não disponível';
    document.getElementById('cidadeUfLogado').innerText = enderecoSalvo ? enderecoSalvo.cidade + ' - ' + enderecoSalvo.uf : 'Endereço não disponível';
    document.getElementById('complementoLogado').innerText = enderecoSalvo ? enderecoSalvo.complemento : 'Endereço não disponível';
}

function recuperarValores() {
    let pedido = JSON.parse(localStorage.getItem('pedido'));

    if (pedido) {
        let valorTotal = pedido.valorTotal || 0;
        let valorFrete = pedido.valorFretePedido || 0;
        let formaPagamentoSelecionada = pedido.formaPagamento || 'Não especificada';

        let totalProdutos = valorTotal - valorFrete;

        document.getElementById('valorDosProdutos').innerHTML += ` R$${totalProdutos.toFixed(2)}`;
        document.getElementById('valorFrete').innerHTML += ` R$${valorFrete.toFixed(2)}`;

        let valorTotalElement = document.getElementById('valorTotal');
        valorTotalElement.innerHTML += ` R$${pedido.valorTotal.toFixed(2)}`;
    } else {
        console.error('Pedido não encontrado no localStorage.');
    }


    if (pedido) {
        let formaPagamentoSelecionadaValor = pedido.formaPagamento || '';

        if (formaPagamentoSelecionadaValor.toLowerCase() === 'cartaodecredito') {
            let numeroParcelas = pedido.numeroParcelas || 1;
            let valorParcela = (pedido.valorTotal / numeroParcelas).toFixed(2);

            // Atualiza o texto do span
            let parcelasTexto = `${numeroParcelas} parcelas de R$ ${valorParcela}`;
            document.getElementById('parcelasTexto').innerText = parcelasTexto;

            // Exibe o span
            document.getElementById('parcelasTexto').style.display = 'inline';
        }
    } else {
        console.error('Pedido não encontrado no localStorage.');
    }

};


function isSessionActive(callback) {
    fetch('/verificar-sessao')
      .then(response => response.json())
      .then(data => {
        callback(data); // Chamar a função de callback com o resultado
      })
      .catch(error => {
        console.error('Erro ao verificar a sessão:', error);
        callback(false); // Tratar erros como sessão inativa
      });
  }

  function toggleSessionElements() {
    const perfilButton = document.getElementById("perfil-button");
    const loginLink = document.getElementById("login-link");

    isSessionActive(function (isSessionActive) {
      if (isSessionActive) {
        perfilButton.style.display = "block";
        loginLink.style.display = "none";
      } else {
        perfilButton.style.display = "none";
        loginLink.style.display = "block";
      }
    });
  }

  toggleSessionElements();


    function showLogoutMessage() {
    window.alert("Sessão encerrada com sucesso.");
    window.location.href = "/authentication/logout";
}


let pedidoLocalStorage = JSON.parse(localStorage.getItem('pedido'));

var pedidoJson = localStorage.getItem('pedido');
let pedidoAdaptado = {
    id: 0, 
    status: null, 
    itensPedido: pedidoLocalStorage.carrinhoPedido,
    idEndereco: pedidoLocalStorage.endereco.idEndereco, 
    endereco: pedidoLocalStorage.endereco,
    cartaoDeCredito: null, 
    numeroParcelas: pedidoLocalStorage.numeroParcelas,
    idDoCliente: pedidoLocalStorage.idDoCliente,
    valorTotal: pedidoLocalStorage.valorTotal,
    valorFretePedido: pedidoLocalStorage.valorFretePedido,
    formaPagamento: pedidoLocalStorage.formaPagamento,
    dataPedido: null 
};

var te = JSON.parse(pedidoJson);
console.log("ok: " , pedidoAdaptado)
te.status =0;
console.info(te)


async function enviarPedidoParaServidor(btn) {
    try {
        const response = await fetch('/salvarJsonPedido', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(pedidoAdaptado),
        });

        const data = await response.text();
        const modalMessages = $('#modalMessages');

        if (data.startsWith('Sucesso:')) {
            const mensagemSucesso = data.replace('Sucesso:', '');
            modalMessages.html('<div class="alert alert-success" role="alert">' + mensagemSucesso + '</div>');

            localStorage.clear();

            setTimeout(function () {
                window.location.href = "/Winery";
            }, 2500);
        } else if (data.startsWith('Erro:')) {
            const mensagemErro = data.replace('Erro:', '');
            modalMessages.html('<div class="alert alert-danger" role="alert">' + mensagemErro + '</div>');
        } else {
            throw new Error('Erro ao finalizar pedido');
        }
    } catch (error) {
        console.error('Erro ao enviar ou processar JSON:', error);
    }
}

