package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.EquipamentoDAO;
import br.com.fatec.model.Equipamento;
import br.com.fatec.dao.CategoriaEquipamentoDAO;
import br.com.fatec.model.CategoriaEquipamento;
import br.com.fatec.dao.StatusEquipamentoDAO;
import br.com.fatec.model.StatusEquipamento;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CadastroDeEquipamentosController implements Initializable {

    @FXML
    private TextField lbl_equipamento;
    @FXML
    private ComboBox<CategoriaEquipamento> cmb_categoria;
    @FXML
    private TextField lbl_Nserie;
    @FXML
    private DatePicker date_aquisicao;
    @FXML
    private TextArea lblA_descricao;

    @FXML
    private Button btn_incluir;
    @FXML
    private Button btn_consultar;
    @FXML
    private Button btn_alterar;
    @FXML
    private Button btn_excluir;
    @FXML
    private Button btn_sair;

    private EquipamentoDAO equipamentoDAO;
    private CategoriaEquipamentoDAO categoriaEquipamentoDAO;
    private StatusEquipamentoDAO statusEquipamentoDAO;

    private Equipamento equipamentoCarregado;
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipamentoDAO = new EquipamentoDAO();
        categoriaEquipamentoDAO = new CategoriaEquipamentoDAO();
        statusEquipamentoDAO = new StatusEquipamentoDAO();

        carregarCategoriasNoComboBox();
        equipamentoCarregado = null;
    }    

    /**
     * Carrega as categorias de equipamentos do banco de dados e as exibe no ComboBox.
     */
    private void carregarCategoriasNoComboBox() {
        try {
            List<CategoriaEquipamento> categorias = categoriaEquipamentoDAO.listarCategorias();
            ObservableList<CategoriaEquipamento> observableCategorias = FXCollections.observableArrayList(categorias);
            cmb_categoria.setItems(observableCategorias);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar categorias do banco de dados: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar as categorias de equipamentos do banco de dados.");
        }
    }

    @FXML
    private void btn_incluir_click(ActionEvent event) {
        String nomeEquipamento = lbl_equipamento.getText().trim();
        CategoriaEquipamento categoriaSelecionada = cmb_categoria.getValue();
        String numeroDeSerie = lbl_Nserie.getText().trim();
        LocalDate dataAquisicao = date_aquisicao.getValue();
        String descricao = lblA_descricao.getText().trim();

        if (nomeEquipamento.isEmpty() ||
            categoriaSelecionada == null ||
            numeroDeSerie.isEmpty() ||
            dataAquisicao == null ||
            descricao.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos Obrigatorios", "Por favor, preencha todos os campos.");
            return;
        }

        if (!numeroDeSerie.matches("^[a-zA-Z0-9-]+$")) {
            showAlert(AlertType.WARNING, "Numero de Serie Invalido", "O numero de serie deve conter apenas letras, numeros e hifens.");
            return;
        }

        int idStatusAtivo;
        try {
            // CORRIGIDO: Buscando "Ativo" sem acento
            idStatusAtivo = statusEquipamentoDAO.getIdStatusByNome("Ativo");
            if (idStatusAtivo == 0) {
                showAlert(AlertType.ERROR, "Erro de Configuracao", "Status 'Ativo' nao encontrado no banco de dados. Verifique a tabela StatusEquipamento e seus dados.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ID do status 'Ativo': " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao buscar o status padrao. Detalhes: " + e.getMessage());
            return;
        }

        Equipamento novoEquipamento = new Equipamento();
        novoEquipamento.setNome(nomeEquipamento);
        novoEquipamento.setCategoria(categoriaSelecionada.getNomeCategoria());
        novoEquipamento.setIdStatus(idStatusAtivo);
        novoEquipamento.setNumeroDeSerie(numeroDeSerie);
        novoEquipamento.setDataAquisicao(dataAquisicao);
        novoEquipamento.setDescricao(descricao);
        novoEquipamento.setIdResponsavel(null);

        try {
            if (equipamentoDAO.numeroDeSerieExiste(numeroDeSerie)) {
                showAlert(AlertType.WARNING, "Numero de Serie Existente", "Um equipamento com este numero de serie ja esta cadastrado.");
                return;
            }

            if (equipamentoDAO.adicionarEquipamento(novoEquipamento)) {
                showAlert(AlertType.INFORMATION, "Cadastro Realizado", "Equipamento cadastrado com sucesso!");
                clearForm();
            } else {
                showAlert(AlertType.ERROR, "Erro no Cadastro", "Nao foi possivel cadastrar o equipamento. Tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao cadastrar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao acessar o banco de dados. Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar cadastrar. Tente novamente mais tarde.");
        }
    }

    @FXML
    private void btn_consultar_click(ActionEvent event) {
        String numeroDeSerie = lbl_Nserie.getText().trim();

        if (numeroDeSerie.isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Obrigatorio", "Por favor, digite o Numero de Serie para consultar.");
            return;
        }

        try {
            equipamentoCarregado = equipamentoDAO.buscarEquipamentoPorNumeroSerie(numeroDeSerie);

            if (equipamentoCarregado != null) {
                lbl_equipamento.setText(equipamentoCarregado.getNome());
                
                String categoriaDoEquipamento = equipamentoCarregado.getCategoria();
                if (categoriaDoEquipamento != null) {
                    CategoriaEquipamento categoriaParaSelecionar = null;
                    for (CategoriaEquipamento cat : cmb_categoria.getItems()) {
                        if (cat.getNomeCategoria().equals(categoriaDoEquipamento)) {
                            categoriaParaSelecionar = cat;
                            break;
                        }
                    }
                    cmb_categoria.getSelectionModel().select(categoriaParaSelecionar);
                } else {
                    System.err.println("Aviso: Categoria do equipamento carregado e nula. Nao foi possivel selecionar no ComboBox.");
                    cmb_categoria.getSelectionModel().clearSelection();
                }

                date_aquisicao.setValue(equipamentoCarregado.getDataAquisicao());
                lblA_descricao.setText(equipamentoCarregado.getDescricao());
                
                showAlert(AlertType.INFORMATION, "Consulta Realizada", "Equipamento encontrado e carregado.");
            } else {
                showAlert(AlertType.INFORMATION, "Nao Encontrado", "Nenhum equipamento encontrado com o Numero de Serie: " + numeroDeSerie);
                clearForm();
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao consultar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao consultar o equipamento. Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar consultar. Tente novamente mais tarde.");
        }
    }

    @FXML
    private void btn_alterar_click(ActionEvent event) {
        if (equipamentoCarregado == null) {
            showAlert(AlertType.WARNING, "Nenhum Equipamento Carregado", "Consulte um equipamento primeiro para poder alteralo.");
            return;
        }

        String novoNomeEquipamento = lbl_equipamento.getText().trim();
        CategoriaEquipamento novaCategoriaSelecionada = cmb_categoria.getValue();
        String novoNumeroDeSerie = lbl_Nserie.getText().trim();
        LocalDate novaDataAquisicao = date_aquisicao.getValue();
        String novaDescricao = lblA_descricao.getText().trim();

        if (novoNomeEquipamento.isEmpty() ||
            novaCategoriaSelecionada == null ||
            novoNumeroDeSerie.isEmpty() ||
            novaDataAquisicao == null ||
            novaDescricao.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos Obrigatorios", "Por favor, preencha todos os campos para alterar.");
            return;
        }
        
        if (!novoNumeroDeSerie.matches("^[a-zA-Z0-9-]+$")) {
            showAlert(AlertType.WARNING, "Numero de Serie Invalido", "O numero de serie deve conter apenas letras, numeros e hifens.");
            return;
        }

        equipamentoCarregado.setNome(novoNomeEquipamento);
        equipamentoCarregado.setCategoria(novaCategoriaSelecionada.getNomeCategoria());
        equipamentoCarregado.setDataAquisicao(novaDataAquisicao);
        equipamentoCarregado.setDescricao(novaDescricao);

        try {
            if (!equipamentoCarregado.getNumeroDeSerie().equals(novoNumeroDeSerie) &&
                equipamentoDAO.numeroDeSerieExiste(novoNumeroDeSerie)) {
                showAlert(AlertType.WARNING, "Numero de Serie Duplicado", "O novo numero de serie ja esta em uso por outro equipamento.");
                return;
            }
            equipamentoCarregado.setNumeroDeSerie(novoNumeroDeSerie);


            if (equipamentoDAO.atualizarEquipamento(equipamentoCarregado)) {
                showAlert(AlertType.INFORMATION, "Alteracao Realizada", "Equipamento alterado com sucesso!");
                clearForm();
                equipamentoCarregado = null;
            } else {
                showAlert(AlertType.ERROR, "Erro na Alteracao", "Nao foi possivel alterar o equipamento. Tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao alterar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao alterar o equipamento. Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao alterar equipamento: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar alterar. Tente novamente mais tarde.");
        }
    }


    @FXML
    private void btn_excluir_click(ActionEvent event) {
        if (equipamentoCarregado == null) {
            showAlert(AlertType.WARNING, "Nenhum Equipamento Carregado", "Consulte um equipamento primeiro para poder exclui-lo.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusao");
        alert.setHeaderText("Voce tem certeza que deseja excluir este equipamento?");
        alert.setContentText("Equipamento: " + equipamentoCarregado.getNome() + "\nSerie: " + equipamentoCarregado.getNumeroDeSerie());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (equipamentoDAO.excluirEquipamento(equipamentoCarregado.getNumeroDeSerie())) {
                    showAlert(AlertType.INFORMATION, "Exclusao Realizada", "Equipamento excluido com sucesso!");
                    clearForm();
                    equipamentoCarregado = null;
                } else {
                    showAlert(AlertType.ERROR, "Erro na Exclusao", "Nao foi possivel excluir o equipamento. Tente novamente.");
                }
            } catch (SQLException e) {
                System.err.println("Erro de SQL ao excluir equipamento: " + e.getMessage());
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erro no Banco de Dados", "Ocorreu um erro ao excluir o equipamento. Detalhes: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Erro inesperado ao excluir equipamento: " + e.getMessage());
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erro Interno", "Ocorreu um erro inesperado ao tentar excluir. Tente novamente mais tarde.");
            }
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    /**
     * Limpa todos os campos do formulario de cadastro de equipamentos.
     */
    private void clearForm() {
        lbl_equipamento.clear();
        cmb_categoria.getSelectionModel().clearSelection();
        lbl_Nserie.clear();
        date_aquisicao.setValue(null);
        lblA_descricao.clear();
        equipamentoCarregado = null;
    }

    @FXML
    private void cmb_categoria_change(ActionEvent event) {
    }

    @FXML
    private void date_aquisicao_click(ActionEvent event) {
    }

    @FXML
    private void btn_sair_click(ActionEvent event) {
        System.out.println("Navegando de volta para o Menu Principal a partir do Cadastro de Equipamentos.");
        try {
            App.setRoot("view/MenuPrincipal");
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
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
        System.out.println("Navegando de volta para o Menu Principal a partir do Cadastro de Equipamentos.");
        try {
            App.setRoot("view/MenuPrincipal");
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }
}
