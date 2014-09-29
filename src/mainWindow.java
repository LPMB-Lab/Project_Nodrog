import javax.swing.JFrame;

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

public class mainWindow extends JFrame
{
	public static void main(String[] args) {new mainWindow();}
	
	public mainWindow()
	{	
		setTitle("Project Nodrog");
        add(new drawWindow());
        setSize(1024, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
}