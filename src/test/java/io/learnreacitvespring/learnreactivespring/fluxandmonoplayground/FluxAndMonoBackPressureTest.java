package io.learnreacitvespring.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import javax.sound.midi.Soundbank;

public class FluxAndMonoBackPressureTest {

    @Test
    public void textBackPressure() {

        Flux<Integer> flux = Flux.range(1, 10);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void textBackPressureWithSubscriber() {

        Flux<Integer> flux = Flux.range(1, 10);

        flux.subscribe(
                s -> System.out.println(" received: " + s),
                e -> System.out.println("error: " + e),
                () -> System.out.println("completed"),
                s -> s.request(2)
        );
    }

    @Test
    public void textBackPressureWithBaseSubscriber() {

        Flux<Integer> flux = Flux.range(1, 10).log();

        flux.subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnNext(Integer value) {
                if (value == 4) {
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("Completed reached");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("Cancel reached");
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
