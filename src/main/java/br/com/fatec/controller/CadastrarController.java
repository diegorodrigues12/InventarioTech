package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.model.Funcionario;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CadastrarController implements Initializable {

    @FXML
    private TextField lbl_nome;
    @FXML
    private TextField lbl_email;
    @FXML
    private PasswordField lbl_senha;
    @FXML
    private PasswordField lbl_senhaConfirma;
    @FXML
    private TextField lbl_matricula;
    @FXML
    private TextField lbl_funcao;
    @FXML
    private TextField lbl_telefone;
    
    @FXML
    private Button btn_cadastrar;
    @FXML
    private Button btn_voltar;

    private FuncionarioDAO funcionarioDAO;
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        funcionarioDAO = new FuncionarioDAO();
    }    
    
    @FXML
    private void btn_cadastrar_click(ActionEvent event) {
        String nome = lbl_nome.getText().trim();
        String email = lbl_email.getText().trim();
        String senha = lbl_senha.getText();
        String senhaConfirma = lbl_senhaConfirma.getText();
        String matricula = lbl_matricula.getText().trim();
        String funcao = lbl_funcao.getText().trim();
        String telefone = lbl_telefone.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty() || matricula.isEmpty() || funcao.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        if (!senha.equals(senhaConfirma)) {
            showAlert(AlertType.WARNING, "Senhas Não Coincidem", "A senha e a confirmação de senha não coincidem.");
            lbl_senha.clear();
            lbl_senhaConfirma.clear();
            lbl_senha.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            showAlert(AlertType.WARNING, "Senha Fraca", "A senha deve ter no mínimo 6 caracteres.");
            lbl_senha.clear();
            lbl_senhaConfirma.clear();
            lbl_senha.requestFocus();
            return;
        }

        if (!email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            showAlert(AlertType.WARNING, "Formato de E-mail Inválido", "Por favor, insira um e-mail válido.");
            return;
        }
        
        if (!telefone.isEmpty() && !telefone.matches("\\d{10,11}")) {
            showAlert(AlertType.WARNING, "Formato de Telefone Inválido", "Por favor, insira um telefone válido (apenas dígitos, 10 ou 11).");
            return;
        }

        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome(nome);
        novoFuncionario.setEmail(email);
        novoFuncionario.setMatricula(matricula);
        novoFuncionario.setFuncao(funcao);
        novoFuncionario.setTelefone(telefone.isEmpty() ? null : telefone);
        novoFuncionario.setDataCadastro(LocalDateTime.now());
        novoFuncionario.setSenhaHash(senha);
        
        try {
            if (funcionarioDAO.adicionarFuncionario(novoFuncionario)) {
                showAlert(AlertType.INFORMATION, "Cadastro Realizado", "Funcionário cadastrado com sucesso!");
                clearForm();
                btn_voltar_click(event);
            } else {
                showAlert(AlertType.ERROR, "Erro no Cadastro", "Não foi possível cadastrar o funcionário. Verifique se a matrícula ou e-mail já existem.");
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao acessar o banco de dados. Por favor, verifique a conexão e tente novamente. Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar cadastrar. Tente novamente mais tarde.");
        }
    }

    @FXML
    private void btn_voltar_click(ActionEvent event) {
        System.out.println("Navegando de volta para a tela de LoginNew.");
        try {
            App.setRoot("view/LoginNew");
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar a tela de LoginNew: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de Login. Detalhes: " + e.getMessage());
        }
        catch (Throwable t) {
            System.err.println("ERRO FATAL NA NAVEGAÇÃO DE VOLTA PARA O LOGIN: " + t.getMessage());
            t.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Crítico de Navegação", "Não foi possível retornar à tela de Login. Por favor, reinicie o aplicativo. Detalhes Técnicos: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }

    private void clearForm() {
        lbl_nome.clear();
        lbl_email.clear();
        lbl_senha.clear();
        lbl_senhaConfirma.clear();
        lbl_matricula.clear();
        lbl_funcao.clear();
        lbl_telefone.clear();
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
        System.out.println("Navegando de volta para a tela de LoginNew.");
        try {
            App.setRoot("view/LoginNew");
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar a tela de LoginNew: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de Login. Detalhes: " + e.getMessage());
        }
        catch (Throwable t) {
            System.err.println("ERRO FATAL NA NAVEGAÇÃO DE VOLTA PARA O LOGIN: " + t.getMessage());
            t.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Crítico de Navegação", "Não foi possível retornar à tela de Login. Por favor, reinicie o aplicativo. Detalhes Técnicos: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }
}
