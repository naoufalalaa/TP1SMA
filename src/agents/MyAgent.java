package agents;

import containers.MyAgentContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.application.Platform;

public class MyAgent extends GuiAgent {
    private transient MyAgentContainer myGui;

    @Override
    protected void setup() {
        myGui = (MyAgentContainer) getArguments()[0];
        myGui.setMyAgent(this);
        System.out.println("***** Initialisation de l'agent *********");
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage=receive();
                if(aclMessage!=null) {
                    Platform.runLater(() -> myGui.getList().add("Message reçu : "+aclMessage.getContent()));
                }
                else {
                    block();
                }
            }
        });
    }

    @Override
    protected void afterMove() {
        System.out.println("****** après migration ********");
    }

    @Override
    protected void beforeMove() {
        System.out.println("***** avant migration ******");
    }

    @Override
    protected void takeDown() {
        System.out.println("****** avant de mourir ");
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        if (guiEvent.getType() == 1) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
            aclMessage.addReceiver(new AID("Server", AID.ISLOCALNAME));
            aclMessage.setContent((String) guiEvent.getParameter(0));
            send(aclMessage);
        }
    }
}
