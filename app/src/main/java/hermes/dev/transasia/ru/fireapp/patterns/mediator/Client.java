package hermes.dev.transasia.ru.fireapp.patterns.mediator;

public abstract class Client {

    protected Server server;

    public Client(Server server) {
        this.server = server;
    }

    public void send(String msg){
        server.sendMessage(msg, this);
    }

    public abstract void notify(String msg);
}
