function removerMensagens(elementId) {
    const mensagemElement = document.getElementById(elementId);

    if (mensagemElement && mensagemElement.classList.contains('mensagem-texto', 'sucesso')) {
        setTimeout(function() {
            mensagemElement.remove();
        }, 5000); // 5000 milissegundos = 5 segundos
    }
}

document.addEventListener('DOMContentLoaded', function() {
    removerMensagens('mensagemDiv');
});