package hermes.dev.transasia.ru.fireapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private Disposable filterDisposable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static final String TAG = "RxJava";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupNavigationListener();

//        filter();
//        take(2);
//        take(1, 10);
//        takeLast(2);
//        skip(20, 100, 50);
//        skipLast(4, 2);
//        skipLast(4, 0);
//        takeWhile(0, 20);
//        skipWhile(0, 20);
//        takeUntil();
//        skipUntil();
//        distinct();
//        distinctWithLambda();
        distinctUntilChanged();

    }


    public void filter() {
        Observable<String> filter = getJustStringObservable();

        filterDisposable = filter
                .filter(s -> s.equals("KitKat"))
                .subscribe(s -> Log.d(TAG, "filter: " + s));
    }

    @SuppressLint("CheckResult")
    public void take(int quantity) {
        getJustStringObservable()
                .take(quantity)
                .subscribe(s -> Log.d(TAG, "take: " + s));
    }

    public void take(int interval, int period) {
        compositeDisposable.add(getIntervalLongObservable(interval)
                .take(period, TimeUnit.SECONDS)
                .filter(i -> i % 2 == 0)
                .subscribe(l -> Log.d(TAG, "take: " + l)));

        sleep(5000);
    }

    public void takeLast(int quantutyOfLastItems) {
        compositeDisposable.add(getJustStringObservable()
                .takeLast(2)
                .subscribe(s -> Log.d(TAG, "takeLast: " + s)));
    }

    public void skip(int quantityOfSkippedItems, int startFrom, int quantity) {
        Observable<Integer> skip = getRangeIntObservable(startFrom, quantity);

        compositeDisposable.add(skip
                .skip(quantityOfSkippedItems)
                .subscribe(integer -> Log.d(TAG, "skip: " + integer)));
    }

    public void skipLast(int quantityOfFirstTakenItems, int quantityOfLastSkippedItems) {
        compositeDisposable.add(getJustStringObservable()
                .take(quantityOfFirstTakenItems)
                .skipLast(quantityOfLastSkippedItems)
                .subscribe(s -> Log.d(TAG, "skipLast: " + s)));
        Log.d(TAG, " ");
    }

    public void takeWhile(int startFrom, int quantity) {
        Observable<Integer> source = getRangeIntObservable(startFrom, quantity);


        compositeDisposable.add(source
                .takeWhile(integer -> integer < 10)
                .subscribe(integer -> Log.d(TAG, "takeWhile onNext: " + integer),
                        Throwable::printStackTrace,
                        () -> Log.d(TAG, "takeWhile onComplete:" + "DONE")));

//       Длинная запись
//        source
//                .takeWhile(integer -> integer < 10)
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "takeWhile onNext LONG: " + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "takeWhile onComplete LONG: ");
//                    }
//                });
    }

    public void skipWhile(int startFrom, int quantity) {

        Observable<Integer> source = getRangeIntObservable(startFrom, quantity);

        source.skipWhile(item -> item < 10)
                .subscribe(item -> Log.d(TAG, "skipWhile: onNext " + item),
                        throwable -> throwable.printStackTrace(),
                        () -> Log.d(TAG, "skipWhile: Completed"));
    }

    @SuppressLint("CheckResult")
    public void takeUntil() {
        Observable<Integer> source = getRangeIntObservable(1, 100);

        source
                .map(integer -> integer + 2)
                .takeUntil(integer -> integer == 50)
                .subscribe(integer -> Log.d(TAG, "takeUntil: " + integer));

        // the same result
//        source.filter(integer -> (integer < 50)).subscribe(integer -> Log.d(TAG, "takeUntil: " + integer));
    }

    @SuppressLint("CheckResult")
    public void skipUntil() {
        Observable<String> source = getJustStringObservable();
        source
                .map(s -> {
                    Log.d(TAG, "apply: " + s);
                    return s + " " + s;
                })
                .skipUntil(getIntervalLongObservable(6))
                .subscribe(s -> Log.d(TAG, "skipUntil: " + s));
    }

    public void distinct(){

        compositeDisposable.add(getJustStringObservable()
                .distinct()
                .map(String::length)
                .subscribe(s -> Log.d(TAG, "distinct: " + s)));
    }

    public void distinctWithLambda(){

        compositeDisposable.add(getJustStringObservable()
//                .map(s -> {
//                    Log.d(TAG, "map: " + s);
//                    return s.equals("KitKat");
//                })
                .distinct(s -> s.toString().length())
                .subscribe(s -> Log.d(TAG, "distinct: " + s)));
    }

    public void distinctUntilChanged(){
        compositeDisposable.add(getJustStringObservable()
                .distinctUntilChanged()
                .subscribe(s -> Log.d(TAG, "distinctUntilChanged: " + s)));
    }



    public Observable<String> getJustStringObservable() {
        return Observable.just("Oreo", "Nougat", "Marshmallow", "Marshmallow", "Marshmallow", "KitKat", "Lollipop", "KitKat");
    }

    public Observable<Long> getIntervalLongObservable(int interval) {
        return Observable.interval(interval, TimeUnit.SECONDS);
    }

    public Observable<Integer> getRangeIntObservable(int startFrom, int quantity) {
        return Observable.range(startFrom, quantity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!filterDisposable.isDisposed()) filterDisposable.dispose();
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupNavigationListener() {

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initView() {
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

}
