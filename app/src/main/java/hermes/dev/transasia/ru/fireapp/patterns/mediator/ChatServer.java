package hermes.dev.transasia.ru.fireapp.patterns.mediator;

public class ChatServer implements Server {

    public void setClient1(Client1 client1) {
        this.client1 = client1;
    }

    public void setClient2(Client2 client2) {
        this.client2 = client2;
    }

    private Client1 client1;
    private Client2 client2;

    @Override
    public void sendMessage(String msg, Client client) {
        if (client.equals(client1)){
            client2.notify(msg);
        } else {
            client1.notify(msg);
        }
    }
}
