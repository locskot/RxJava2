package hermes.dev.transasia.ru.fireapp.patterns.mediator;

import android.util.Log;

import static hermes.dev.transasia.ru.fireapp.patterns.mediator.Server.MEDIATOR;

public class Client1 extends Client {


    public Client1(Server server) {
        super(server);
    }

    @Override
    public void notify(String msg) {
        Log.d(MEDIATOR, "notify: client1 " + msg);
    }
}
