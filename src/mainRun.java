import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


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
class Surface extends JPanel
{
    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.drawString("Java 2D", 50, 50);
        g2d.setColor(Color.blue);
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
               RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setRenderingHints(rh);
        
        Dimension size = getSize();
        Insets insets = getInsets();
        
        int windowWidth = size.width - insets.left - insets.right;
        int windowHeight = size.height - insets.top - insets.bottom;
        
        for (int i = 1; i < 10; i++)
        {
        	g2d.drawOval(i*windowWidth/9, 7*windowHeight/9,50,50);
        }
        
        //g2d.drawOval(170, 130, 50, 50);
        //g2d.fillOval(270, 130, 50, 50);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
}

public class mainRun extends JFrame implements MouseListener
{
	public mainRun()
	{
		setTitle("Project Nodrog");

        add(new Surface());
        addMouseListener(this);

        setSize(1024, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}

	public static void main(String[] args)
	{
		
		mainRun mainWindow = new mainRun();
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