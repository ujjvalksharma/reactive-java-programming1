import java.util.function.Supplier;

import Util.Util;
import reactor.core.publisher.Mono;

public class SimpleMonoExample {
	
	public static void main(String[] args) {
	
		// simple mono
	Mono<Integer> intPublisher=	Mono.just(1);
	
	// mono with error
	Mono<Integer> intErrPublisher=	Mono.just(1)
			                        .map((i)->i/0);
	
	// use only 1 consumer functional interface object
	intPublisher.subscribe((i)->{
			System.out.println("Recieved:"+i);
		});
	
	// use 2 consumer and 1 runnable functionla interface object
	intPublisher.subscribe((i)->{
		System.out.println("Recieved: "+i);
	}, (err)->{
		System.out.println("err:"+err.getMessage());
		
	}, ()->{
		System.out.println("Completed");
	});
	
	// use 2 consumer and 1 runnable functional interface object for error
	intErrPublisher.subscribe((i)->{
		System.out.println("Recieved: "+i);
	}, (err)->{
		System.out.println("err:"+err.getMessage());
		
	}, ()->{
		System.out.println("Completed");
	});
	
	// use one subscriber object for implementation
	System.out.println("Hi");
	intPublisher.subscribe(Util.getSubscriber());
	

	
	// item won't retriving until subscribed
		Supplier<Integer> supplier=()->{
			return getRandomNum();
		};
	Mono<Integer> itemWillRetrive=Mono.just(getRandomNum());
	
	// In suplier an item won't retrive until it is subscribed
	Mono<Integer> itemWontRetriveUntilSubscribe=Mono.fromSupplier(supplier);
	
	
	
	}
	
public static int getRandomNum() {
	
	System.out.println("item is retriving..");
	return 1;
		
	}

}
