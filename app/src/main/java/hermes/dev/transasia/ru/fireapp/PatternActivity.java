package hermes.dev.transasia.ru.fireapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hermes.dev.transasia.ru.fireapp.patterns.mediator.ChatServer;
import hermes.dev.transasia.ru.fireapp.patterns.mediator.Client1;
import hermes.dev.transasia.ru.fireapp.patterns.mediator.Client2;

public class PatternActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        ChatServer chatServer = new ChatServer();
        Client1 client1 = new Client1(chatServer);
        Client2 client2 = new Client2(chatServer);

        chatServer.setClient1(client1);
        chatServer.setClient2(client2);


        client1.send("1");
        client2.send("2");

    }
}
