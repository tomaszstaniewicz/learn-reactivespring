package io.learnreacitvespring.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void virtualTimeTest() {
        VirtualTimeScheduler.getOrSet();

        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.withVirtualTime(flux::log)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(100))
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }
}
