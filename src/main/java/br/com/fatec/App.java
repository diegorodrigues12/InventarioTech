package br.com.fatec;

import br.com.fatec.persistencia.Banco; 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException; 

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/LoginNew")); 
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL fxmlUrl = App.class.getResource(fxml + ".fxml"); 

        if (fxmlUrl == null) {
            throw new IOException("Recurso FXML não encontrado: " + fxml + ".fxml" + 
                                  " (Caminho completo tentado: " + App.class.getPackage().getName().replace('.', '/') + "/" + fxml + ".fxml" + ")");
        }
        System.out.println("Tentando carregar FXML de: " + fxmlUrl.toExternalForm());

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        
        /* O código de conexão com o banco diretamente no main está comentado,
         * pois a conexão é gerenciada pelo FuncionarioDAO quando necessário.
         * Descomente apenas se precisar testar a conexão diretamente ou inserir dados de teste via console.
         */
        /*
        System.out.println("Conectando...");
        try {
            Banco.conectar();
            System.out.println("Conectado..");
            Banco.desconectar();
            System.out.println("Fechado com sucesso!!!");
            
            // Exemplo de como era o código para inserir um Proprietario, que não é mais relevante aqui
            // Proprietario p = new Proprietario(12, "Paloma Duarte");
            // ProprietarioDAO dao = new ProprietarioDAO();
            // dao.inserir(p);
        }
        catch (SQLException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        */
        
    }

}
