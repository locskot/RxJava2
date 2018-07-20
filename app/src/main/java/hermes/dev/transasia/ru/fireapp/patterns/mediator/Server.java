package hermes.dev.transasia.ru.fireapp.patterns.mediator;

public interface Server {

    String MEDIATOR = "MEDIATOR";

    void sendMessage(String msg, Client client);
}
