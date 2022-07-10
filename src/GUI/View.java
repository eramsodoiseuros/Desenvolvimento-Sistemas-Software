package GUI;

import BL.Gestor;
import BL.Requisicao;
import PL.Controlador;
import PL.ControladorSessoes;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class View implements GUI {

    private Controlador c;
    private TextField usertxt, txt;
    private PasswordField passwordtxt;
    private ListView<String> listView;

    public View(){
        listView = new ListView<>();
        listView.getItems().addAll("a");
        c = new ControladorSessoes();
    }

    public static void make_window(String title, Scene s){
        Stage w = new Stage();
        w.setTitle(title);
        w.setScene(s);
        w.show();
    }

    public static void alert(String titulo, String mensagem){
        Stage w = new Stage();
        w.initModality(Modality.APPLICATION_MODAL);
        w.setTitle(titulo);
        w.setMinWidth(300);

        Label label = new Label();
        label.setText(mensagem);
        Button closeButton = new Button("Fechar.");
        closeButton.setOnAction(e -> w.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        w.setScene(scene);
        w.showAndWait();
    }

    public Scene menu() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        listView = new ListView<>();
        listView.getItems().addAll(
                "Registar Gestor", "Eliminar Gestor", "Login de Gestor", "Painel de Robots",
                "Requisições Feitas", "Entregas Feitas", "Requisitar Palete",
                "Requisitar Entrega", "Correr X"
        );

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button b1 = new Button("Escolher.");
        b1.setOnAction(e -> escolher_menu());

        Button b2 = new Button("Sair.");
        b2.setOnAction(e -> {
            Platform.exit();
        });

        Button b3 = new Button("Atualizar.");
        b3.setOnAction(e -> {
            c = new ControladorSessoes();
            c.end_scene(e);
            make_window("Menu Principal", menu());
        });

        layout.getChildren().addAll(listView,b1,b2,b3);
        return new Scene(layout, 400, 400);
    }

    private void escolher_menu(){
        String s = String.valueOf(listView.getSelectionModel().getSelectedItems());

        if(s.equals("[Registar Gestor]")){
            make_window("Resgistar Gestor", registar_gestor());
        }

        if(s.equals("[Eliminar Gestor]")){
            make_window("Eliminar Gestor", eliminar_gestor());
        }

        if(s.equals("[Login de Gestor]")){
            make_window("Login de Gestor", login_gestor());
        }

        if(s.equals("[Painel de Robots]")){
            make_window("Painel dos Robots", painel_pedido(c.lista_robots()));
        }

        if(s.equals("[Entregas Feitas]")){
            painel_EF();
        }

        if(s.equals("[Requisições Feitas]")){
            painel_RF();
        }

        if(s.equals("[Requisitar Palete]")){
            make_window("Requisitar Palete", criar_req());
        }

        if(s.equals("[Requisitar Entrega]")){
            make_window("Requisitar Entrega", criar_ent());
        }

        if(s.equals("[Correr X]")){
            make_window("Formulário de Pedidos", correr_X());
        }
    }

    private Scene painel_gestor(String codID) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        listView = new ListView<>();
        listView.getItems().addAll(
                "Consultar Inventário", "Aceitar Pedidos",
                "Consultar Robots disponiveis", "Requisições Feitas", "Entregas Feitas",
                "Consultar Listagem de Localizações", "Consultar Mapa"

        );

        Button b1 = new Button("Escolher.");
        b1.setOnAction(e -> gestor_menu());

        Button b2 = new Button("Terminar Sessão.");
        b2.setOnAction(e -> {
            c.logOutGestor(codID);
            c.end_scene(e);
        });

        Button b3 = new Button("Atualizar.");
        b3.setOnAction(e -> {
            c.end_scene(e);
            make_window("Menu do Gestor", painel_gestor(codID));
        });

        layout.getChildren().addAll(listView,b1,b2,b3);
        return new Scene(layout, 400, 400);
    }

    private void gestor_menu() {
        String s = String.valueOf(listView.getSelectionModel().getSelectedItems());
        if(s.equals("[Consultar Inventário]")){
            make_window("Inventário Disponivel", painel_pedido(c.inventario()));
        }

        if(s.equals("[Consultar Listagem de Localizações]")){
            make_window("Listagem de Localizações", painel_pedido(c.listagem()));
        }

        if(s.equals("[Aceitar Pedidos]")){
            make_window("Pedidos não processados", menu_pedidos(c.lista_requisicoes(), c.lista_entregas(), c.parking()));
        }

        if(s.equals("[Consultar Robots disponiveis]")){
            make_window("Lista de Robots disponiveis", painel_pedido(c.robots()));
        }

        if(s.equals("[Entregas Feitas]")){
            painel_EF();
        }

        if(s.equals("[Requisições Feitas]")){
            painel_RF();
        }
        if(s.equals("[Consultar Mapa]")){
            c.map();
        }
    }

    private Scene registar_gestor() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        txt = new TextField();
        Label lblNome = new Label("Nome Completo");

        usertxt = new TextField();
        Label lblUser = new Label("Username");

        passwordtxt = new PasswordField();
        Label lblPassword = new Label("Password");

        Button b = new Button("Registar.");
        b.setOnAction(e -> {
            String nome = txt.getText();
            String user = usertxt.getText();
            String pwd = passwordtxt.getText();

            if(user.equals("")) alert("Email NULL", "Precisa de inserir um email para se registar.");
            if(pwd.equals("")) alert("Password NULL", "Precisa de inserir uma palavra-passe para se registar.");
            if(nome.equals("")) alert("Nome NULL", "Precisa de inserir um nome para se registar.");
            else {
                c.validaRegisto(nome, user, pwd);
                c.end_scene(e);
            }
        });

        layout.getChildren().addAll(lblNome, txt, lblUser, usertxt, lblPassword, passwordtxt, b);
        return new Scene(layout, 500, 400);
    }

    private Scene eliminar_gestor() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        usertxt = new TextField();
        Label lblUser = new Label("Código ID");

        Button b = new Button("Eliminar.");
        b.setOnAction(e -> {
            String user = usertxt.getText();

            if(user.equals("")) alert("ERRO", "Precisa de inserir um Código ID, para apagar o Gestor.");
            else {
                c.delete(user);
                c.end_scene(e);
            }
        });

        layout.getChildren().addAll(lblUser, usertxt, b);
        return new Scene(layout, 500, 400);
    }

    private Scene login_gestor() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        usertxt = new TextField();
        Label lblUser = new Label("Username");

        passwordtxt = new PasswordField();
        Label lblPassword = new Label("Password");

        Button b = new Button("LogIn.");
        b.setOnAction(e -> {
            String user = usertxt.getText();
            String pwd = passwordtxt.getText();

            if(user.equals("")) alert("ERRO", "Precisa de inserir um Código ID para se conectar.");
            if(pwd.equals("")) alert("ERRO", "Precisa de inserir uma palavra-passe para se conectar.");
            else {
                logInGestor(user, pwd);
                c.end_scene(e);
            }
        });

        layout.getChildren().addAll(lblUser, usertxt, lblPassword, passwordtxt, b);
        return new Scene(layout, 500, 400);
    }

    private Scene painel_pedido(List<String> lista) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        listView= new ListView<>();
        listView.getItems().addAll(lista);

        layout.getChildren().addAll(listView);
        return new Scene(layout, 600, 500);
    }

    private Scene correr_X() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        usertxt = new TextField();
        Label lbl1 = new Label("Número de Entregas: ");

        txt = new TextField();
        Label lbl2 = new Label("Número de Requisições: ");

        Button b = new Button("Processar.");
        b.setOnAction(e -> {
            c.end_scene(e);
            String ent = usertxt.getText();
            String req = txt.getText();
            try{
                int e1 = Integer.parseInt(ent);
                int r1 = Integer.parseInt(req);

                if(e1 >= 0 && r1 >= 0){
                    c.both(e1,r1);
                } else alert("ERRO", "Erro ao inserir números inteiros positivos.");
            } catch (NumberFormatException exception){
                alert("ERRO", "Precisa de inserir um número positivo.");
            }
        });

        layout.getChildren().addAll(lbl1, usertxt, lbl2, txt, b);
        return new Scene(layout, 500, 500);
    }

    private Scene menu_pedidos(List<String> req, List<String> ent, int p) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label lblUser = new Label("Parking disponivel: " + p);

        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(req);
        cb.setPromptText("Requisições a necessitar de aprovação: ");
        cb.setOnAction(e -> {
            c.end_scene(e);
            String[] split = cb.getValue().split(" ", 8);
            c.addRA(split[1]);
        });

        ComboBox<String> cb2 = new ComboBox<>();
        cb2.getItems().addAll(ent);
        cb2.setPromptText("Entregas a necessitar de aprovação: ");
        cb2.setOnAction(e -> {
            c.end_scene(e);
            String[] split = cb2.getValue().split(" ", 8);
            c.addEA(split[1]);
        });

        layout.getChildren().addAll(lblUser,cb, cb2);
        return new Scene(layout, 400, 300);
    }

    private Scene criar_ent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        txt = new TextField();
        Label lblNome = new Label("Conteudo");

        Button b = new Button("Criar Entrega.");
        b.setOnAction(e -> {
            String cod = txt.getText();

            if(cod.equals("")) alert("ERRO", "Precisa de inserir o conteudo da Entrega.");
            else {
                c.addE(cod);
                c.end_scene(e);
            }
        });

        layout.getChildren().addAll(lblNome, txt, b);
        return new Scene(layout, 500, 400);
    }
    private Scene criar_req() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        txt = new TextField();
        Label lblNome = new Label("Conteudo");

        Button b = new Button("Criar Requisição.");
        b.setOnAction(e -> {
            String cod = txt.getText();

            if(cod.equals("")) alert("ERRO", "Precisa de inserir o conteudo da Requisição.");
            else {
                c.addR(cod);
                c.end_scene(e);
            }
        });

        layout.getChildren().addAll(lblNome, txt, b);
        return new Scene(layout, 500, 400);
    }

    private void painel_EF(){
        make_window("Painel das Entregas Feitas", painel_pedido(c.getEntFeitas()));
    }
    private void painel_RF(){
        make_window("Painel das Requisições Feitas", painel_pedido(c.getReqFeitas()));
    }
    private void logInGestor(String codID, String pwd){
        if(c.iniciaSessao(codID, pwd)){
            View.make_window("Menu do Gestor", painel_gestor(codID));
        } else View.alert("ERRO", "Falha ao iniciar a sessão, verifique os seus dados.");
    }
}
