package containers;

import agents.MyAgent;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.TimerDispatcher;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MyAgentContainer extends Application {
    private MyAgent myAgent;
    private ObservableList<String> list = FXCollections.observableArrayList();

    public void setMyAgent(MyAgent myAgent) {
        this.myAgent = myAgent;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void startAgent(String name, String className, Object[] args) throws ControllerException {
        Runtime runtime= Runtime.instance();
        ProfileImpl profileImpl=new ProfileImpl();
        profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
        AgentController agentController=agentContainer.createNewAgent(name,className,args);
        agentController.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startAgent("Agent1","agents.MyAgent",new Object[]{this});

        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        Label label = new Label("Agent message: ");
        TextField textField = new TextField();
        textField.setPromptText("Enter your message");
        Button button = new Button("Send");
        hBox.getChildren().addAll(label, textField, button);
        borderPane.setTop(hBox);

        ListView<String> listView = new ListView<>(list);
        borderPane.setCenter(listView);

        primaryStage.setScene(new Scene(borderPane, 600, 450));
        primaryStage.setTitle("Agent Container");
        primaryStage.show();

        button.setOnAction(event -> {
            String message = textField.getText();
            GuiEvent guiEvent = new GuiEvent(this, 1);
            guiEvent.addParameter(message);
            myAgent.onGuiEvent(guiEvent);
            list.add("My Try : "+message);
        });
    }

    public ObservableList<String> getList() {
        return list;
    }
}
