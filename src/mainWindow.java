import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
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

public class mainWindow extends JFrame implements MouseListener
{
	public enum State
	{
		IDLE,
		GENERATE,
		INITIAL_COUNTDOWN,
		IN_TRIAL,
		TRIAL_REST,
		COMPLETED,
	}
	
	State m_State;
	
	public mainWindow()
	{
		setTitle("Project Nodrog");
        add(new drawWindow());
        addMouseListener(this);
        setSize(1024, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	public void StartProgram()
	{
		mainWindow mainWindow = new mainWindow();
        mainWindow.setVisible(true);
		
        /*
		SwingUtilities.invokeLater(new Runnable()
		{
            @Override
            public void run()
            {
                mainRun mainWindow = new mainRun();
                mainWindow.setVisible(true);
            }
        });
		*/

        
		Vector<Trial> generatedTrials = new Vector<Trial>();
		
		for (int i = 0; i < 20; i++)
		{
			Trial myTrial = new Trial();
			generatedTrials.add(myTrial);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		System.out.println(x + " " + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}