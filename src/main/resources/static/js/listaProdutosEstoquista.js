function updateRadioInputs() {
    var preview = document.getElementById('preview');
    var radios = preview.querySelectorAll('.form-check-input');
    var labels = preview.querySelectorAll('.form-check-label');
    for (var i = 0; i < radios.length; i++) {
      radios[i].id = 'exampleRadios' + i;
      radios[i].value = i;
      labels[i].htmlFor = 'exampleRadios' + i;
    }
  }

  function removeImage(card, file) {
    var input = document.getElementById('imagem');
    var preview = document.getElementById('preview');

    preview.removeChild(card);

    var dataTransfer = new DataTransfer();
    for (var i = 0; i < input.files.length; i++) {
      if (input.files[i] !== file) {
        dataTransfer.items.add(input.files[i]);
      }
    }


    input.files = dataTransfer.files;
  }

  function handleFile(file, i) {
    console.log(file.name);
    if (!file.type.match('image/jpeg') && !file.type.match('image/png')) {
      alert('Só são permitidos arquivos de imagem (jpg, jpeg, png).');
      this.value = '';
      return;
    }

    var reader = new FileReader();

    reader.onload = function (e) {
      var img = document.createElement('img');
      img.src = e.target.result;
      img.alt = 'Imagem-produto';
      img.className = 'card-img-top';

      var cardBody = document.createElement('div');
      cardBody.className = 'card-body d-flex flex-column';

      var formCheck = document.createElement('div');
      formCheck.className = 'form-check mt-auto';

      var radioInput = document.createElement('input');
      radioInput.className = 'form-check-input';
      radioInput.type = 'radio';
      radioInput.name = 'imgPrincipal';
      radioInput.id = 'exampleRadios' + i;
      radioInput.value = i;
      var radioLabel = document.createElement('label');
      radioLabel.className = 'form-check-label';
      radioLabel.htmlFor = 'exampleRadios' + i;
      radioLabel.textContent = 'Imagem principal';

      formCheck.appendChild(radioInput);
      formCheck.appendChild(radioLabel);
      cardBody.appendChild(formCheck);

      var closeButton = document.createElement('button');
      closeButton.type = 'button';
      closeButton.className = 'btn-close';
      closeButton.setAttribute('aria-label', 'Close');

      var card = document.createElement('div');
      card.className = 'card p-2 m-1';
      card.style.width = '18rem';
      card.appendChild(closeButton);
      card.appendChild(img);
      card.appendChild(cardBody);

      closeButton.addEventListener('click', function () {
        removeImage(card, file);
        updateRadioInputs()
      });

      preview.appendChild(card);
    };

    reader.readAsDataURL(file);
  }

  document.body.addEventListener('change', function (event) {
    if (event.target.id === 'imagem') {
      var preview = document.getElementById('preview');
      preview.innerHTML = '';


      for (var i = 0; i < event.target.files.length; i++) {
        handleFile(event.target.files[i], i);
      }
    }
  });


  document.getElementById('exampleModal').addEventListener('submit', function (e) {
    var radios = document.getElementsByName('imgPrincipal');
    var formValid = false;

    var i = 0;
    while (!formValid && i < radios.length) {
      if (radios[i].checked) formValid = true;
      i++;
    }

    if (!formValid) {
      alert("Por favor, selecione uma imagem principal.");
      e.preventDefault();
    }
  });
