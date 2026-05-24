package com.projeto.ecowallet.factory;

import com.projeto.ecowallet.model.*;

import java.time.LocalDate;

public class TransacaoFactory {

    public static Transacao criarTransacao(String descricao, double valor,
                                           LocalDate data, TipoTransacao tipo,
                                           Categoria categoria) {
        switch (tipo) {
            case RECEITA:
                return new Receita(descricao, valor, data, categoria);
            case DESPESA:
                return new Despesa(descricao, valor, data, categoria);
            default:
                throw new IllegalArgumentException("Tipo de transação inválido");
        }
    }
}