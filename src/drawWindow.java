import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class drawWindow extends JPanel
{
	public drawWindow()
	{
		
	}

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