package br.com.fatec.controller;

import br.com.fatec.App; 
import br.com.fatec.dao.EquipamentoDAO;
import br.com.fatec.model.Equipamento;  
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.model.Funcionario;  
import br.com.fatec.dao.ManutencaoDAO; 
import br.com.fatec.model.Manutencao;    

import java.io.IOException; 
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Diego, Gustavo e Matheus
 */
public class ManutencaoController implements Initializable {

    @FXML
    private Button btn_sair;
    @FXML
    private ComboBox<Equipamento> cmb_equipamento; 
    @FXML
    private ComboBox<Funcionario> cmb_responsavel; 
    @FXML
    private TextField lbl_Nserie;
    @FXML
    private DatePicker date_validade;
    @FXML
    private TextArea lblA_descricao; 
    @FXML
    private Button btn_incluir;
    @FXML
    private Button btn_alterar; 

    // DAOs
    private EquipamentoDAO equipamentoDAO;
    private FuncionarioDAO funcionarioDAO;
    private ManutencaoDAO manutencaoDAO; 
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipamentoDAO = new EquipamentoDAO();
        funcionarioDAO = new FuncionarioDAO();
        manutencaoDAO = new ManutencaoDAO(); 

        carregarEquipamentosNoComboBox();
        carregarResponsaveisNoComboBox();

        // Adiciona um listener para atualizar o numero de serie quando um equipamento e selecionado
        cmb_equipamento.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("DEBUG: Equipamento selecionado: " + newSelection.getNome()); 
                System.out.println("DEBUG: Numero de Serie do equipamento selecionado: " + newSelection.getNumeroDeSerie()); 
                
                if (lbl_Nserie != null) {
                    lbl_Nserie.setText(newSelection.getNumeroDeSerie());
                    System.out.println("DEBUG: lbl_Nserie.setText() executado com sucesso."); // DEPURACAO
                } else {
                    System.err.println("ERRO GRAVE: lbl_Nserie e nulo! Injecao FXML falhou."); // DEPURACAO
                    showAlert(AlertType.ERROR, "Erro Interno da UI", "O campo 'Número de Série' não foi carregado corretamente. Por favor, reinicie o aplicativo.");
                }
                
            } else {
                lbl_Nserie.clear(); // Limpa o campo se nada for selecionado
                clearFormExcludingEquipmentAndSerial(); // Novo método para limpar o resto do formulário
            }
        });
    }    

    /**
     * Carrega os equipamentos do banco de dados que estao com status "Ativo" e os exibe no ComboBox de equipamentos.
     */
    private void carregarEquipamentosNoComboBox() {
        try {
            List<Equipamento> equipamentos = equipamentoDAO.listarTodosEquipamentos(); 
            ObservableList<Equipamento> observableEquipamentos = FXCollections.observableArrayList(equipamentos);
            cmb_equipamento.setItems(observableEquipamentos);
            System.out.println("DEBUG: Equipamentos carregados na combo: " + equipamentos.size()); 
            for(Equipamento eq : equipamentos) { 
                System.out.println("DEBUG:   - " + eq.getNome() + " (Serie: " + eq.getNumeroDeSerie() + ")");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar equipamentos do banco de dados: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar os equipamentos do banco de dados.");
        }
    }

    /**
     * Carrega os funcionarios (responsaveis) do banco de dados e os exibe no ComboBox de responsaveis.
     */
    private void carregarResponsaveisNoComboBox() {
        try {
            List<Funcionario> funcionarios = funcionarioDAO.listarTodosFuncionarios();
            ObservableList<Funcionario> observableFuncionarios = FXCollections.observableArrayList(funcionarios);
            cmb_responsavel.setItems(observableFuncionarios);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar responsaveis do banco de dados: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar os responsaveis do banco de dados.");
        }
    }

    @FXML
    private void btn_sair_click(ActionEvent event) {
        System.out.println("Navegando de volta para o Menu Principal.");
        try {
            App.setRoot("view/MenuPrincipal"); // Define o MenuPrincipal.fxml como a nova tela raiz
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            // Exibe um alerta ao usuario em caso de falha na navegacao
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }

    @FXML
    private void cmb_equipamento_change(ActionEvent event) {

    }


    @FXML
    private void date_validade_click(ActionEvent event) {

    }

    @FXML
    private void btn_incluir_click(ActionEvent event) {
        Equipamento equipamentoSelecionado = cmb_equipamento.getValue();
        Funcionario responsavelSelecionado = cmb_responsavel.getValue();
        LocalDate dataFimManutencao = date_validade.getValue();
        String descricaoServico = lblA_descricao.getText().trim();

        if (equipamentoSelecionado == null || responsavelSelecionado == null || dataFimManutencao == null || descricaoServico.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos Obrigatorios", "Por favor, preencha todos os campos para incluir a manutencao.");
            return;
        }

        Manutencao novaManutencao = new Manutencao(
            equipamentoSelecionado.getIdEquipamento(),
            responsavelSelecionado.getIdFuncionario(),
            LocalDate.now(), // Data de inicio da manutencao e a data atual
            dataFimManutencao,
            descricaoServico
        );

        try {
            if (manutencaoDAO.adicionarManutencao(novaManutencao)) {
                showAlert(AlertType.INFORMATION, "Manutencao Registrada", "Manutencao cadastrada e status do equipamento atualizado para 'Em Manutencao'!");
                clearFormCompleto(); 
                carregarEquipamentosNoComboBox();
            } else {
                showAlert(AlertType.ERROR, "Erro", "Nao foi possivel cadastrar a manutencao. Tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao registrar manutencao: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao registrar a manutencao: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar manutencao: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar cadastrar. Tente novamente mais tarde.");
        }
    }

    @FXML
    private void btn_alterar_click(ActionEvent event) {
        System.out.println("Navegando para a tela de Equipamentos em Manutencao.");
        try {
            App.setRoot("view/EquipamentosEmManutencao"); // Redireciona para a tela de EquipamentosEmManutencao.fxml
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de Equipamentos em Manutencao: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel carregar a tela de Equipamentos em Manutencao. Detalhes: " + e.getMessage());
        }
    }
    
    private void clearFormExcludingEquipmentAndSerial() {
        cmb_responsavel.getSelectionModel().clearSelection();
        date_validade.setValue(null);
        lblA_descricao.clear();
    }

    private void clearFormCompleto() {
        cmb_equipamento.getSelectionModel().clearSelection(); 
        cmb_responsavel.getSelectionModel().clearSelection();
        lbl_Nserie.clear(); 
        date_validade.setValue(null);
        lblA_descricao.clear();
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
        System.out.println("Navegando de volta para o Menu Principal.");
        try {
            App.setRoot("view/MenuPrincipal"); // Define o MenuPrincipal.fxml como a nova tela raiz
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            // Exibe um alerta ao usuario em caso de falha na navegacao
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }
}
