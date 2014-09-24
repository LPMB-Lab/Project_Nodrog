
/*
 * Objective:
 * 8 Fingers
 * 20 presses randomly generated
 * 40 trials
 * Each press has 11 within hand and 8 between hands (Half of the between hands are same finger to finger)
 *  500ms delay each stimuli on release
 *  5000ms delay between trials
 *  avoid repetition 
 */

public class mainRun
{
	public static void main(String[] args)
	{
		final long startTime = System.nanoTime();
		Trial myTrial = new Trial();
		final long duration = System.nanoTime() - startTime;
		System.out.println("Time it took to generate trial (ms): " + duration/1000000);
	}
}