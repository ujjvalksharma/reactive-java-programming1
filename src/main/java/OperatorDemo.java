import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.logging.Handler;

import Util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

public class OperatorDemo {
	
	public static void main(String args[]) {
		// handle (Bi consumer) takes 2 arg and return void= map+ filter
		
		BiConsumer<Integer, SynchronousSink<Integer>> biConsumer=(num,synSink)->{
			if(num%2==0) {
			synSink.next(num);
			}
		};
		
		// handle takes Biconsumer as input and returns a flux
		// handle is basically used to makes conditional add item in flux and such as we can be flux sink
		Flux.range(0, 10)
	        .handle(biConsumer)
	        .subscribe((i)->System.out.println(i));
		System.out.println("--------------------------");
		// common do callbacks
		Flux.create(fluskSink->{
			System.out.println("emiting..");
			fluskSink.next(1);
			fluskSink.next(2);
			fluskSink.next(3);
			fluskSink.complete();
			
		})
		// executed just before Completed and will execute if the flux finish, in infinite 
		// flux it won't execute till end 
		.doOnComplete(()->System.out.println("doOnComplete executed"))
		// executed before emitting
		.doFirst(()->System.out.println("doFirst executed")) // executed before emitting
		// executed just before consuming the element
		.doOnNext((i)->System.out.println("doOnNext executed"))
		.doOnRequest((l)->System.out.println("doOnRequest executed: "+l))
		//executed just after doFirst
		.doOnSubscribe(subscriberObj->System.out.println("doOnSubscribe executed:"+subscriberObj))
		.doOnCancel(()->System.out.println("doOnCancel executed"))
		.doOnTerminate(()->System.out.println("doOnCancel executed"))
		.doOnError((err)->System.out.println("doOnError executed: "+err.getMessage()))
	
		.subscribe(Util.getSubscriber());
		
		
		/* limit rate is used when we want to limit data in pipeline and after most 
		of the data is finished in piepeline more data is request by subscriber 
		and data is pushed to pipline*/
		System.out.println("--------------------------");
		Flux.range(0, 10)
		.log()
		//limitRate(bounded_request_limit,percent_pipeline_finish_request
		.limitRate(10, 50) 
		.log()
		.subscribe(Util.getSubscriber());
		System.out.println("--------------------------");
		// operator delay works like rate limit but rate limit is 32 which can be ranged
		Flux.range(0, 40)
		.log()
		.delayElements(Duration.ofSeconds(2))
		.log()
		.subscribe(Util.getSubscriber());
		System.out.println("--------------------------");
		// verify onError for resilient pipeline
		Flux.range(0, 5)
		.map(i->i/0)
	//	.onErrorReturn(-1)
	//	.onErrorResume(err->Flux.just(100))
		.onErrorContinue((err,errorObj)->{
			System.out.println("errorObj: "+errorObj);
			System.out.println("err: "+err.getMessage());
		})
		.subscribe(Util.getSubscriber());
		
		// timeout
		
	
		//uncomment to see affect of timeout
	/*	
	  	System.out.println("--------------------------");
		  getOrderNumbers()
          .timeout(Duration.ofSeconds(1), Mono.just(100)) // time out is executed only once
          .subscribe((i)->System.out.println("timeout: "+i));


  Util.sleepThreadInSeconds(100);
		*/
		// defaultifempty it is generally used if we if know that our filter is going to empty the flux
	    //so we show the default item
		System.out.println("--------------------------");
		Flux.range(0, 20)
		.filter((i)->i>100)
		.defaultIfEmpty(100) // for this the whole flux has to be empty
		.subscribe((i)->System.out.println("default if empty flux: "+i));
		
		// switch if empty returns a publisher rather than default value
		
		Flux.range(0, 20)
		.filter((i)->i>100)
		.switchIfEmpty(Flux.just(0,1,2,3,3,4)) // for this the whole flux has to be empty
		.subscribe((i)->System.out.println("switch if empty flux: "+i));
	}
	
	 private static Flux<Integer> getOrderNumbers(){
	        return Flux.range(1, 10)
	                    .delayElements(Duration.ofSeconds(5));
	    }

	    private static Flux<Integer> fallback(){
	        return Flux.range(100, 10)
	                    .delayElements(Duration.ofSeconds(5));
	    }

	    

}
