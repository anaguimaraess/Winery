package br.com.ecommerce.winery.models.exception;

public class BusinessException extends Exception{
    public BusinessException(String mensagem){
        super(mensagem);
    }
}
