import java.time.Duration;

import Util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class RepeatAndRetry {
	
	public static void main(String[] args) {

		// repeat: ask from publisher from data again
		Flux.range(0, 5)
	//	.repeat(2) // number of times it should repeat
		// this is boolean predicate that doesn't get any arg, but returns boolean
		//also number of times it should repeat
		.repeat(2, ()->true)
		.cache() //publisher will store in cache rather than calculating it
		.subscribe((i)->System.out.println("flux repeat: "+i));
		
		System.out.println("-------------------------------");
		// retry
		Flux.range(1, 5)
		.map(i->{
			if(i%2==0) {
				return i/0;
			}
			return i;
		})
		//.retry() //indefinite amount of retry
		// since we retry the publisher will again request elements 
		//	from the same flux and starting element will come again
		.retry(2)
		.doOnError((err)->System.out.println("Error:"+err.getMessage()))
		.subscribe(Util.getSubscriber());
		
		// retry when
		Flux.range(1, 5)
		.map(i->{
			if(i%2==0) {
				return i/0;
			}
			return i;
		})
		//Retry.fixedDelay(max_retry, time_after_which_to_retry)            
		.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
		.subscribe(Util.getSubscriber());
		
		// (advance retry) retry based on condition
		   orderService(Util.getFaker().business().creditCardNumber())
           .retryWhen(Retry.from(
                flux -> flux
                           .doOnNext(rs -> {
                               System.out.println(rs.totalRetries());
                               System.out.println(rs.failure());
                           })
                           .handle((rs, synchronousSink) -> {
                               if(rs.failure().getMessage().equals("500"))
                                   synchronousSink.next(1);
                               else
                                   synchronousSink.error(rs.failure()); //no retry
                           })
                           .delayElements(Duration.ofSeconds(1))
           ))
           .subscribe(Util.getSubscriber());

   Util.sleepThreadInSeconds(60);
		
	}
	

    // order service
    private static Mono<String> orderService(String ccNumber){
        return Mono.fromSupplier(() -> {
            processPayment(ccNumber);
            return Util.getFaker().idNumber().valid();
        });
    }

    // payment service
    private static void processPayment(String ccNumber){
        int random = Util.getFaker().random().nextInt(1, 10);
        if(random < 8)
            throw new RuntimeException("500");
        else if(random < 10)
            throw new RuntimeException("404");
    }

}
