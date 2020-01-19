package personal.narudore.example.feignhystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ray Naldo
 */
@Slf4j
@Component
public class ExampleServiceFallbackFactory implements FallbackFactory<ExampleService> {

    private static AtomicInteger failedCount = new AtomicInteger(0);

    @Override
    public ExampleService create(Throwable cause) {
        return () -> {
            log.error("Error #{} when invoking feign client", failedCount.addAndGet(1), cause);
            return Collections.emptyList();
        };
    }
}
