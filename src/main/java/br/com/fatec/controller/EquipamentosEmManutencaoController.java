package br.com.fatec.controller;

import br.com.fatec.App; 
import br.com.fatec.dao.EquipamentoDAO; 
import br.com.fatec.model.Equipamento;   

import java.io.IOException; 
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert; 
import javafx.scene.control.Alert.AlertType; 
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType; 
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
import javafx.scene.control.cell.PropertyValueFactory; 
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EquipamentosEmManutencaoController implements Initializable {

    @FXML
    private Button btn_sair; 
    @FXML
    private TableView<Equipamento> table_manutencao;
    @FXML
    private TableColumn<Equipamento, Integer> column_id; 
    @FXML
    private TableColumn<Equipamento, String> column_equipamento; 
    @FXML
    private TableColumn<Equipamento, String> column_Nserie; 
    @FXML
    private TableColumn<Equipamento, String> column_categoria; 
    @FXML
    private TableColumn<Equipamento, String> column_descricao; 
    @FXML
    private TextField txt_equipamentoId; 
    @FXML
    private Button btn_finalizado;

    // DAOs
    private EquipamentoDAO equipamentoDAO;
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipamentoDAO = new EquipamentoDAO();
        
        // Configura as colunas da TableView
        column_id.setCellValueFactory(new PropertyValueFactory<>("idEquipamento"));
        column_equipamento.setCellValueFactory(new PropertyValueFactory<>("nome"));
        column_Nserie.setCellValueFactory(new PropertyValueFactory<>("numeroDeSerie"));
        column_categoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        column_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        carregarEquipamentosEmManutencao(); // Carrega os dados
    }    

    @FXML
    private void btn_sair_click(ActionEvent event) {
        System.out.println("Navegando de volta para o Menu Principal a partir de Equipamentos em Manutencao.");
        try {
            App.setRoot("view/MenuPrincipal");
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }

    @FXML
    private void btn_finalizado_click(ActionEvent event) {
        System.out.println("DEBUG: Botao 'Finalizado' clicado."); // DEPURACAO
        
        if (txt_equipamentoId == null) { // Verifica se o TextField foi injetado
            System.err.println("ERRO GRAVE: txt_equipamentoId e nulo! Injeção FXML falhou.");
            showAlert(AlertType.ERROR, "Erro Interno", "O campo de ID nao foi carregado corretamente. Reinicie o aplicativo.");
            return;
        }

        String idText = txt_equipamentoId.getText().trim();
        System.out.println("DEBUG: Texto digitado no campo ID: '" + idText + "'"); // DEPURACAO

        if (idText.isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Vazio", "Por favor, digite o ID do equipamento a ser finalizado.");
            return;
        }

        try {
            int idEquipamento = Integer.parseInt(idText);
            System.out.println("DEBUG: ID do equipamento a ser finalizado (parsed): " + idEquipamento);

            // Confirmacao antes de finalizar
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Finalizacao");
            alert.setHeaderText("Voce tem certeza que deseja finalizar a manutencao do equipamento ID: " + idEquipamento + "?");
            alert.setContentText("O status do equipamento sera alterado para 'Ativo' (ID 2).");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("DEBUG: Confirmacao de finalizacao aceita pelo usuario.");
                // Tenta buscar o equipamento para verificar se existe e se esta em manutencao
                Equipamento equipamentoParaFinalizar = equipamentoDAO.buscarEquipamentoPorId(idEquipamento);
                System.out.println("DEBUG: Equipamento buscado por ID: " + (equipamentoParaFinalizar != null ? equipamentoParaFinalizar.getNome() : "Nao encontrado")); // DEPURACAO

                if (equipamentoParaFinalizar == null) {
                    showAlert(AlertType.ERROR, "Equipamento Nao Encontrado", "Nao foi encontrado equipamento com o ID: " + idEquipamento);
                    return;
                }

                // Verifica se o equipamento realmente esta com status "Em Manutencao" (ID 1)
                // Se o status for diferente, avisa o usuario.
                if (equipamentoParaFinalizar.getIdStatus() != 1) { 
                    System.out.println("DEBUG: Status atual do equipamento: " + equipamentoParaFinalizar.getIdStatus() + ". Esperado: 1."); // DEPURACAO
                    showAlert(AlertType.WARNING, "Status Incorreto", "O equipamento ID " + idEquipamento + " nao esta com status 'Em Manutencao'. Status atual: " + equipamentoParaFinalizar.getIdStatus() + ".");
                    return;
                }

                // ID 2 e o status "Ativo"
                boolean atualizado = equipamentoDAO.atualizarStatusEquipamento(idEquipamento, 2); 
                System.out.println("DEBUG: Resultado da atualizacao do status do equipamento: " + atualizado); // DEPURACAO

                if (atualizado) { 
                    showAlert(AlertType.INFORMATION, "Manutencao Finalizada", "Manutencao do equipamento ID " + idEquipamento + " finalizada com sucesso! Status alterado para 'Ativo'.");
                    txt_equipamentoId.clear(); 
                    carregarEquipamentosEmManutencao(); // Atualiza a tabela
                } else {
                    showAlert(AlertType.ERROR, "Erro", "Nao foi possivel finalizar a manutencao do equipamento ID " + idEquipamento + ". Tente novamente.");
                }
            } else {
                System.out.println("DEBUG: Confirmacao de finalizacao cancelada pelo usuario."); // DEPURACAO
            }
        } catch (NumberFormatException e) {
            System.err.println("ERRO: Entrada invalida para ID: " + idText + " - " + e.getMessage()); // DEPURACAO
            showAlert(AlertType.WARNING, "Entrada Invalida", "Por favor, digite um ID de equipamento valido (somente numeros).");
            txt_equipamentoId.clear();
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao finalizar manutencao: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao acessar o banco de dados. Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao finalizar manutencao: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar finalizar. Tente novamente mais tarde.");
        }
    }

    /**
     * Carrega os equipamentos com status "Em Manutencao" (id_status = 1) na TableView.
     */
    private void carregarEquipamentosEmManutencao() {
        try {
            // ID 1 e o status "Em Manutencao"
            List<Equipamento> equipamentos = equipamentoDAO.listarEquipamentosPorStatus(1); 
            ObservableList<Equipamento> observableEquipamentos = FXCollections.observableArrayList(equipamentos);
            table_manutencao.setItems(observableEquipamentos);
            System.out.println("DEBUG: Equipamentos em Manutencao carregados na tabela: " + equipamentos.size()); // DEPURACAO
        } catch (SQLException e) {
            System.err.println("Erro ao carregar equipamentos em manutencao do banco de dados: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar os equipamentos em manutencao do banco de dados.");
        }
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
        System.out.println("Navegando de volta para o Menu Principal a partir de Equipamentos em Manutencao.");
        try {
            App.setRoot("view/MenuPrincipal"); // Define o MenuPrincipal.fxml como a nova tela 
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }
}
