
function showCreditCardForm() {
    const creditCardForm = document.getElementById("creditCardForm");
    const creditCardRadio = document.getElementById("flexRadioDefault3");

    if (creditCardRadio.checked) {
        creditCardForm.style.display = "block";
    } else {
        creditCardForm.style.display = "none";
    }
}

function activateRadio(radioId) {
    const radioCartao = document.getElementById(radioId);
    if (radioCartao) {
        radioCartao.checked = true;
        showCreditCardForm();
    }
}

function showBoletoForm() {
    const boletoForm = document.getElementById("boletoForm");
    const boletoRadio = document.getElementById("flexRadioDefault2");

    if (boletoRadio.checked) {
        boletoForm.style.display = "block";
    } else {
        boletoForm.style.display = "none";
    }
}

function activateRadio(radioId) {
    const radioBoleto = document.getElementById(radioId);
    if (radioBoleto) {
        radioBoleto.checked = true;
        showBoletoForm();
    }
}

function showPixForm() {
    const pixForm = document.getElementById("pixForm");
    const pixRadio = document.getElementById("flexRadioDefault1");

    if (pixRadio.checked) {
        pixForm.style.display = "block";
    } else {
        pixForm.style.display = "none";
    }
}

function activateRadio(radioId) {
    const radioPix = document.getElementById(radioId);
    if (radioPix) {
        radioPix.checked = true;
        showPixForm();
    }
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


function atualizarQuantidadeItensCarrinho() {
document.getElementById("carrinhoItens").textContent = quantidadeItensCarrinho;
}

let carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];

quantidadeItensCarrinho = carrinho.reduce((total, item) => total + item.quantidade, 0);
atualizarQuantidadeItensCarrinho();