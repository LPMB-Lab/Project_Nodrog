import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class drawWindow extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int m_iCircleDiameter = 100;

	Dimension screenSize;
	Insets insets;
	RenderingHints rh;
	
	State m_State;
	State m_RecoveryState;
	Vector<Trial> m_vGeneratedTrials;
	Vector<Finger> m_vFingers;
	Trial m_CurrentTrial;
	int m_iCurrentTrial;
	int m_iCurrentTrialStep;
	int windowWidth;
	int windowHeight;
	boolean m_bIsPaused;
	
	Timer m_Timer;
	Button startButton;
	Button pauseButton;
	Button restartButton;
	Button quitButton;
	
	AudioClip correctSound;
	
	public drawWindow()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		m_vFingers = new Vector<Finger>();
		m_vGeneratedTrials = new Vector<Trial>();
		addMouseListener(this);
        insets = getInsets();

		try {
			startButton = new Button(ImageIO.read(getClass().getResource("images/startButton.png")), 5, 5);
			pauseButton = new Button(ImageIO.read(getClass().getResource("images/pauseButton.png")), 100, 5);
			restartButton = new Button(ImageIO.read(getClass().getResource("images/restartButton.png")), 195, 5);
			quitButton = new Button(ImageIO.read(getClass().getResource("images/quitButton.png")), 290, 5);
			correctSound = Applet.newAudioClip(getClass().getResource("sounds/correctSound.wav"));
		} catch (IOException e) {e.printStackTrace();}
        
		
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
		m_bIsPaused = false;
		
		windowWidth = screenSize.width - insets.left - insets.right;
        windowHeight = screenSize.height - insets.top - insets.bottom;

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
        	case PAUSE:	break;
	        case IDLE:
	        	g2d.drawImage(startButton.getImage(), startButton.getX(),  startButton.getY(),  null);
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
	        case INTERMISSION_REST:
	        	g2d.drawString("YOU HAVE REACHED HALF WAY! 2 MINUTE BREAK!", windowWidth/2, 50);
	        	break;
	        case COMPLETED:
	        	g2d.drawString("The test is complete! Thank you for participating!", windowWidth/2, 50);
	        	break;
        }
        
        g2d.drawImage(pauseButton.getImage(), pauseButton.getX(), pauseButton.getY(), null);
        g2d.drawImage(restartButton.getImage(), restartButton.getX(), restartButton.getY(), null);
        g2d.drawImage(quitButton.getImage(), quitButton.getX(), quitButton.getY(), null);
        
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        for (int i = 0; i < m_vFingers.size(); i++)
        {
        	int x = m_vFingers.get(i).getX();
        	int y = m_vFingers.get(i).getY();
        	
        	if (m_vFingers.get(i).isFill())
        		g2d.fillOval( x, y, m_iCircleDiameter, m_iCircleDiameter);
        	else
        		g2d.drawOval( x, y, m_iCircleDiameter, m_iCircleDiameter);
        }
    }
    
    class updateTask extends TimerTask
	{
    	State state;
    	
    	updateTask(State state) {this.state = state;}
		public void run()
		{
			if (m_State != State.PAUSE)
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
		
		if (pauseButton.isPressed(x, y))
    	{
			if (m_bIsPaused)
			{
				m_bIsPaused = false;
				m_State = m_RecoveryState;
			}
			else
			{
				m_bIsPaused = true;
				m_RecoveryState = m_State;
				m_State = State.PAUSE;
				System.out.println("PAUSE PRESSED");
			}
    	}
		else if (quitButton.isPressed(x, y))
		{
			System.exit(0);
		}
		else if (restartButton.isPressed(x,  y))
		{
			Reset();
		}
		else
		{
		
			switch(m_State)
	        {
		        case IDLE:
	        	{
	        		if (startButton.isPressed(x, y))
	        		{
	        			System.out.println("FINGER TRACKING");
	        			m_State = State.FINGER_TRACKING;
	        		}
	        		break;
	        	}
		        case FINGER_TRACKING:
		        {
		        	m_vFingers.addElement(new Finger(x-m_iCircleDiameter/2, y-m_iCircleDiameter/2));
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
		        case TRIAL_REST:
		        	if (pauseButton.isPressed(x, y))
		        	{
		    			m_State = State.PAUSE;
		    			System.out.println("PAUSE PRESSED");
		        	}
		        	break;
			default:
				break;
	        }
		}
		
		repaint();
	}
	
	private void clearCircles()
	{
		Graphics g;
		
		for (int i = 0; i < m_vFingers.size(); i++)
			m_vFingers.get(i).setFill(false);
		
		g = getGraphics();
		paint(g);
	}
	
	private void CheckClick(int x, int y, int fingerID)
	{
		int x1 = m_vFingers.get(fingerID).getX();
		int y1 = m_vFingers.get(fingerID).getY();
		int z = (int) Math.sqrt(Math.pow((x1+m_iCircleDiameter/2-x), 2) + Math.pow((y1+m_iCircleDiameter/2-y), 2));
		
		System.out.println("User clicked: (" + x + "," + y + ") and is " + z + " pixels off");
		if ( z < m_iCircleDiameter/2)
		{
			clearCircles();
			
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
					
					if (m_iCurrentTrial == 19)
					{
						m_State = State.INTERMISSION_REST;
						m_Timer.schedule(new updateTask(State.IN_TRIAL), 1000*60*2);
					}
					else
					{
						m_State = State.TRIAL_REST;
						m_Timer.schedule(new updateTask(State.IN_TRIAL), 5000);
					}
				}
			}
			else
			{
				correctSound.play();
				try {Thread.sleep((long)(Math.random()*500 + 500));}
				catch (InterruptedException e) {e.printStackTrace();}
				
				m_iCurrentTrialStep++;
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
