function openCadastroModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "block";
}


function closeCadastroModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}


var openCadastroModalBtn = document.getElementById("openModalBtn");
openCadastroModalBtn.addEventListener("click", openCadastroModal);


var closeCadastroBtn = document.querySelector("#myModal .close");
closeCadastroBtn.addEventListener("click", closeCadastroModal);


window.addEventListener("click", function (event) {
    var modal = document.getElementById("myModal");
    if (event.target == modal) {
        closeCadastroModal();
    }
});


function openAlterModal() {
    var modal = document.getElementById("alterModal");
    modal.style.display = "block";
}


function closeAlterModal() {
    var modal = document.getElementById("alterModal");
    modal.style.display = "none";
}


var openAlterModalBtn = document.getElementById("openModalAlter");
openAlterModalBtn.addEventListener("click", openAlterModal);


var closeAlterModalBtn = document.querySelector("#alterModal .close");
closeAlterModalBtn.addEventListener("click", closeAlterModal);


window.addEventListener("click", function (event) {
    var modal = document.getElementById("alterModal");
    if (event.target == modal) {
        closeAlterModal();
    }
});