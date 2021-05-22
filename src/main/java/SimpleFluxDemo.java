import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

import Util.Util;
import reactor.core.publisher.Flux;

//Learn how to make bounded request as below request to flux is unbounded
public class SimpleFluxDemo {

	public static void main(String args[]) {
	
		// simple flux
		Flux.just(0,1,2,3,4,5)
		.subscribe(Util.getSubscriber());
		
		// flux from list
		Flux.fromIterable(Arrays.asList(1,2,3,4,5,6))
		.subscribe((num)->System.out.println("Flux from list:"+num));
		
		//flux from range
		Flux.range(0, 5)
		.subscribe((num)->System.out.println("Flux from range:"+num));
		
		// flux froms stream
		Flux.fromStream(Stream.of(1,2,3,4,5))
		//.log()
		.subscribe((num)->System.out.println("Flux by using log:"+num));
		
		// Flux using sleep, but put a sleep on main function too or else main function thread will excute and we are out of main function
		Flux.just(0,1,2,3,4,5)
		.interval(Duration.ofSeconds(1))
		.subscribe((num)->System.out.println("Flux using interval:"+num));
		
		// Flux using on stream supplier
		Flux.fromStream(()->{
			return Stream.of(0,1,2,3,3);
		});
		
		
     
		
	}
}
