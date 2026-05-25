package com.projeto.ecowallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

public class Despesa extends Transacao {

    public Despesa() {
    }

    public Despesa(String descricao, double valor, LocalDate data, Categoria categoria) {
        super(descricao, valor, data, TipoTransacao.DESPESA, categoria);
    }

    public Despesa(String descricao, double valor, LocalDate data,
                   TipoTransacao tipo, Categoria categoria) {
        super(descricao, valor, data, TipoTransacao.DESPESA, categoria);
    }

    @Override
    @JsonIgnore
    public double getValorParaSaldo() {
        return -getValor();
    }
}