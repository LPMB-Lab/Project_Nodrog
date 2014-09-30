import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;

import javax.swing.JPanel;

class drawWindow extends JPanel implements MouseListener
{
	Dimension size;
	Insets insets;
	RenderingHints rh;
	
	State m_State;
	Vector<Trial> m_vGeneratedTrials;
	Vector<Finger> m_vFingers;
	
	int windowWidth;
	int windowHeight;
	Timer m_Timer;
	
	public drawWindow()
	{
		m_vFingers = new Vector<Finger>();
		m_vGeneratedTrials = new Vector<Trial>();
		addMouseListener(this);
		
		Reset();
	}
	
	private void Reset()
	{
		m_State = State.IDLE;
		m_Timer = new Timer();
		m_vGeneratedTrials.clear();
		m_vFingers.clear();

		for (int i = 0; i < 20; i++)
		{
			Trial myTrial = new Trial();
			m_vGeneratedTrials.add(myTrial);
		}
	}

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        System.out.println("TEST");
        
        switch(m_State)
        {
	        case IDLE: break;
	        case FINGER_TRACKING: break;
	        case INITIAL_COUNTDOWN:
	        	g2d.drawString("INITIAL COUNTDOWN...", 50, 50);
	        	break;
	        case IN_TRIAL: break;
	        case TRIAL_REST: break;
	        case COMPLETED: break;
        }

        g2d.setColor(Color.blue);
        
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
               RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setRenderingHints(rh);
        
        size = getSize();
        insets = getInsets();
        
        windowWidth = size.width - insets.left - insets.right;
        windowHeight = size.height - insets.top - insets.bottom;
        
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
			m_Timer.cancel();
			m_State = state;
			repaint();
		}
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
        		if (x < 50 && y < 50)
        		{
        			System.out.println("FINGER TRACKING");
        			m_State = State.FINGER_TRACKING;
        		}
        		else
        			System.out.println("IDLE");
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
	        case INITIAL_COUNTDOWN:	break;
	        case IN_TRIAL:
	        {
        		break;
        	}
	        case TRIAL_REST:
	        {
        		break;
        	}
	        case COMPLETED:
	        {
        		break;
        	}
	        default:
	        {
	        	if (x < 50 && y < 50)
        			m_State = State.FINGER_TRACKING;
        		break;
	        }
        }
		
		if (x < 50 && y < 50)
			m_State = State.FINGER_TRACKING;
		
		System.out.println(x + " " + y);
		repaint();
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