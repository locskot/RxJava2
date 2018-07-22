package hermes.dev.transasia.ru.fireapp;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hermes.dev.transasia.ru.fireapp.patterns.mediator.ChatServer;
import hermes.dev.transasia.ru.fireapp.patterns.mediator.Client1;
import hermes.dev.transasia.ru.fireapp.patterns.mediator.Client2;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class PatternActivity extends AppCompatActivity {

    String TAG = "working_home";

    CompositeDisposable d = new CompositeDisposable();

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


    @Override
    protected void onStart() {
        super.onStart();

        Observable<Marker> all = getAllPoints();

        d.add(all.filter(marker -> {
                    if (getVisitedList().size() != 0) {
                        try {
                            Point p = (Point) marker;
                            for (int i = 0; i < getVisitedList().size(); i++) {
                                Point p2 = (Point) getVisitedList().get(i);
                                if (p.getId().equals(p2.getId())) {
                                    return false;
                                }
                            }
                            return true;
                        } catch (ClassCastException e) {
                            return true;
                        }
                    }
                    return true;
                })
                .map(marker -> {
                    if (marker instanceof Point) return new Pair<>(marker, "not_visited");
                    else return new Pair<>(marker, "header");
                })
                .concatWith(getVisited())
                .subscribe(result -> Log.d(TAG, "onStart: " + result),
                        error -> Log.d(TAG, "onStart: " + error),
                        () -> Log.d(TAG, "onStart: completed")));


    }


    public Observable<Marker> getAllPoints() {
        List<Marker> list = new ArrayList<>();
        list.add(new Point(1L));
        list.add(new Point(2L));
        list.add(new Point(3L));
        list.add(new Point(4L));
        list.add(new Point(5L));
        list.add(new Point(6L));
        list.add(new Point(7L));
        list.add(new Point(8L));
        list.add(new Point(9L));
        list.add(new Point(10L));
        if (list.size() == 0) return Observable.empty();
        else return Observable.fromIterable(list).startWith(new Header(100000L));
    }

    public Observable<Pair<Marker, String>> getVisited() {
        List<Marker> list = getVisitedList();

        if (list.size() == 0) return Observable.empty();
        else return Observable
                .fromIterable(list)
                .startWith(new Header(999999999L))
                .map(marker -> {
                    if (marker instanceof Point) return new Pair<>(marker, "visited");
                    else return new Pair<>(marker, "second_header");
                });

    }

    public List<Marker> getVisitedList() {
        List<PatternActivity.Marker> list = new ArrayList<>();
        list.add(new Point(1L));
        list.add(new Point(2L));
        list.add(new Point(3L));
        list.add(new Point(4L));
        list.add(new Point(5L));
        return list;
    }


    public interface Marker {
    }

    public class Point implements Marker {
        Long id;

        public Point(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return getId().toString();
        }
    }

    public class Header implements Marker {
        Long id;

        public Header(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return getId().toString();
        }
    }
}
