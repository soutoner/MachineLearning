package utils;
import java.lang.Math;
public class Functions {

/**
 * 
 * @param probs probability of an event
 * @return Associated entropy for probs
 */
	public static float entropy(float[] probs){
		float entropy = 0;
		
		for(int i = 0; i< probs.length;i++){
			
			entropy += -probs[i]*Math.log10((1/probs[i]));
			
		}
		
		return entropy;
	}
/**
 * 
 * @param entropyOfS
 * @param probsOfEvent 
 * @param entropyOfEvent
 * @return Associated earning for event
 */
	public static float earning(float entropyOfS,float[] probsOfEvent,float[] entropyOfEvent){
		float earning = entropyOfS;
		
		for(int i = 0;i < entropyOfEvent.length;i++){
			
			earning += -probsOfEvent[i]*entropyOfEvent[i];
			
		}
		
		return earning;
	}
}
