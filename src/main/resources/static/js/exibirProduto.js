const avaliacaoProdutoThymeleaf = parseFloat([[${produto.avaliacaoProduto}]]);

$("#star-rating").rateYo({
    rating: avaliacaoProdutoThymeleaf,
    starWidth: "16px",
    halfStar: true,
    readOnly: true
});


function atualizarImagemPrincipal(element) {
    var imagemUrl = $(element).attr('src');
    $('#imagemPrincipal').attr('src', imagemUrl);
}
$(document).ready(function () {
        $(".owl-carousel").owlCarousel({
            items: 3, 
            loop: true, 
            margin: 10, 
            nav: true, 
            responsive: {
                0: {
                    items: 1 
                },
                768: {
                    items: 3
                }
            }
        });
    });

    $(document).ready(function () {
    var owl = $(".owl-carousel");

    owl.owlCarousel({
        items: 3,
        loop: true, 
        margin: 10, 
        nav: false, 
        responsive: {
            0: {
                items: 1 
            },
            768: {
                items: 3 
            }
        }
    });

    // Adicione funcionalidade aos botões personalizados
    $("#custom-prev").click(function () {
        owl.trigger("prev.owl.carousel");
    });

    $("#custom-next").click(function () {
        owl.trigger("next.owl.carousel");
    });
});