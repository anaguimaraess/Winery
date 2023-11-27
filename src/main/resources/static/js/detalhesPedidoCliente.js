var valorFreteDoPedido = document.getElementById("valorFrete").textContent;
var valorTotalDoPedido = document.getElementById("valorTotal").textContent;

var valorTotalSemFrete = valorTotalDoPedido - valorFreteDoPedido;

var valorDosProdutosElement = document.getElementById("valorDosProdutos");


if (valorDosProdutosElement) {

valorDosProdutosElement.textContent = "Valor dos Produtos: R$ " + valorTotalSemFrete.toFixed(2);
} else {
console.error("Elemento HTML não encontrado.");
}


function isSessionActive(callback) {

fetch('/verificar-sessao')
  .then(response => response.json())
  .then(data => {
    callback(data);
  })
  .catch(error => {
    console.error('Erro ao verificar a sessão:', error);
    callback(false); 
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
