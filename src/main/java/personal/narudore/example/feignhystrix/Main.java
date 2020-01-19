package personal.narudore.example.feignhystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ray Naldo
 */
@Slf4j
@Component
public class Main implements CommandLineRunner {

    private AtomicInteger requestCounter = new AtomicInteger(0);
    private AtomicInteger responseCounter = new AtomicInteger(0);

    private ExampleService api;

    public Main(ExampleService api) {
        this.api = api;
    }

    @Override
    public void run(String... args) throws Exception {
        Observable.range(1, 10_000)
            .concatMap(i -> Observable.just(i).delay(80, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()))
            .flatMap(i -> request()
                .toObservable()
                .doOnError(throwable -> log.error("Error invoking request", throwable))
                .subscribeOn(Schedulers.io()))
            .doOnError(throwable -> log.error("Error invoking request", throwable))
            .timestamp()
            .subscribeOn(Schedulers.computation())
            .doOnNext(timedList -> {
                System.out.print(responseCounter.addAndGet(1) + "#");
                System.out.print(timedList.getTimestampMillis() + "=");
                System.out.println(timedList.getValue().size());
            })
            .subscribe();

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private Single<List<Example>> request() {
        System.out.println("Request #" + requestCounter.addAndGet(1));
        return Single.fromCallable(() -> api.getAllExamples());
    }
}
