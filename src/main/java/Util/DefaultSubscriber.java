package Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DefaultSubscriber implements Subscriber<Object>{
/**
 * No events will be sent by a Publisher until demand is signaled via this method.
It can be called however often and whenever needed—but if the 
outstanding cumulative demand ever becomes Long.MAX_VALUE 
or more, it may be treated by the Publisher as "effectively unbounded".
Whatever has been requested can be sent by the Publisher so 
only signal demand for what can be safely handled.


 */
	@Override
	public void onSubscribe(Subscription s) {
		
		s.request(Integer.MAX_VALUE);
		
	}

	@Override //Consumer
	public void onNext(Object t) {
		System.out.println("Recieved: "+t);
		
	}

	@Override //Consumer
	public void onError(Throwable t) {
		System.out.println("Err: "+t.getMessage());
		
	}

	@Override //Runable
	public void onComplete() {
		System.out.println("Completed");
		
	}

}
