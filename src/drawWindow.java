import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JPanel;

class drawWindow extends JPanel implements MouseListener
{
	Dimension size;
	Insets insets;
	RenderingHints rh;
	
	State m_State;
	Vector<Trial> m_vGeneratedTrials;
	Finger m_aFingers[];
	
	int windowWidth;
	int windowHeight;
	
	public drawWindow()
	{
		m_aFingers = new Finger[8];
		m_vGeneratedTrials = new Vector<Trial>();
		m_State = State.IDLE;
		addMouseListener(this);
	}
	
	private void GenerateTrials()
	{	
		for (int i = 0; i < 20; i++)
		{
			Trial myTrial = new Trial();
			m_vGeneratedTrials.add(myTrial);
		}
	}

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        switch(m_State)
        {
	        case IDLE: break;
	        case FINGER_TRACKING: break;
	        case INITIAL_COUNTDOWN: break;
	        case IN_TRIAL: break;
	        case TRIAL_REST: break;
	        case COMPLETED: break;
        }

        //g2d.drawString("Java 2D", 50, 50);
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
        
        for (int i = 1; i < 10; i++)
        {
        	g2d.drawOval(i*windowWidth/9, 7*windowHeight/9,50,50);
        }

        //g2d.fillOval(270, 130, 50, 50);
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
	public void mousePressed(MouseEvent e)
	{
		switch(m_State)
        {
	        case IDLE: break;
	        case FINGER_TRACKING: break;
	        case INITIAL_COUNTDOWN: break;
	        case IN_TRIAL: break;
	        case TRIAL_REST: break;
	        case COMPLETED: break;
        }
		
		int x = e.getX();
		int y = e.getY();
		
		if (x < 50 && y < 50)
			m_State = State.FINGER_TRACKING;
		
		System.out.println(x + " " + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}
}