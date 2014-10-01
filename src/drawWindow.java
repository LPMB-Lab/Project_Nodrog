import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class drawWindow extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Dimension size;
	Insets insets;
	RenderingHints rh;
	
	State m_State;
	Vector<Trial> m_vGeneratedTrials;
	Vector<Finger> m_vFingers;
	Trial m_CurrentTrial;
	int m_iCurrentTrial;
	int m_iCurrentTrialStep;
	
	int windowWidth;
	int windowHeight;
	Timer m_Timer;
	
	BufferedImage startButton;
	
	public drawWindow()
	{
		setSize(1024,800);
		m_vFingers = new Vector<Finger>();
		m_vGeneratedTrials = new Vector<Trial>();
		addMouseListener(this);
		size = getSize();
        insets = getInsets();
        try {
			startButton = ImageIO.read(new File("startButton.png"));
		} catch (IOException e) {
			startButton = null;
			e.printStackTrace();
		}
        
		
		Reset();
	}
	
	private void Reset()
	{
		m_State = State.IDLE;
		m_Timer = new Timer();
		m_vGeneratedTrials.clear();
		m_vFingers.clear();
		
		m_iCurrentTrial = 0;
		m_iCurrentTrialStep = 0;
		
		windowWidth = size.width - insets.left - insets.right;
        windowHeight = size.height - insets.top - insets.bottom;

		for (int i = 0; i < 40; i++)
		{
			Trial myTrial = new Trial();
			m_vGeneratedTrials.add(myTrial);
		}
	}

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.blue);
        
        switch(m_State)
        {
	        case IDLE:
	        	g2d.drawImage(startButton, 5,  5,  null);
	        	break;
	        case FINGER_TRACKING:
	        	g2d.drawString("FINGER TRACKING", windowWidth/2, 50);
	        	break;
	        case INITIAL_COUNTDOWN:
	        	g2d.drawString("INITIAL COUNTDOWN...", windowWidth/2, 50);
	        	break;
	        case IN_TRIAL: break;
	        case TRIAL_REST:
	        	g2d.drawString("TRIAL #" + m_iCurrentTrial + " Complete! 5 second rest before next!", windowWidth/2, 50);
	        	break;
	        case COMPLETED:
	        	g2d.drawString("The test is complete! Thank you for participating!", windowWidth/2, 50);
	        	break;
        }
        
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        for (int i = 0; i < m_vFingers.size(); i++)
        {
        	int x = m_vFingers.get(i).getX();
        	int y = m_vFingers.get(i).getY();
        	
        	if (m_vFingers.get(i).isFill())
        		g2d.fillOval( x, y, 50, 50);
        	else
        		g2d.drawOval( x, y, 50, 50);
        }
    }
    
    class updateTask extends TimerTask
	{
    	State state;
    	
    	updateTask(State state) {this.state = state;}
		public void run()
		{
			m_State = state;
			
			if (m_State == State.IN_TRIAL)
				updateTrial();
		}
	}
    
    private void updateTrial()
    {
    	m_CurrentTrial = m_vGeneratedTrials.get(m_iCurrentTrial);
    	int fingerIndex = m_CurrentTrial.getCurrentFinger(m_iCurrentTrialStep);
    	
    	switch (fingerIndex)
    	{
	    	case -4: m_vFingers.get(0).setFill(true);	break;
	    	case -3: m_vFingers.get(1).setFill(true);	break;
	    	case -2: m_vFingers.get(2).setFill(true);	break;
	    	case -1: m_vFingers.get(3).setFill(true);	break;
	    	case 1: m_vFingers.get(4).setFill(true);	break;
	    	case 2: m_vFingers.get(5).setFill(true);	break;
	    	case 3: m_vFingers.get(6).setFill(true);	break;
	    	case 4: m_vFingers.get(7).setFill(true);	break;
    	}
    	
    	repaint();
    }
    
	@Override
	public void mousePressed(MouseEvent e)
	{	
		int x = e.getX();
		int y = e.getY();
		
		
		switch(m_State)
        {
	        case IDLE:
        	{
        		if (x < 95 &&
        			x > 5 &&
        			y < 45 &&
        			y > 5)
        		{
        			System.out.println("FINGER TRACKING");
        			m_State = State.FINGER_TRACKING;
        		}
        		break;
        	}
	        case FINGER_TRACKING:
	        {
	        	m_vFingers.addElement(new Finger(x-25, y-25));
	        	System.out.println("GOT " + m_vFingers.size() + " FINGERS (" + x + ", " + y + ")");
	        	
	        	if (m_vFingers.size() >= 8)
	        	{
	        		m_State = State.INITIAL_COUNTDOWN;
	        		m_Timer.schedule(new updateTask(State.IN_TRIAL), 5000);
	        	}
        		break;
        	}
	        case IN_TRIAL:
	        {
	        	m_CurrentTrial = m_vGeneratedTrials.get(m_iCurrentTrial);
	        	int fingerIndex = m_CurrentTrial.getCurrentFinger(m_iCurrentTrialStep);
	        	
	        	switch (fingerIndex)
	        	{
	    	    	case -4:CheckClick(x, y, 0);	break;
	    	    	case -3: CheckClick(x, y, 1);	break;
	    	    	case -2: CheckClick(x, y, 2);	break;
	    	    	case -1: CheckClick(x, y, 3);	break;
	    	    	case 1: CheckClick(x, y, 4);	break;
	    	    	case 2: CheckClick(x, y, 5);	break;
	    	    	case 3: CheckClick(x, y, 6);	break;
	    	    	case 4: CheckClick(x, y, 7);	break;
	        	}
        		break;
        	}
		default:
			break;
        }
		
		repaint();
	}
	
	private void CheckClick(int x, int y, int fingerID)
	{
		int x1 = m_vFingers.get(fingerID).getX();
		int y1 = m_vFingers.get(fingerID).getY();
		int z = (int) Math.sqrt(Math.pow((x1+25-x), 2) + Math.pow((y1+25-y), 2));
		
		System.out.println("User clicked: (" + x + "," + y + ") and is " + z + " pixels off");
		if ( z < 25)
		{
			m_vFingers.get(fingerID).setFill(false);
			repaint();
			
			if (m_iCurrentTrialStep == 19)
			{
				if (m_iCurrentTrial == 39)
				{
					m_State = State.COMPLETED;
				}
				else
				{
					m_iCurrentTrial++;
					m_iCurrentTrialStep = 0;
					m_State = State.TRIAL_REST;
					m_Timer.schedule(new updateTask(State.IN_TRIAL), 5000);
				}
			}
			else
			{
				m_iCurrentTrialStep++;
				try {Thread.sleep(500);}
				catch (InterruptedException e) {e.printStackTrace();}
    			updateTrial();
			}
    		
		}
	}

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}
}