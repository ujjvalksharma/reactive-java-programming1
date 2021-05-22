import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import Util.*;
public class BckPressOverFlw {
	
	public static void main(String[] args) {
		
		// In this scenior all items are pushed, but got later on which is bad as item get stored in cache 
		//and it can cause memory out of bound err
	/*	 Flux.create(fluxSink -> {
	            for (int i = 1; i < 501; i++) {
	                fluxSink.next(i);
	                System.out.println("Pushed by: "+Thread.currentThread().getName()+" item: " + i);
	            }
	            fluxSink.complete();
	        })
	            .publishOn(Schedulers.boundedElastic())
	            .doOnNext(i -> {
	                Util.sleepThreadInSeconds(1);
	            })
	            .subscribe(Util.getSubscriber());


	        Util.sleepThreadInSeconds(60);*/
	        
	        //overflow strategy1
	     System.setProperty("reactor.bufferSize.small", "16");
	     List<Object> list = new ArrayList<>();
	     //back pressure drop

	   /*   

	        Flux.create(fluxSink -> {
	            for (int i = 1; i < 201; i++) {
	                fluxSink.next(i);
	                System.out.println("Pushed : " + i);
	                Util.sleepThreadInSeconds(1);
	            }
	            fluxSink.complete();
	        })//.onBackpressureDrop() //drops the last item
	                .onBackpressureDrop(list::add) //do something with dropped item
	                .publishOn(Schedulers.boundedElastic())
	                .doOnNext(i -> {
	                    Util.sleepThreadInSeconds(10);
	                })
	                .subscribe(Util.getSubscriber());


	        Util.sleepThreadInSeconds(10);
	        System.out.println("cache: "+list);
	        
	        */
	        
	      Flux.create(fluxSink -> {
	    	  
	    	  // flux is cancelled on error, so it shouldn't produce more
	            for (int i = 1; i < 201&& !fluxSink.isCancelled(); i++) {
	                fluxSink.next(i);
	                System.out.println("Pushed : " + i);
	                Util.sleepThreadInSeconds(1);
	            }
	            fluxSink.complete();
	        })//.onBackpressureDrop() //drops the last item
	                .onBackpressureError() // throw error if there is a back pressure
	                .publishOn(Schedulers.boundedElastic())
	                .doOnNext(i -> {
	                    Util.sleepThreadInSeconds(10);
	                })
	                .distinct() // distinct elements
	                .subscribe(Util.getSubscriber());


	        Util.sleepThreadInSeconds(10);
	        System.out.println("cache: "+list);
	        
	        // increase queue size on overflow
	        Flux.create(fluxSink -> {
	            for (int i = 1; i < 201 && !fluxSink.isCancelled(); i++) {
	                fluxSink.next(i);
	                System.out.println("Pushed : " + i);
	                Util.sleepThreadInSeconds(1);
	            }
	            fluxSink.complete();
	        })
	                .onBackpressureBuffer(50, o -> System.out.println("Dropped : "+ o))
	                .publishOn(Schedulers.boundedElastic())

	                .doOnNext(i -> {
	                    Util.sleepThreadInSeconds(10);
	                })
	                .subscribe(Util.getSubscriber());


	        Util.sleepThreadInSeconds(10);
	}

}
