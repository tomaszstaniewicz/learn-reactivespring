package io.learnreacitvespring.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoTest {


    @Test
    public void fluxBufferTest() {

        Flux<List<String>> stringFlux = Flux.just("1", "2", "3", "4", "5", "6")
                .buffer(2)
                .log();

        stringFlux.subscribe(System.out::println, System.out::println);
    }

    @Test
    public void verifyNext() {
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5", "6");

        StepVerifier.create(flux)
                .expectNext("1", "2", "3", "4", "5", "6")
                .verifyComplete();
    }

    @Test
    public void verifyError() {
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5", "6")
                .concatWith(Flux.error(new RuntimeException("exception message")));

        StepVerifier.create(flux)
                .expectNextCount(6)
                .expectErrorMessage("exception message")
                .verify();
    }
}
