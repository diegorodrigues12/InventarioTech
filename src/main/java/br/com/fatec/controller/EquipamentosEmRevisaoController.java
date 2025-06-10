package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.model.EquipamentoRevisao; 
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EquipamentosEmRevisaoController implements Initializable {

    @FXML
    private Button btn_sair; 
    @FXML
    private TextField lbl_observacao; 
    @FXML
    private ComboBox<String> cmb_equipameto; 
    @FXML
    private Button btn_inserir;
    @FXML
    private TableView<EquipamentoRevisao> table_equipamentos; 
    @FXML
    private TableColumn<EquipamentoRevisao, String> column_nomeEq; 
    @FXML
    private TableColumn<EquipamentoRevisao, String> column_observacao;

    private List<String> equipamentosDisponiveis = new ArrayList<>();
    
    private ObservableList<EquipamentoRevisao> listaEquipamentosEmRevisao;
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        column_nomeEq.setCellValueFactory(new PropertyValueFactory<>("nomeEquipamento"));
        column_observacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));

        listaEquipamentosEmRevisao = FXCollections.observableArrayList();
        table_equipamentos.setItems(listaEquipamentosEmRevisao); 
        
        carregarEquipamentosNoComboBox(); 
    }    

    private void carregarEquipamentosNoComboBox() { 
        equipamentosDisponiveis.add("Notebook HP EliteBook");
        equipamentosDisponiveis.add("Monitor Dell UltraSharp");
        equipamentosDisponiveis.add("Impressora Epson EcoTank");
        equipamentosDisponiveis.add("Teclado Mecanico HyperX");
        equipamentosDisponiveis.add("Mouse Logitech G502");
        equipamentosDisponiveis.add("Servidor PowerEdge R740");
        equipamentosDisponiveis.add("Switch Cisco Catalyst 2960");
        equipamentosDisponiveis.add("Roteador TP-Link Archer AX55");
        
        ObservableList<String> observableEquipamentos = FXCollections.observableArrayList(equipamentosDisponiveis);
        cmb_equipameto.setItems(observableEquipamentos); 
        cmb_equipameto.getSelectionModel().selectFirst(); 
    }

    @FXML
    private void btn_sair_click(ActionEvent event) { 
        System.out.println("Navegando de volta para o Menu Principal a partir da Revisao.");
        try {
            App.setRoot("view/MenuPrincipal");
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
        catch (Throwable t) {
            System.err.println("ERRO FATAL NA NAVEGACAO DE VOLTA PARA O MENU PRINCIPAL: " + t.getMessage());
            t.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Critico de Navegacao", "Ocorreu um erro inesperado ao tentar retornar ao Menu Principal. Detalhes Tecnicos: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }

    @FXML
    private void btn_inserir_click(ActionEvent event) {
        String equipamentoSelecionado = cmb_equipameto.getSelectionModel().getSelectedItem(); 
        String observacao = lbl_observacao.getText().trim(); 

        if (equipamentoSelecionado == null || equipamentoSelecionado.isEmpty()) {
            showAlert(AlertType.WARNING, "Selecao Invalida", "Por favor, selecione um equipamento.");
            return;
        }

        if (observacao.isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Vazio", "Por favor, digite uma observacao.");
            return;
        }

        EquipamentoRevisao novoItem = new EquipamentoRevisao(equipamentoSelecionado, observacao);
        listaEquipamentosEmRevisao.add(novoItem); 

        cmb_equipameto.getSelectionModel().clearSelection();
        lbl_observacao.clear();
        cmb_equipameto.getSelectionModel().selectFirst(); 
        
        showAlert(AlertType.INFORMATION, "Sucesso", "Equipamento adicionado a lista de revisao temporaria!");
    }

    @FXML
    private void cmb_equipameto_change(ActionEvent event) {
        // Atualmente, nenhuma logica especifica necessaria aqui,
        System.out.println("DEBUG: Equipamento selecionado na ComboBox: " + cmb_equipameto.getSelectionModel().getSelectedItem());
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); 
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void img_sair_click(MouseEvent event) {
        System.out.println("Navegando de volta para o Menu Principal a partir da Revisao.");
        try {
            App.setRoot("view/MenuPrincipal");
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
        catch (Throwable t) {
            System.err.println("ERRO FATAL NA NAVEGACAO DE VOLTA PARA O MENU PRINCIPAL: " + t.getMessage());
            t.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Critico de Navegacao", "Ocorreu um erro inesperado ao tentar retornar ao Menu Principal. Detalhes Tecnicos: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }
}
