package hermes.dev.transasia.ru.fireapp.patterns.mediator;

import android.util.Log;

import static hermes.dev.transasia.ru.fireapp.patterns.mediator.Server.MEDIATOR;

public class Client2 extends Client {


    public Client2(Server server) {
        super(server);
    }

    @Override
    public void notify(String msg) {
        Log.d(MEDIATOR, "notify: client 2 " + msg);
    }
}
