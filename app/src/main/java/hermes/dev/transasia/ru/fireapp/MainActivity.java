package hermes.dev.transasia.ru.fireapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
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

//          Suppressing operators
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
//        distinctUntilChanged();
//        elementAt(20);
//        elementAtOrError(20);
//        singleElement();
//        firstElement();
//        lastElement();

//          Transforming operators
//        map();
//        cast();
//        startWith();
//        startWithArray();
//        defaultIfEmpty(20);
//        switchIfEmpty();
//        sorted();
//        delay(8, 10000);
//        delaySubsciption(5, 0);
//        repeat(4);
//        repeatUntil();
//        repeatWhen();
//        scan();


//          Reducing operators
        // TODO: 11.07.2018 USE only with finite observable
//        count();
//        reduce();
//        all();
//        any();
//        contains();

//        Collections operators always return Single<>
//        toList();
//        toSortedList();
//        toMap();
//        toMultimap();
//        collect();

//          Error recovery operations
//        onErrorReturnItem();
//        onErrorReturn();
//        handleErrorWithMap();
//        onErrorResumeNext();
//        retry();
//        retryUntil();
//        retryWhen();

//        Actions operators
//        doOnError();
//        doOnSuccess();
//        do_s_Other();

        //Combining operators
//        merge();
        flatMap();
        flatMapIterable();


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

    public void distinct() {

        compositeDisposable.add(getJustStringObservable()
                .distinct()
                .map(String::length)
                .subscribe(s -> Log.d(TAG, "distinct: " + s)));
    }

    public void distinctWithLambda() {

        compositeDisposable.add(getJustStringObservable()
//                .map(s -> {
//                    Log.d(TAG, "map: " + s);
//                    return s.equals("KitKat");
//                })
                .distinct(s -> s.toString().length())
                .subscribe(s -> Log.d(TAG, "distinct: " + s)));
    }

    public void distinctUntilChanged() {
        compositeDisposable.add(getJustStringObservable()
                .distinctUntilChanged()
                .subscribe(s -> Log.d(TAG, "distinctUntilChanged: " + s)));
    }

    public void elementAt(long number) {
        //Maybe
        compositeDisposable.add(getJustStringObservable()
                .elementAt(number)
//                .defaultIfEmpty("Ice Cream Sandwitch")
                .switchIfEmpty(getStringMaybe())
                .subscribe(
                        s -> Log.d(TAG, "elementAt: " + s),
                        e -> Log.d(TAG, "elementAt: " + "OnError"),
                        () -> Log.d(TAG, "elementAt: " + "On Complete")));
    }

    public void elementAtOrError(long number) {
        //Single
        compositeDisposable.add(getJustStringObservable()
                .elementAtOrError(number)
//                .defaultIfEmpty("Ice Cream Sandwitch")
//                .switchIfEmpty(getStringMaybe())
                .subscribe(
                        s -> Log.d(TAG, "elementAtOrError: " + s),
                        e -> Log.d(TAG, "elementAtOrError: " + "OnError")));
    }

    public void singleElement() {
        //from Observable into Maybe (or make an error if there are more than one emitted item)
        compositeDisposable.add(getJustStringObservable()
//                .take(1)
//                .take(2)
                .singleElement()
                .subscribe(
                        s -> Log.d(TAG, "singleElement: " + s),
                        e -> Log.d(TAG, "singleElement: " + "OnError"),
                        () -> Log.d(TAG, "singleElement: " + "On Complete")));
    }

    public void firstElement() {
        //from Observable into Maybe
        compositeDisposable.add(getJustStringObservable()
                .firstElement()
                .subscribe(
                        s -> Log.d(TAG, "firstElement: " + s),
                        e -> Log.d(TAG, "firstElement: " + "OnError"),
                        () -> Log.d(TAG, "firstElement: " + "On Complete")));
    }

    public void lastElement() {
        //from Observable into Maybe
        compositeDisposable.add(getJustStringObservable()
                .lastElement()
                .subscribe(
                        s -> Log.d(TAG, "lastElement: " + s),
                        e -> Log.d(TAG, "lastElement: " + "OnError"),
                        () -> Log.d(TAG, "lastElement: " + "On Complete")));
    }

    public void map() {

        compositeDisposable.add(getJustStringObservable()
                .map(s -> s + " " + s.length())
                .subscribe(d -> Log.d(TAG, "map: " + d)));


        compositeDisposable.add(getJustStringObservable()
                .map(s -> {
                    if (s.length() % 2 == 0) {
                        return new Pair<>(s, true);
                    } else return new Pair<>(s, false);
                })
                .subscribe(res -> Log.d(TAG, "map: " + res.first + " " + res.second)));
    }

    public void cast() {
//        Observable<Object> objectObservable = getJustStringObservable().map(s -> (Object) s);
//        or the same result but shorter:
        Observable<Object> objectObservable = getJustStringObservable().cast(Object.class);
        compositeDisposable.add(objectObservable
                .map(o -> {
                    if (o instanceof Object) {
                        return new Pair<>(o, true);
                    } else {
                        return new Pair<>(o, false);
                    }
                })
                .subscribe(o -> Log.d(TAG, "cast: " + o.first + ", " + o.second)));
    }

    public void startWith() {
        compositeDisposable.add(getJustStringObservable()
                .startWith("There are some android versions:")
                .subscribe(s -> Log.d(TAG, "startWith: " + s)));
    }

    public void startWithArray() {
        compositeDisposable.add(getJustStringObservable()
                .startWithArray("To see", "all android", "versions", "see android.developer.com")
                .subscribe(s -> Log.d(TAG, "startWith: " + s)));
    }

    public void defaultIfEmpty(long number) {
        compositeDisposable.add(getJustStringObservable()
                .elementAt(number)  //Maybe
                .defaultIfEmpty("Ice Cream Sandwitch")
                .subscribe(
                        s -> Log.d(TAG, "defaultIfEmpty: " + s),
                        e -> Log.d(TAG, "defaultIfEmpty: " + "OnError"),
                        () -> Log.d(TAG, "defaultIfEmpty: " + "On Complete")));
    }

    public void switchIfEmpty() {
        compositeDisposable.add(getJustStringObservable()
                .filter(s -> s.length() == 20)
                .switchIfEmpty(getJustStringDateobservable())
                .subscribe(
                        s -> Log.d(TAG, "switchIfEmpty: " + s),
                        e -> Log.d(TAG, "switchIfEmpty: " + "OnError"),
                        () -> Log.d(TAG, "switchIfEmpty: " + "On Complete")));
    }

    public void sorted() {
        // for finite observable
        compositeDisposable.add(getJustStringObservable()
                .sorted(Comparator.reverseOrder())
                .map(s -> {
                    Log.d(TAG, "sorted reverseOrder: " + s);
                    return s;
                })
                .sorted()
                .map(s -> {
                    Log.d(TAG, "sorted normalOrder: " + s);
                    return s;
                })
                .sorted((x, y) -> Integer.compare(x.length(), y.length()))
                .map(s -> {
                    Log.d(TAG, "sorted by length: abs " + s);
                    return s;
                })
                .sorted((x, y) -> Integer.compare(y.length(), x.length()))
                .map(s -> {
                    Log.d(TAG, "sorted by length: dec " + s);
                    return s;
                })
                .subscribe());
    }

    public void delay(int delay, long sleep) {
        compositeDisposable.add(getJustStringDateobservable()
                .delay(delay, TimeUnit.SECONDS, true)
                .subscribe(s -> Log.d(TAG, "delay: " + s)));

        sleep(sleep);
    }

    public void delaySubsciption(int delay, long sleep) {
        compositeDisposable.add(getJustStringDateobservable()
                .delaySubscription(delay, TimeUnit.SECONDS)
                .subscribe(s -> Log.d(TAG, "delaySubsciption: " + s)));
        sleep(sleep);
    }

    public void repeat(long times) {
        //if times is indefinite, then repeating will be immortal)))

        compositeDisposable.add(getJustStringObservable()
                .repeat(times)
                .subscribe(s -> Log.d(TAG, "repeat: " + s)));
    }

    public void repeatUntil() {
        // boolean supplier gives true and emission ends
        compositeDisposable.add(getJustStringObservable()
                .repeatUntil(new BooleanSupplier() {
                    int count = 0;

                    @Override
                    public boolean getAsBoolean() throws Exception {
                        count++;
                        return count >= 2;
                    }
                })
                .subscribe(s -> Log.d(TAG, "repeatUntil: " + s)));
    }

    public void repeatWhen() {
//
//        RepeatWhen operator creates an observable which emits the same items emitted by the source
//        observable, but repeatWhen can resubscribe to the source observable based on the return value
//        from the supplied function.
//        This function is called only once after source observable calls onComplete or onError first
//        time. This function is passed an observable which emits an item each time source
//        observable calls onComplete or onError.
//        By subscribing to this observable, you can get notification about source observable’s onComplete call
//        and indicate back to repeatWhen operator whether to resubscribe or call onComplete on child
//        subscribers.

        Observable<String> source = Observable.just("c", "c plus", "perl", "c sharp", "dot net");

        compositeDisposable.add(source
                .repeatWhen(new Function<Observable<Object>, ObservableSource<Object>>() {

                    private int count = 0;

                    @Override
                    public ObservableSource<Object> apply(Observable<Object> objectObservable) throws Exception {
                        Log.d(TAG, "apply: " + count);
                        return objectObservable.flatMap(object -> {
                            count++;
                            if (count < 3) {
                                Log.d(TAG, "repeating: " + count);
                                return Observable.just(object);
                            } else {
                                return Observable.empty();
                            }
                        });
                    }
                })
                .onErrorReturnItem("exception returned")
                .subscribe(s -> Log.d(TAG, "repeatWhen: " + s)));


    }

    public void scan() {
        compositeDisposable.add(Observable.just(5, 3, 7, 10, 2, 14)
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(s -> Log.d(TAG, "scan: " + s)));

        Log.d(TAG, " one more try");

        compositeDisposable.add(getJustStringObservable()
                .scan(0, (total, next) -> total + 1)
                .skip(1)
                .subscribe(s -> Log.d(TAG, "scan: " + s)));
    }

    public void count() {
        compositeDisposable.add(getJustStringObservable()
                .count()  //return Single<Long>
                .subscribe(quantity -> Log.d(TAG, "count: " + quantity)));
    }

    private void reduce() {
        //return Maybe<>
        compositeDisposable.add(getRangeIntObservable(1, 5)
                .reduce((integer, integer2) -> integer + integer2)
                .subscribe(quantity -> Log.d(TAG, "reduce: " + quantity)));

        Log.d(TAG, "one more way: ");

        compositeDisposable.add(getJustStringObservable()
                .reduce((s, s2) -> {
//                        return s + (s.equals("") ? "" : ",") + s2;
//                        return s + (s.equals(" ") ? "" : "") + s2;
                    return s + (s.equals(" ") ? "" : " ") + s2;
                })
                .subscribe(quantity -> Log.d(TAG, "reduce: " + quantity)));
    }

    private void all() {
        compositeDisposable.add(getJustStringObservable()
                .all(name -> name.equals("KitKat")) //return Single<Boolean>
                .subscribe(flag -> Log.d(TAG, "all: " + flag)));

        Log.d(TAG, "one more way: all");

        compositeDisposable.add(getRangeIntObservable(1, 10)
                .all(number -> number < 20)
                .subscribe(flag -> Log.d(TAG, "all: " + flag)));
    }

    private void any() {
        compositeDisposable.add(getJustStringObservable()
                .any(name -> name.equals("KitKat")) //return Single<Boolean>
                .subscribe(flag -> Log.d(TAG, "any: " + flag)));

        Log.d(TAG, "one more way: any");

        compositeDisposable.add(getRangeIntObservable(1, 10)
                .all(number -> number > 20)
                .subscribe(flag -> Log.d(TAG, "any: " + flag)));
    }

    private void contains() {
        compositeDisposable.add(getJustStringObservable()
                .contains("KitKat") //return Single<Boolean>
                .subscribe(flag -> Log.d(TAG, "contains: " + flag)));

        Log.d(TAG, "one more way: contains");

        compositeDisposable.add(getRangeIntObservable(1, 10)
                .contains(20) //return Single<Boolean>
                .subscribe(flag -> Log.d(TAG, "contains: " + flag)));
    }

    public void toList() {
        compositeDisposable.add(Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon")
                .toList(5)  //return Single
                .subscribe(s -> {
                    for (String st : s) {
                        Log.d(TAG, "toList: " + st);
                    }
                }));
    }

    public void toSortedList() {
        compositeDisposable.add(Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .toSortedList((new Comparator<Integer>() {              //return Single
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2 - o1;
//                        return o1 - o2;
                    }
                }), 9)
                .subscribe(s -> {
                    for (Integer st : s) {
                        Log.d(TAG, "toSortedList: " + st);
                    }
                }));
    }

    public void toMap() {
        compositeDisposable.add(Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon")
                .toMap(s -> s.charAt(0))        //return Single
//                .toMap(String::length)
//                .toMap(s -> s.charAt(0), String::length, ConcurrentHashMap::new)
                .subscribe(s -> Log.d(TAG, "toMap: " + s)));
    }

    public void toMultimap() {
        compositeDisposable.add(Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon")
                .toMultimap(String::length)         //return Single
                .subscribe(s -> Log.d(TAG, "toMultimap: " + s)));
    }

    public void collect() {
        compositeDisposable.add(getJustStringObservable()
                .collect(HashSet::new, HashSet::add)
                .subscribe(set -> Log.d(TAG, "collect: " + set)));
    }

    public void onErrorReturnItem() {
        compositeDisposable.add(Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10 / i)
                .onErrorReturnItem(-1)
                .subscribe(i -> Log.d(TAG, "onErrorReturnItem: onNext " + i),
                        e -> Log.d(TAG, "onErrorReturnItem: onError " + e),
                        () -> Log.d(TAG, "onErrorReturnItem: onComplete")
                ));
    }

    public void onErrorReturn() {
        compositeDisposable.add(Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10 / i)
                .onErrorReturn(error -> -1)
                .subscribe(i -> Log.d(TAG, "onErrorReturn: onNext " + i),
                        e -> Log.d(TAG, "onErrorReturn: onError " + e),
                        () -> Log.d(TAG, "onErrorReturn: onComplete")
                ));
    }

    public void handleErrorWithMap() {
        compositeDisposable.add(Observable.just(5, 6, 7, 0, 1, 3)
                .map(i -> {
                    try {
                        return 10 / i;
                    } catch (ArithmeticException e) {
                        return -1;
                    }
                })
                .subscribe(result -> Log.d(TAG, "handleErrorWithMap: onNext " + result),
                        error -> Log.d(TAG, "handleErrorWithMap: onError " + error),
                        () -> Log.d(TAG, "handleErrorWithMap: onComplelte ")));

    }

    public void onErrorResumeNext() {
        Observable<Integer> source = getJustIntegerObservable();

        compositeDisposable.add(source
                .map(i -> 10 / i)
                .onErrorResumeNext(source.sorted(Comparator.reverseOrder())
                        .map(i -> 10 / i))
                .onErrorResumeNext(Observable.empty())
                .subscribe(i -> Log.d(TAG, "onErrorResumeNext: onNext " + i),
                        e -> Log.d(TAG, "onErrorResumeNext: onError " + e),
                        () -> Log.d(TAG, "onErrorResumeNext: onComplete")
                ));
    }

    public void retry() {
        compositeDisposable.add(getJustIntegerObservable()
                .map(i -> 10 / i)
//                .retry()  infinite
                .retry(2)
                .subscribe(i -> Log.d(TAG, "retry: onNext " + i),
                        e -> Log.d(TAG, "retry: onError " + e),
                        () -> Log.d(TAG, "retry: onComplete")
                ));
    }

    public void retryUntil() {
        compositeDisposable.add(getJustIntegerObservable()
                .map(i -> 10 / i)
                .retryUntil(new BooleanSupplier() {
                    int count = 0;

                    @Override
                    public boolean getAsBoolean() throws Exception {
                        count++;
                        return count > 2;
                    }
                })
                .subscribe(i -> Log.d(TAG, "retryUntil: onNext " + i),
                        e -> Log.d(TAG, "retryUntil: onError " + e),
                        () -> Log.d(TAG, "retryUntil: onComplete")
                ));
    }

    public void retryWhen() {
        compositeDisposable.add(getJustIntegerObservable()
                .map(i -> 10 / i)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return MainActivity.this.getJustStringObservable();
                    }
                })
                .subscribe(i -> Log.d(TAG, "retryWhen: onNext " + i),
                        e -> Log.d(TAG, "retryWhen: onError " + e),
                        () -> Log.d(TAG, "retryWhen: onComplete")
                ));
    }

    public void doOnError() {
        compositeDisposable.add(getJustIntegerObservable()
                .map(i -> 10 / i)
                .doOnError(throwable -> Log.d(TAG, "doOnError 1: "))
                .retry(1)
                .doOnError(throwable -> Log.d(TAG, "doOnError 2: "))
                .subscribe(i -> Log.d(TAG, "retry: onNext " + i),
                        e -> Log.d(TAG, "retry: onError " + e),
                        () -> Log.d(TAG, "retry: onComplete")
                ));
    }

    public void doOnSuccess() {
        compositeDisposable.add(Observable.just(5, 3, 7, 10, 2, 14)
                .reduce((total, next) -> total + next)
                .doOnSuccess(result -> Log.d(TAG, "doOnSuccess: " + result))
                .subscribe(result -> Log.d(TAG, "onSuccess: " + result)));
    }

    public void do_s_Other() {
        compositeDisposable.add(Observable.just(5, 2, 4, 0, 3, 2, 8)
                .doOnSubscribe(disposable -> Log.d(TAG, "doOnSubscribe: "))
                .doOnEach(integerNotification -> Log.d(TAG, "doOnEach: " + integerNotification.getValue()))
                .doOnNext((item) -> Log.d(TAG, "doOnNext: " + item))
                .doAfterNext((item) -> Log.d(TAG, "doAfterNext: " + item))
                .doOnComplete(() -> Log.d(TAG, "doOnComplete: "))
                .doOnDispose(() -> Log.d(TAG, "doOnDispose: "))
                .doFinally(() -> Log.d(TAG, "doFinally: at the end of chain after onComplete or onError"))
                .subscribe(i -> Log.d(TAG, "do_s_Other: onNext " + i),
                        e -> Log.d(TAG, "do_s_Other: onError " + e),
                        () -> Log.d(TAG, "do_s_Other: onComplete")
                ));
    }

    public void merge() {
        Log.d(TAG, "merge: FIRST WAY");
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma");
        Observable<String> source2 = Observable.just("Zeta", "Eta");
        Observable<String> source3 = Observable.just("Delta", "Epsilon");
        Observable<String> source4 = Observable.just("Theta");
        compositeDisposable.add(Observable.merge(source1, source2, source3, source4)
                .subscribe(i -> Log.d(TAG, "FIRST: " + i)));

        Log.d(TAG, "merge: SECOND WAY");
        compositeDisposable.add(source4.mergeWith(source3)
                .mergeWith(source1)
                .mergeWith(source2)
                .subscribe(i -> Log.d(TAG, "SECOND: " + i)));


        Log.d(TAG, "merge: THIRD WAY");
        compositeDisposable.add(Observable.mergeArray(source4, source2, source1, source3)
                .subscribe(i -> Log.d(TAG, "THIRD: " + i)));

        Log.d(TAG, "merge: FOURTH WAY");
        Observable<String> s1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .map(l -> "Source1: " + l + " seconds");
        Observable<String> s2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300)
                .map(l -> "Source2: " + l + " milliseconds");
        compositeDisposable.add(Observable.merge(s1, s2).subscribe(result -> Log.d(TAG, "FOURTH: " + result)));

        sleep(3000);
    }

    public void flatMap() {
        Log.d(TAG, "flatMap: part one");
        compositeDisposable.add(getJustStringObservable()
                .flatMap(s -> Observable.fromArray(s.split("")))
                .subscribe(s -> Log.d(TAG, "flatMap: " + s)));
        Log.d(TAG, " ");

        Log.d(TAG, "flatMap: part two");
        compositeDisposable.add(Observable.just("23123/123123/Tango", "23213/12312111/Cat", "4324/1312/Peyot")
                .flatMap(s -> Observable.fromArray(s.split("/")))
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::valueOf)
                .subscribe(s -> Log.d(TAG, "flatMap: " + s)));
        Log.d(TAG, " ");

        Log.d(TAG, "flatMap: part three");
        compositeDisposable.add(getRangeIntObservable(1, 3)
                .flatMap(number -> Observable.interval(number, TimeUnit.SECONDS))
                .map(newNumber -> newNumber + " seconds")
                .subscribe(result -> Log.d(TAG, "flatMap: " + result)));
        sleep(12000);
        Log.d(TAG, " ");

        Log.d(TAG, "flatMap: part four");
        compositeDisposable.add(getJustStringObservable()
                .flatMap(string -> Observable.fromArray(string.split("")),
                        (string, split) -> string + "-" + split)
                .subscribe(result -> Log.d(TAG, "flatMap: " + result)));
        Log.d(TAG, " ");
    }

    public void flatMapIterable(){
        compositeDisposable.add(getListString()
        .flatMapIterable(new Function<List<String>, Iterable<String>>() {
            @Override
            public Iterable<String> apply(List<String> item) throws Exception {
                return item;
            }
        })
        .subscribe(result -> Log.d(TAG, "flatMapIterable: " + result)));
    }


    private Observable<Integer> getJustIntegerObservable() {
        return Observable.just(5, 2, 4, 0, 3, 2, 8);
    }

    public Observable<String> getJustStringDateobservable() {
        return Observable.just("1/3/1986", "12/12/1999", "02/07/2018");
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

    public Maybe<String> getStringMaybe() {
        return Maybe.just("Android Version");
    }

    public Observable<List<String>> getListString(){
        List<String> list = new ArrayList<>();
        list.add("Hi");
        list.add("Bonjour");
        list.add("Privet");

        List<String> list2 = new ArrayList<>();
        list2.add("Bye");
        list2.add("OreVoua");
        list2.add("Poka");

        return Observable.just(list, list2);
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
