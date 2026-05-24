package com.projeto.ecowallet.factory;

import java.time.LocalDate;

import com.projeto.ecowallet.model.Categoria;
import com.projeto.ecowallet.model.Despesa;
import com.projeto.ecowallet.model.Receita;
import com.projeto.ecowallet.model.Transacao;

public class TransacaoFactory {
    public static Transacao criarTransacao(
        String tipo,
        String descricao,
        double valor, 
        LocalDate data,
        Categoria categoria
    
    ){

        if (tipo.equalsIgnoreCase("Entrada")){
            return new Receita(descricao, valor, data, categoria);
        } else if (tipo.equalsIgnoreCase("Saída")) {
            return new Despesa(descricao, valor, data, categoria);
        } else {
            throw new IllegalArgumentException("Tipo de transação inválida.");
        }
    }
}
