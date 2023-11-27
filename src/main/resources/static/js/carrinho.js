let pedido = JSON.parse(localStorage.getItem('pedido'));

        function savePaymentForm() {
            const formaPagamentoSelecionada = document.querySelector('input[name="campoFormasPagamento"]:checked');
            if (formaPagamentoSelecionada) {
                const formaPagamentoSelecionadaId = formaPagamentoSelecionada.id;

                const pedido = JSON.parse(localStorage.getItem('pedido')) || {};
                pedido.formaPagamento = formaPagamentoSelecionadaId;

                localStorage.setItem('pedido', JSON.stringify(pedido));
                localStorage.setItem('formaPagamento', formaPagamentoSelecionadaId);
            } else {
                alert('Por favor, selecione uma forma de pagamento.');
            }
        }





        document.addEventListener("DOMContentLoaded", function () {
            const avancarButton = document.getElementById("avancarButton");

            avancarButton.addEventListener("click", function () {
                const paymentRadios = document.querySelectorAll('input[type="radio"][name="campoFormasPagamento"]');
                let isPaymentSelected = false;

                paymentRadios.forEach(function (radio) {
                    if (radio.checked) {
                        isPaymentSelected = true;
                    }
                });

                if (isPaymentSelected) {
                    const formaPagamentoSelecionada = document.querySelector('input[name="campoFormasPagamento"]:checked');
                    const formaPagamentoSelecionadaId = formaPagamentoSelecionada.id;

                    if (formaPagamentoSelecionadaId === 'cartaoDeCredito') {
                        verificarCamposDoCartao(function (isValid) {
                            if (isValid) {
                                savePaymentForm();
                                window.location.href = "checkout";
                            }
                        });
                    } else {
                        savePaymentForm();
                        window.location.href = "checkout";
                    }
                } else {
                    alert("Por favor, selecione uma forma de pagamento antes de avançar.");
                }
            });
        });

        document.querySelector("#cadastrarCartaoButton").addEventListener("click", function () {
            // Chama a função de verificação antes de prosseguir
            verificarCamposDoCartao(function (isValid) {
                if (isValid) {
                    // Se a validação passar, salva o formulário de pagamento e avança
                    savePaymentForm();
                    alert("Cartão cadastrado com sucesso!");
                    // Você pode redirecionar ou realizar outras ações aqui conforme necessário
                }
            });
        });
        function atualizarValorPorParcela() {
            const pedido = JSON.parse(localStorage.getItem('pedido')) || {};
            const parcelasSelect = document.getElementById('parcelas');
            const valorPorParcelaInput = document.getElementById('valorPorParcela');

            const valorTotal = pedido.valorTotal;
            const numeroParcelas = parseInt(parcelasSelect.value);

            if (numeroParcelas > 1) {
                const valorPorParcela = valorTotal / numeroParcelas;
                valorPorParcelaInput.value = `R$ ${valorPorParcela.toFixed(2)}`;
            } else {
                valorPorParcelaInput.value = '';
            }
        }

        function verificarCamposDoCartao(callback) {
            console.log("Chamando verificarCamposDoCartao");

            const cardPaymentRadio = document.getElementById('cartaoDeCredito');

            if (cardPaymentRadio.checked) {
                const cardNumberInput = document.getElementById('cardNumber');
                const cardHolderNameInput = document.getElementById('cardHolderName');
                const expiryDateInput = document.getElementById('expiryDate');
                const cvvInput = document.getElementById('cvv');

                const cardNumber = cardNumberInput.value.replace(/\D/g, '');
                const cardHolderName = cardHolderNameInput.value.trim();
                const expiryDate = expiryDateInput.value.replace(/\D/g, '');
                const cvv = cvvInput.value.replace(/\D/g, '');

                if (cardNumber.length !== 16) {
                    alert("O número do cartão deve ter 16 dígitos.");
                    callback(false);
                } else if (cardHolderName === "") {
                    alert("Por favor, insira o nome do titular do cartão.");
                    callback(false);
                } else if (!verificarDataExpiracaoMenorQueAtual(expiryDate)) {
                    callback(false);
                } else if (cvv.length !== 3) {
                    alert("O CVV do cartão deve ter 3 dígitos.");
                    callback(false);
                } else {
                    callback(true);
                }
            } else {
                callback(true);
            }
        }

        const cartaoDeCredito = {
            numero: "",
            nome: "",
            validade: "",
            cvv: ""
        };

        document.querySelector("form").addEventListener("submit", function (event) {
            event.preventDefault();

            cartaoDeCredito.numero = document.getElementById("cardNumber").value;
            cartaoDeCredito.nome = document.getElementById("cardHolderName").value;
            cartaoDeCredito.validade = document.getElementById("expiryDate").value;
            cartaoDeCredito.cvv = document.getElementById("cvv").value;

            const pedido = JSON.parse(localStorage.getItem('pedido')) || {};
            pedido.cartaoDeCredito = cartaoDeCredito;
            pedido.cartaoDeCredito = cartaoDeCredito;
            pedido.numeroParcelas = parseInt(document.getElementById('parcelas').value);
            localStorage.setItem('pedido', JSON.stringify(pedido));
        });

        const cardNumberInput = document.getElementById('cardNumber')
        cardNumberInput.addEventListener('input', function () {
            const value = this.value;
            if (/\D/g.test(value)) {

                alert('Este campo aceita apenas valores numéricos!')
                this.value = this.value.replace(/\D/g, '')
            }

            if (this.value.length > 16) {
                this.value = this.value.slice(0, 16)
            }
        });

        const cardHolderNameInput = document.getElementById('cardHolderName')
        cardHolderNameInput.addEventListener('input', function () {

            const value = this.value;
            if (/\d/.test(value)) {

                alert('Este campo não aceita valores numéricos!')
                this.value = value.replace(/\d/g, '')
            }
            if (this.value.length > 20) {
                this.value = this.value.slice(0, 20)
            }
        });

        const cvvInput = document.getElementById('cvv');
        cvvInput.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '');
            if (this.value.length > 3) {
                this.value = this.value.slice(0, 3);
            }
        });






        // controle dos radio e cards
        function showCreditCardForm() {
            const creditCardForm = document.getElementById("creditCardForm");
            const creditCardRadio = document.getElementById("cartaoDeCredito");

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
            const boletoRadio = document.getElementById("boleto");

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
            const pixRadio = document.getElementById("pix");

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

        //------------------------------- mascarando campo de data vencimento
        const expiryDateInput = document.getElementById('expiryDate');
        expiryDateInput.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '');

            if (this.value.length === 2 && this.value.indexOf('/') === -1) {
                this.value += '/';
            } else if (this.value.length > 2) {
                this.value = this.value.slice(0, 2) + '/' + this.value.slice(2);
            }

            if (this.value.length > 5) {
                this.value = this.value.slice(0, 5);
            }


            if (this.value.length >= 2) {
                const month = parseInt(this.value.slice(0, 2));
                if (month > 12) {
                    this.value = '12';
                }
            }

        });


        function verificarDataExpiracaoMenorQueAtual(inputValue) {
            if (inputValue.length === 4) {
                const month = parseInt(inputValue.slice(0, 2), 10);
                const year = parseInt(inputValue.slice(2, 4), 10);

                const today = new Date();
                const currentMonth = today.getMonth() + 1; // Mês é baseado em zero no JavaScript
                const currentYearFull = today.getFullYear() % 100;

                if (year < currentYearFull || (year === currentYearFull && month < currentMonth)) {
                    alert('Cartão vencido? A data de expiração não pode ser menor que a data atual.');
                    return false
                }
                return true;
            }
        }

        // Função para verificar se a sessão está ativa
function isSessionActive(callback) {
    // Fazer uma requisição para a rota do controlador
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

  // Função para alternar entre "Meu perfil" e "Faça login/crie seu login"
  function toggleSessionElements() {
    const perfilButton = document.getElementById("perfil-button");
    const loginLink = document.getElementById("login-link");

    isSessionActive(function (isSessionActive) {
      if (isSessionActive) {
        // Sessão ativa, mostrar "Meu perfil" e ocultar "Faça login/crie seu login"
        perfilButton.style.display = "block";
        loginLink.style.display = "none";
      } else {
        // Sessão inativa, mostrar "Faça login/crie seu login" e ocultar "Meu perfil"
        perfilButton.style.display = "none";
        loginLink.style.display = "block";
      }
    });
  }

  // Chame a função para alternar os elementos com base no estado da sessão
  toggleSessionElements();




    function showLogoutMessage() {
    window.alert("Sessão encerrada com sucesso.");
    // Redirecione para a página de logout
    window.location.href = "/authentication/logout";
}


      
        
        