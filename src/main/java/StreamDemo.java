import java.util.Arrays;
import java.util.stream.Stream;

public class StreamDemo {
	
	public static void main(String[] args) {
		
		//Stream<Integer> streamOfInt= Stream.of(1,2,3,4,5);
		
		Stream<Integer> streamOfInt=Arrays.asList(1,2,3,4,5).stream();
	//	System.out.println("I am here1");
	streamOfInt.map(num->{
		//	System.out.println("I am here2");
					if(num>0) {
				System.out.println("inside stream:"+Thread.currentThread().getName());
		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return num*2;
		})
				.forEach(System.out::println);
		
		System.out.println("outside stream:"+Thread.currentThread().getName());
		
		
  /*      Stream<Integer> stream = Stream.of(1,2,3,4,5,6)
        .map(i -> {
                	System.out.println("I am here3");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * 2;
                });

//System.out.println(stream);
stream.forEach(System.out::println);*/
	}
	// this function is used to 
	

}
