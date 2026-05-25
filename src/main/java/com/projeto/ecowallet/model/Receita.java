package com.projeto.ecowallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

public class Receita extends Transacao {

    public Receita() {
    }

    public Receita(String descricao, double valor, LocalDate data, Categoria categoria) {
        super(descricao, valor, data, TipoTransacao.RECEITA, categoria);
    }

    public Receita(String descricao, double valor, LocalDate data,
                   TipoTransacao tipo, Categoria categoria) {
        super(descricao, valor, data, TipoTransacao.RECEITA, categoria);
    }

    @Override
    @JsonIgnore
    public double getValorParaSaldo() {
        return getValor();
    }
}