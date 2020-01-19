package personal.narudore.example.feignhystrix;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Ray Naldo
 */
@RestController
public class ExampleController {

    @GetMapping("/test")
    public Observable<String> test() {
        return Observable.just("214", "55", "24").flatMap(s -> Observable.just(s).delay(1000, TimeUnit.MILLISECONDS));
    }
}
