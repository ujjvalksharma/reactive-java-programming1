import reactor.core.publisher.Flux;
import Util.DefaultFluskSink;
import Util.DefaultSubscriber;
import Util.Util;
public class ProgramitacllyFluxDemo {
	
	public static void main(String args[]) {
		
		/**
		 * Using Flusk sink we can programmitically emit items till the developer wants. 
		 * Item just keep going in pipeline unlike in normal flux once an all items are 
		 * inserted in pipeline and then pipeline is executed
		 */
		Flux.create(fluskSink->{
			int i=0;
			while(i<5) {
				fluskSink.next(i); 
			//	Util.sleepThreadInSeconds(1); // uncomment this how in sequence flusk is getting emitted
				i++;
			}
			fluskSink.complete(); // finish inserting into flux
			
		}).subscribe((i)->System.out.println("flusk sink using complete: "+i));
		
	/*	
	 Uncomment this block if you wanna see an infinite flusk sink
		Flux.create(fluskSink->{
			int i=0;
			while(i<5) {
				fluskSink.next(i); 
				Util.sleepThreadInSeconds(1);
				i++;
			}
		//	fluskSink.complete(); // finish inserting into flux
			
		}).subscribe((i)->System.out.println("flusk sink infinite: "+i));
	
		
		Util.sleepThreadInSeconds(7);
		*/
		// custom flusksink consumer object
		DefaultFluskSink defaultFluskSink=new DefaultFluskSink();
		Flux.create(defaultFluskSink)
		.subscribe((i)->System.out.println("Custom flusk sink: "+i));
		
		defaultFluskSink.produce();
		
		// execute custom flus using different for different produce task
		
		DefaultFluskSink FluskSinkForMulThread1=new DefaultFluskSink();
		
		Flux.create(FluskSinkForMulThread1)
		.subscribe((i)->System.out.println("Custom flusk sink for : "+Thread.currentThread().getName()+" "+i));
		
		Runnable runnable=()->FluskSinkForMulThread1.produce();
		
		for(int i=0;i<10;i++) {
			Thread thread=new Thread(runnable);
			thread.start();
		}
		
		// flusk sink is emitting data even after it cancelled using take function in pipeline by subscriber
		
		Flux.create((fluskSink)->{
			int i=0;
			System.out.println("flunk sink started");
			while(i<10&&!fluskSink.isCancelled()) { // using cancelled it won't emit as it will exit out of loop
				System.out.println("fluskSink emitting: "+i);
				fluskSink.next(i); 
				i++;
			}
			fluskSink.complete();
		}).take(3)
		.subscribe((i)->System.out.println("flusk sink recived: "+i));
		;
		
		// Synschronous Sink is worked using Flux.generate, 
		//in this we can emmit only once, but the emitting works using using complete or take method
		
		Flux.generate(SynSink->{
			SynSink.next(1);
			//SynSink.next(2); // error: as only one next is allowed
		})
		.take(2) // without take function infinite emission
		.subscribe((i)->System.out.println("Syn recieved: "+i));
		
	}

}
