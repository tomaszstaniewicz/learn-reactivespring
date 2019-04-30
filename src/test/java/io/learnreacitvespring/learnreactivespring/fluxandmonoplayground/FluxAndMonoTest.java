package io.learnreacitvespring.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

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

    @Test
    public void testFlatMap() {
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5", "6")
                .flatMap(s -> Flux.fromIterable(createNewArray(s)))
                .log();

        StepVerifier.create(flux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void testFlatMap_parallel() {
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5", "6")
                .window(2)
                .flatMap((s) -> s.map(this::createNewArray).subscribeOn(parallel())
                        .flatMap(Flux::fromIterable))
                .log();

        StepVerifier.create(flux)
                .expectNextCount(12)
                .verifyComplete();

    }

    private List<String> createNewArray(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(s, "new value " + s);
    }

}
