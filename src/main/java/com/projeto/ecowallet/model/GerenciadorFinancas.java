package com.projeto.ecowallet.model;

import com.projeto.ecowallet.persistence.Persistenciajson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GerenciadorFinancas {

    private final ObservableList<Transacao> transacoes;
    private final Persistenciajson persistencia;

    public GerenciadorFinancas() {
        this.persistencia = new Persistenciajson();
        this.transacoes = FXCollections.observableArrayList(
                persistencia.carregar()
        );
    }

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
        persistencia.salvar(transacoes);
    }

    public void removerTransacao(Transacao transacao) {
        transacoes.remove(transacao);
        persistencia.salvar(transacoes);
    }

    public ObservableList<Transacao> getTransacoes() {
        return transacoes;
    }

    public double calcularSaldo() {
        double saldo = 0;
        for (Transacao t : transacoes) {
            saldo += t.getValorParaSaldo();
        }
        return saldo;
    }
}