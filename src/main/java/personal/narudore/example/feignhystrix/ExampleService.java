package personal.narudore.example.feignhystrix;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Ray Naldo
 */
@FeignClient(name = "exampleFeign", url = "http://localhost", fallbackFactory = ExampleServiceFallbackFactory.class)
public interface ExampleService {

    @GetMapping("/example.php")
    List<Example> getAllExamples();
}
