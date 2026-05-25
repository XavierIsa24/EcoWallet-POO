package com.projeto.ecowallet.controller;

import com.projeto.ecowallet.factory.TransacaoFactory;
import com.projeto.ecowallet.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MainController {

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtValor;

    @FXML
    private TextField txtData;

    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private Label lblSaldo;

    @FXML
    private TableView<Transacao> tabelaTransacoes;

    @FXML
    private TableColumn<Transacao, String> colDescricao;

    @FXML
    private TableColumn<Transacao, Double> colValor;

    @FXML
    private TableColumn<Transacao, String> colData;

    @FXML
    private TableColumn<Transacao, TipoTransacao> colTipo;

    @FXML
    private TableColumn<Transacao, Categoria> colCategoria;

    private final GerenciadorFinancas gerenciador =
            new GerenciadorFinancas();

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {

        comboTipo.getItems().addAll("RECEITA", "DESPESA");

        comboCategoria.getItems().addAll(
                "ALIMENTACAO", "TRANSPORTE", "LAZER", "SALARIO", "CONTAS"
        );

        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        tabelaTransacoes.setItems(gerenciador.getTransacoes());

        atualizarSaldo();
    }

    @FXML
    private void adicionarTransacao() {
        try {
            String descricao = txtDescricao.getText();

            double valor = Double.parseDouble(txtValor.getText());

            LocalDate data = LocalDate.parse(txtData.getText(), FORMATO_DATA);

            TipoTransacao tipo = TipoTransacao.valueOf(comboTipo.getValue());

            Categoria categoria = Categoria.valueOf(comboCategoria.getValue());

            Transacao transacao = TransacaoFactory.criarTransacao(
                    descricao, valor, data, tipo, categoria
            );

            gerenciador.adicionarTransacao(transacao);
            atualizarSaldo();
            limparCampos();

        } catch (DateTimeParseException e) {
            mostrarAlerta("Data inválida! Use o formato dd/MM/yyyy.");
        } catch (Exception e) {
            mostrarAlerta("Preencha todos os campos corretamente!");
        }
    }

    @FXML
    private void removerTransacao() {
        Transacao selecionada =
                tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta("Selecione uma transação na tabela para remover.");
            return;
        }

        gerenciador.removerTransacao(selecionada);
        atualizarSaldo();
    }

    private void atualizarSaldo() {
        lblSaldo.setText(
                String.format("Saldo Total: R$ %.2f", gerenciador.calcularSaldo())
        );
    }

    private void limparCampos() {
        txtDescricao.clear();
        txtValor.clear();
        txtData.clear();
        comboTipo.setValue(null);
        comboCategoria.setValue(null);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}