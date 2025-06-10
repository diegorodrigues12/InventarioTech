package br.com.fatec.controller;

import br.com.fatec.App; 
import br.com.fatec.dao.FuncionarioDAO; 
import br.com.fatec.model.Funcionario;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class LoginNewController implements Initializable {

    @FXML
    private TextField lbl_usuario; 

    @FXML
    private PasswordField lbl_senha; 

    @FXML
    private TextField lbl_senha2;

    @FXML
    private Button btn_entrar;

    @FXML
    private Text txt_cadastro; // 

    @FXML
    private CheckBox chk_senha; // 

    private FuncionarioDAO funcionarioDAO; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            funcionarioDAO = new FuncionarioDAO();

            lbl_senha2.visibleProperty().bind(chk_senha.selectedProperty());
            lbl_senha.visibleProperty().bind(chk_senha.selectedProperty().not());
            lbl_senha2.managedProperty().bind(chk_senha.selectedProperty());
            lbl_senha.managedProperty().bind(chk_senha.selectedProperty().not());

            lbl_senha.textProperty().addListener((obs, oldText, newText) -> {
                lbl_senha2.setText(newText);
            });
            lbl_senha2.textProperty().addListener((obs, oldText, newText) -> {
                lbl_senha.setText(newText);
            });

        } catch (Throwable t) { 
            // Captura qualquer exceção durante a inicialização do controlador (ex: NullPointerException em FXML ids)
            System.err.println("ERRO FATAL DURANTE A INICIALIZAÇÃO DO LOGINNEWCONTROLLER: " + t.getMessage());
            t.printStackTrace(); 
            showAlert(Alert.AlertType.ERROR, "Erro de Inicialização", 
                      "Ocorreu um erro ao inicializar a tela de Login. Por favor, reinicie o aplicativo." +
                      "\nDetalhes Técnicos: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }

    //Teste de login
    private void adicionarFuncionarioDeTeste() {
        Funcionario funcTeste = new Funcionario();
        funcTeste.setNome("Usuário de Teste");
        funcTeste.setMatricula("12345");
        funcTeste.setEmail("teste@fatec.com");
        funcTeste.setSenhaHash("senha123"); 
        funcTeste.setFuncao("Tester");
        funcTeste.setTelefone("999999999");

        try {
            if (funcionarioDAO.adicionarFuncionario(funcTeste)) {
                System.out.println("Funcionário de teste 'teste@fatec.com' adicionado com sucesso!");
            } else {
                System.err.println("Falha ao adicionar funcionário de teste (pode já existir).");
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar adicionar funcionário de teste: " + e.getMessage());
        }
    }

    @FXML
    private void btn_entrar_click(ActionEvent event) {
        String usuarioDigitado = lbl_usuario.getText().trim(); 
        String senhaDigitada = chk_senha.isSelected() ? lbl_senha2.getText() : lbl_senha.getText();

        if (usuarioDigitado.isEmpty() || senhaDigitada.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos Vazios", "Por favor, preencha o email/matrícula e a senha.");
            return;
        }

        Funcionario funcionarioLogado = funcionarioDAO.autenticar(usuarioDigitado, senhaDigitada);

        if (funcionarioLogado != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Efetuado com Sucesso", "Bem-vindo(a), " + funcionarioLogado.getNome() + "!");

            try {
                App.setRoot("view/MenuPrincipal"); 
            } catch (IOException e) {
                System.err.println("Erro ao carregar a tela principal: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela principal. Detalhes: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro de Login", "Email/Matrícula ou Senha inválidos.");
        }
    }
    
    @FXML
    private void txt_cadastro_click(MouseEvent event) {
        try {
            App.setRoot("view/Cadastrar"); 
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de cadastro: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de cadastro. Detalhes: " + e.getMessage());
        }
    }

    @FXML
    private void chk_senha_click(ActionEvent event) {
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); 
        alert.setContentText(message);
        alert.showAndWait();
    }
}
