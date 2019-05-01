package io.learnreacitvespring.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdVsHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofSeconds(1));

        just.subscribe(s -> System.out.println("Subscriber 1 received: " + s));
        Thread.sleep(3000);

        // subscriber 2 receives all elements
        just.subscribe(s -> System.out.println("Subscriber 2 received: " + s));
        Thread.sleep(7000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<Integer> coldFlux = Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofSeconds(1));
        ConnectableFlux<Integer> hotFlux = coldFlux.publish();
        hotFlux.connect();

        hotFlux.subscribe(s -> System.out.println("Subscriber 1 received: " + s));
        Thread.sleep(3000);

        // subscriber 2 will receive only elements which were emmited after the subscription
        hotFlux.subscribe(s -> System.out.println("Subscriber 2 received: " + s));
        Thread.sleep(7000);
    }
}
