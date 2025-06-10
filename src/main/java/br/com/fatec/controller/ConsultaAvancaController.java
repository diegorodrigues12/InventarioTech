package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.CategoriaEquipamentoDAO; 
import br.com.fatec.dao.EquipamentoDAO;
import br.com.fatec.dao.StatusEquipamentoDAO; 
import br.com.fatec.model.CategoriaEquipamento; 
import br.com.fatec.model.Equipamento;   
import br.com.fatec.model.StatusEquipamento; 

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
import javafx.scene.control.cell.PropertyValueFactory; 
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class ConsultaAvancaController implements Initializable {

    @FXML
    private Button btn_sair;
    @FXML
    private ComboBox<CategoriaEquipamento> cmb_categoria; 
    @FXML
    private ComboBox<Equipamento> cmb_equipamento; 
    @FXML
    private DatePicker date_de; 
    @FXML
    private DatePicker date_ate; 
    @FXML
    private TableView<Equipamento> table_consulta; 
    @FXML
    private TableColumn<Equipamento, String> column_equipamento; 
    @FXML
    private TableColumn<Equipamento, String> column_Nserie; 
    @FXML
    private TableColumn<Equipamento, String> column_categoria;
    @FXML
    private TableColumn<Equipamento, LocalDate> column_data; 
    @FXML
    private TableColumn<Equipamento, String> column_status; 
    @FXML
    private Button btn_consultar; 
    
    // Novo: Campo de texto para pesquisa por nome ou ID do equipamento
    private TextField txt_termoEquipamento;


    private CategoriaEquipamentoDAO categoriaEquipamentoDAO;
    private EquipamentoDAO equipamentoDAO;
    private StatusEquipamentoDAO statusEquipamentoDAO;
    @FXML
    private ImageView img_sair;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaEquipamentoDAO = new CategoriaEquipamentoDAO();
        equipamentoDAO = new EquipamentoDAO();
        statusEquipamentoDAO = new StatusEquipamentoDAO();

        // Configura as colunas da TableView para mapear as propriedades do objeto Equipamento
        column_equipamento.setCellValueFactory(new PropertyValueFactory<>("nome"));
        column_Nserie.setCellValueFactory(new PropertyValueFactory<>("numeroDeSerie"));
        column_categoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        column_data.setCellValueFactory(new PropertyValueFactory<>("dataAquisicao"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("nomeStatus")); 

        carregarCategoriasNoComboBox();
        carregarTodosEquipamentosNoComboBox();
        
        date_de.valueProperty().addListener((obs, oldDate, newDate) -> validarDatas());
        date_ate.valueProperty().addListener((obs, oldDate, newDate) -> validarDatas());

        realizarConsulta();
    }    

    @FXML
    private void btn_sair_click(ActionEvent event) {
        System.out.println("Navegando de volta para o Menu Principal a partir da Consulta Avancada.");
        try {
            App.setRoot("view/MenuPrincipal"); // Define o MenuPrincipal.fxml como a nova tela raiz
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do Menu Principal: " + e.getMessage());
            e.printStackTrace();
            // Exibe um alerta ao usuario em caso de falha na navegacao
            showAlert(AlertType.ERROR, "Erro de Navegacao", "Nao foi possivel retornar ao Menu Principal. Detalhes: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as categorias do banco de dados na ComboBox de categorias.
     */
    private void carregarCategoriasNoComboBox() {
        try {
            List<CategoriaEquipamento> categorias = categoriaEquipamentoDAO.listarCategorias();
            // Opcional: Adicionar uma opcao "Todas as Categorias"
            CategoriaEquipamento todasCategorias = new CategoriaEquipamento(0, "Todas as Categorias");
            categorias.add(0, todasCategorias); // Adiciona no inicio da lista
            ObservableList<CategoriaEquipamento> observableCategorias = FXCollections.observableArrayList(categorias);
            cmb_categoria.setItems(observableCategorias);
            cmb_categoria.getSelectionModel().selectFirst(); // Seleciona "Todas as Categorias" por padrao
        } catch (SQLException e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar as categorias.");
        }
    }

    /**
     * Carrega todos os equipamentos do banco de dados na ComboBox de equipamentos.
     */
    private void carregarTodosEquipamentosNoComboBox() {
        try {
            List<Equipamento> equipamentos = equipamentoDAO.listarTodosEquipamentosSemFiltroDeStatus();
            // Opcional: Adicionar uma opcao "Todos os Equipamentos"
            Equipamento todosEquipamentos = new Equipamento();
            todosEquipamentos.setIdEquipamento(0); // Um ID que nao vai colidir com os reais
            todosEquipamentos.setNome("Todos os Equipamentos");
            equipamentos.add(0, todosEquipamentos);
            ObservableList<Equipamento> observableEquipamentos = FXCollections.observableArrayList(equipamentos);
            cmb_equipamento.setItems(observableEquipamentos);
            cmb_equipamento.getSelectionModel().selectFirst(); // Seleciona "Todos os Equipamentos" por padrao
        } catch (SQLException e) {
            System.err.println("Erro ao carregar equipamentos: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Carregamento", "Nao foi possivel carregar os equipamentos.");
        }
    }

    @FXML
    private void btn_consultar_click(ActionEvent event) {
        realizarConsulta();
    }

    /**
     * Metodo que efetivamente realiza a consulta no banco de dados com base nos filtros.
     */
    private void realizarConsulta() {
        // Obter filtros selecionados
        Integer idCategoria = null;
        if (cmb_categoria.getValue() != null && cmb_categoria.getValue().getIdCategoria() != 0) {
            idCategoria = cmb_categoria.getValue().getIdCategoria();
        }

        String termoEquipamento = null;
        if (cmb_equipamento.getValue() != null && cmb_equipamento.getValue().getIdEquipamento() != 0) {
            termoEquipamento = String.valueOf(cmb_equipamento.getValue().getIdEquipamento()); // Busca por ID do equipamento selecionado na combo
        } else if (txt_termoEquipamento != null && !txt_termoEquipamento.getText().trim().isEmpty()) {
             termoEquipamento = txt_termoEquipamento.getText().trim(); // Se houver texto digitado, usa como termo de pesquisa
        }


        LocalDate dataDe = date_de.getValue();
        LocalDate dataAte = date_ate.getValue();

        if (dataDe != null && dataAte != null && dataDe.isAfter(dataAte)) {
            showAlert(AlertType.WARNING, "Erro de Data", "A 'Data De' não pode ser posterior à 'Data Até'. Por favor, corrija.");
            date_de.setValue(null);
            date_ate.setValue(null);
            table_consulta.setItems(FXCollections.observableArrayList()); // Limpa a tabela
            return;
        }

        try {
            List<Equipamento> resultados = equipamentoDAO.consultarEquipamentos(idCategoria, termoEquipamento, dataDe, dataAte);
            ObservableList<Equipamento> observableResultados = FXCollections.observableArrayList(resultados);
            table_consulta.setItems(observableResultados);
            if (resultados.isEmpty()) {
                showAlert(AlertType.INFORMATION, "Nenhum Resultado", "Nenhum equipamento encontrado com os critérios de busca selecionados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao realizar consulta de equipamentos: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Consulta", "Nao foi possivel realizar a consulta. Detalhes: " + e.getMessage());
        }
    }
    
    /**
     * Metodo para validar as datas selecionadas.
     * Exibe um alerta se a data "De" for posterior a data "Ate".
     */
    private void validarDatas() {
        LocalDate dataDe = date_de.getValue();
        LocalDate dataAte = date_ate.getValue();

        if (dataDe != null && dataAte != null && dataDe.isAfter(dataAte)) {
            showAlert(AlertType.WARNING, "Erro de Data", "A 'Data De' não pode ser posterior à 'Data Até'. Por favor, corrija.");
        }
    }

    @FXML
    private void date_de_click(ActionEvent event) {
    }

    @FXML
    private void date_ate_click(ActionEvent event) {
    }

    @FXML
    private void cmb_categoria_change(ActionEvent event) {
        realizarConsulta();
    }

    @FXML
    private void cmb_equipamento_change(ActionEvent event) {
        realizarConsulta();
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
        System.out.println("Navegando de volta para o Menu Principal a partir da Consulta Avancada.");
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
