import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import Util.*;
public class ThreadingAndScheduling {
	
	public static void main(String[] args) {
	/*	
Flux<Object> simpleFlux=Flux.create((fluskSink)->{
			
			for(int i=1;i<5;i++) {
				System.out.println("emitting by :"+Thread.currentThread().getName());
				fluskSink.next(i);
			}
			
		})
		.doOnNext((i)->System.out.println(Thread.currentThread().getName()));

Runnable runnable=()->simpleFlux.subscribe(Util.getSubscriber());

for(int i=0;i<5;i++){
	
	Thread currentThread=new Thread(runnable);
	currentThread.start();
}
*/
// Simple subscribeOn and publishoN
//Subscribe On threads are basically for emitting and generating the data
//and publishOn for doing task on them

/*		
Flux<Object> subscribeOnFlux=Flux.create((fluskSink)->{
	for(int i=1;i<5;i++) { //block is excuted by subscribeOn/Main
		System.out.println("emitting by :"+Thread.currentThread().getName());
		fluskSink.next(i);
	}
	fluskSink.complete();
})
.doOnNext((i)->System.out.println(Thread.currentThread().getName())) //subscribeOn/Main
.doFirst(()->System.out.println(Thread.currentThread().getName()))
.subscribeOn(Schedulers.boundedElastic())
.publishOn(Schedulers.boundedElastic())
.doOnNext((i)->System.out.println(Thread.currentThread().getName())) //publishOn/Main
.doFirst(()->System.out.println(Thread.currentThread().getName()));
Runnable runnableBoundedElastic=()->subscribeOnFlux.subscribe((i)->System.out.println("recieved by : "+Thread.currentThread().getName()+" "+i));


for(int i=0;i<5;i++){
	
	Thread currentThread=new Thread(runnableBoundedElastic);
	currentThread.start();
}
//since a lot of threads are running flux and main would finish, so we have to make it sleep for sometime to observe the work
Util.sleepThreadInSeconds(60); 
		*/
		// parallel processing demo using paralle and runOn
		// we can't have subscribeOn and parall at the same time
		List<Object> cache=new ArrayList<Object>();
		Flux.create((fluskSink)->{
			
			for(int i=0;i<100;i++) {
				// emiting is done by main/subscribeOn
				System.out.println("emitting: "+Thread.currentThread().getName());
				fluskSink.next(i);
			}
			fluskSink.complete();
		})
		.parallel()
		.runOn(Schedulers.boundedElastic())
		.doOnNext((i)->System.out.println("doOnNext: "+Thread.currentThread().getName()))
		//.subscribeOn(Schedulers.boundedElastic())
		.subscribe(
			(i)->{
				System.out.println("recieved: "+Thread.currentThread().getName()+" "+i);
					cache.add(i);
			}
			,(err)->System.out.println("Error: "+err.getMessage())
		,()->System.out.println("cache size: "+cache.size()));
		
		Util.sleepThreadInSeconds(60); // this lets the main thread to sleep for sometime
	}
}
