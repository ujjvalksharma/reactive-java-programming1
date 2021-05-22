package Util;

import java.util.Random;
import java.util.function.Consumer;

import reactor.core.publisher.FluxSink;

public class DefaultFluskSink implements Consumer<FluxSink<Object>>{

	FluxSink<Object> currentFluxObj;
	@Override
	public void accept(FluxSink<Object> t) {
	this.currentFluxObj=t;
		
	}
	
	public void produce() {
		Random random= new Random();
		currentFluxObj.next(random.nextInt(20));
	}

}
