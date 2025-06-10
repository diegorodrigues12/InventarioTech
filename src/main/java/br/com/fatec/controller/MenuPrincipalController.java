package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException; 
import java.net.URL;
import java.util.Optional; 
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert; 
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private Button btn_sair; 
    @FXML
    private Button btn_manutencao;
    @FXML
    private Button btn_equipamentos; 
    @FXML
    private Button btn_cadEqui; 
    @FXML
    private Button btn_consulta;
    @FXML
    private Button btn_revisar;
    @FXML
    private ImageView img_sair;
    @FXML
    private ImageView img_manutencao;
    @FXML
    private ImageView img_equipamentos;
    @FXML
    private ImageView img_cadEqui;
    @FXML
    private ImageView img_revisar;
    @FXML
    private ImageView img_consulta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void btn_sair_click(ActionEvent event) {
        // Cria um alerta de confirmação para deslogar
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Deslogar"); // Título do alerta
        alert.setHeaderText("Você está prestes a deslogar."); // Cabeçalho do alerta
        alert.setContentText("Você realmente deseja deslogar e retornar ao Login?"); // Conteúdo do alerta

        Optional<ButtonType> result = alert.showAndWait();

        // Se o usuário clicou em OK (confirmou deslogar), então procede com a navegação para LoginNew
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                App.setRoot("view/LoginNew"); 
                System.out.println("Deslogando e navegando para a tela de Login (LoginNew).");
            } catch (IOException e) {
                // Em caso de erro ao carregar o FXML, imprime o erro no console
                System.err.println("Erro ao carregar a tela de Login (LoginNew): " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de Login.");
            }
        } else {
            System.out.println("Deslogar cancelado. Permanecendo no Menu Principal.");
        }
    }

    @FXML
    private void btn_manutencao_click(ActionEvent event) {
        carregarTela("view/Manutencao", "Manutenção de Equipamentos");
    }

    @FXML
    private void btn_equipamentos_click(ActionEvent event) {
        carregarTela("view/EquipamentosEmManutencao", "Listagem de Equipamentos");
    }

    @FXML
    private void btn_cadEqui_click(ActionEvent event) {
        carregarTela("view/CadastroDeEquipamentos", "Cadastro de Equipamentos");
    }

    @FXML
    private void btn_consulta_click(ActionEvent event) {
        carregarTela("view/ConsultaAvanca", "Consulta Avançada");
    }

    @FXML
    private void btn_revisar_click(ActionEvent event) {
        carregarTela("view/EquipamentosEmRevisao", "Revisão de Dados");
    }
    

    private void carregarTela(String fxmlPath, String title) {
        try {
            App.setRoot(fxmlPath); // Usa o método setRoot da classe App para trocar a tela
            System.out.println("Navegando para a tela: " + fxmlPath);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela: " + title + ". Detalhes: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void img_sair_click(MouseEvent event) {
        // Cria um alerta de confirmação para deslogar
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Deslogar"); // Título do alerta
        alert.setHeaderText("Você está prestes a deslogar."); // Cabeçalho do alerta
        alert.setContentText("Você realmente deseja deslogar e retornar ao Login?"); // Conteúdo do alerta

        // Mostra o alerta e espera a resposta do usuário
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                App.setRoot("view/LoginNew"); 
                System.out.println("Deslogando e navegando para a tela de Login (LoginNew).");
            } catch (IOException e) {
                // Em caso de erro ao carregar o FXML, imprime o erro no console
                System.err.println("Erro ao carregar a tela de Login (LoginNew): " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de Login.");
            }
        } else {
            // Se o usuário clicou em Cancelar ou fechou o diálogo, a ação de deslogar é cancelada.
            System.out.println("Deslogar cancelado. Permanecendo no Menu Principal.");
        }
    }

    @FXML
    private void img_manutencao_click(MouseEvent event) {
        carregarTela("view/Manutencao", "Manutenção de Equipamentos");
    }

    @FXML
    private void img_equipamentos_click(MouseEvent event) {
        carregarTela("view/EquipamentosEmManutencao", "Listagem de Equipamentos");
    }

    @FXML
    private void img_cadEqui_click(MouseEvent event) {
        carregarTela("view/CadastroDeEquipamentos", "Cadastro de Equipamentos");
    }

    @FXML
    private void img_revisar_click(MouseEvent event) {
        carregarTela("view/EquipamentosEmRevisao", "Revisão de Dados");
    }

    @FXML
    private void img_consulta_click(MouseEvent event) {
        carregarTela("view/ConsultaAvanca", "Consulta Avançada");
    }
}
