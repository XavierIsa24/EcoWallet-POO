package com.projeto.ecowallet.controller;
import  java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.projeto.ecowallet.factory.TransacaoFactory;
import com.projeto.ecowallet.model.Categoria;
import com.projeto.ecowallet.model.GerenciadorFinancas;
import com.projeto.ecowallet.model.Transacao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {
    private GerenciadorFinancas gerenciador = new GerenciadorFinancas();

    private ObservableList<Transacao> listaTransacoes = FXCollections.observableArrayList();
    
    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private TextField txtData;

    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<Categoria> comboCategoria;

    @FXML private TableView<Transacao> tabelaTransacoes;

    @FXML private TableColumn<Transacao, String> colDescricao;
    @FXML private TableColumn<Transacao, Double> colValor;
    @FXML private TableColumn<Transacao, LocalDate> colData;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private TableColumn<Transacao, Categoria> colCategoria;

    @FXML private Label lblSaldo;

    @FXML
    public void initialize(){
        comboTipo.getItems().addAll("Entrada", "Saída");
        comboCategoria.getItems().setAll(Categoria.values());

        tabelaTransacoes.setItems(listaTransacoes);

        colDescricao.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao())
        );

        colValor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getValor())
        );

        colData.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getData())
        );

        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo())
        );

        colCategoria.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCategoria())
        );

        atualizarSaldo();
    }

    @FXML
    public void adicionarTransacao(){

        String tipo = comboTipo.getValue();
        String descricao = txtDescricao.getText();
        String valorTexto = txtValor.getText();
        String dataTexto = txtData.getText();
        Categoria categoria = comboCategoria.getValue();

        try {

            if(descricao.isEmpty() || valorTexto.isEmpty() || dataTexto.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.setContentText("Preencha todos os campos!");
                alert.showAndWait();

                return;
            }
            
            double valor = Double.parseDouble(valorTexto);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataTexto, formatter);
            Transacao transacao = TransacaoFactory.criarTransacao(tipo, descricao, valor, data, categoria);
            gerenciador.adicionarTransacao(transacao);

            listaTransacoes.add(transacao);
            atualizarSaldo();
            limparCampos();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Transação adicionada com sucesso!");
            alert.showAndWait();

        } catch (NumberFormatException e){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Valor inválido!");
            alert.showAndWait();

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }

    private void atualizarSaldo(){
        lblSaldo.setText("Saldo: " + gerenciador.calcularSaldo());

    }

    private void limparCampos(){
        txtDescricao.clear();
        txtValor.clear();
        txtData.clear();
        comboTipo.getSelectionModel().clearSelection();
        comboCategoria.getSelectionModel().clearSelection();
    }

    @FXML
    public void removerTransacao(){
        Transacao transacaoSelecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

        if(transacaoSelecionada != null){

            gerenciador.removerTransacao(transacaoSelecionada);
            listaTransacoes.remove(transacaoSelecionada);
            atualizarSaldo();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Transação removida!");
            alert.showAndWait();

        }else{

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma transação!");
            alert.showAndWait();

        }
    }
    
}
