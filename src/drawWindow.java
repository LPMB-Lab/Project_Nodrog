import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Button;
import models.Finger;
import models.State;
import models.Trial;

class drawWindow extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int m_iRectangleDimension = 100;
	private static final int STATE_POSITION = 105;
	private static final int TOTAL_TRIALS = 20;

	Dimension screenSize;
	RenderingHints rh;

	State m_State;
	State m_RecoveryState;
	Vector<Trial> m_vGeneratedTrials;
	Vector<Finger> m_vFingers;
	Trial m_CurrentTrial;
	int m_iCurrentTrial;
	int m_iCurrentTrialStep;
	boolean m_bIsPaused;

	Timer m_Timer;
	Button startButton;
	Button pauseButton;
	Button restartButton;
	Button quitButton;
	Button saveButton;

	AudioClip correctSound;

	long m_lStartTime;
	int m_iGlobalTimer;

	public drawWindow() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		addMouseListener(this);

		m_vFingers = new Vector<Finger>();
		m_vGeneratedTrials = new Vector<Trial>();

		try {
			startButton = new Button(ImageIO.read(getClass().getResource(
					"images/startButton.png")), 5, 5);
			pauseButton = new Button(ImageIO.read(getClass().getResource(
					"images/pauseButton.png")), 100, 5);
			restartButton = new Button(ImageIO.read(getClass().getResource(
					"images/restartButton.png")), 195, 5);
			quitButton = new Button(ImageIO.read(getClass().getResource(
					"images/quitButton.png")), 290, 5);
			saveButton = new Button(ImageIO.read(getClass().getResource(
					"images/saveButton.png")), 385, 5);
			correctSound = Applet.newAudioClip(getClass().getResource(
					"sounds/correctSound.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Reset();
	}

	private void Reset() {
		if (m_Timer != null)
			m_Timer.cancel();

		m_State = State.IDLE;
		m_Timer = new Timer();
		m_vGeneratedTrials.clear();
		m_vFingers.clear();
		m_iCurrentTrial = 0;
		m_iCurrentTrialStep = 0;
		m_bIsPaused = false;
		m_iGlobalTimer = 0;

		for (int i = 0; i < TOTAL_TRIALS; i++) {
			Trial myTrial = new Trial();
			m_vGeneratedTrials.add(myTrial);
		}
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);

		g2d.setColor(Color.blue);
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));

		switch (m_State) {
		case PAUSE:
			g2d.drawString("PAUSED", 5, STATE_POSITION);
			break;
		case IDLE:
			g2d.drawImage(startButton.getImage(), startButton.getX(),
					startButton.getY(), null);
			break;
		case FINGER_TRACKING:
			g2d.drawString("FINGER TRACKING", 5, STATE_POSITION);
			break;
		case COUNTDOWN:
			g2d.drawString("Countdown to begin in " + m_iGlobalTimer / 1000
					+ " seconds", 5, STATE_POSITION);
		case IN_TRIAL:
			break;
		case COMPLETED:
			g2d.drawString(
					"The test is complete! Thank you for participating!", 5,
					STATE_POSITION);
			break;
		}

		g2d.drawString("CURRENT TRIAL: " + (m_iCurrentTrial + 1) + "/" + TOTAL_TRIALS, 5, 75);
		g2d.drawImage(pauseButton.getImage(), pauseButton.getX(),
				pauseButton.getY(), null);
		g2d.drawImage(restartButton.getImage(), restartButton.getX(),
				restartButton.getY(), null);
		g2d.drawImage(quitButton.getImage(), quitButton.getX(),
				quitButton.getY(), null);
		g2d.drawImage(saveButton.getImage(), saveButton.getX(),
				saveButton.getY(), null);

		for (int i = 0; i < m_vFingers.size(); i++) {
			int x = m_vFingers.get(i).getX();
			int y = m_vFingers.get(i).getY();

			if (m_vFingers.get(i).isFill())
				g2d.fillRect(x, y, m_iRectangleDimension,
						m_iRectangleDimension * 3);
			// g2d.fillOval( x, y, m_iCircleDiameter, m_iCircleDiameter);
			else
				g2d.drawRect(x, y, m_iRectangleDimension,
						m_iRectangleDimension * 3);
			// g2d.drawOval( x, y, m_iCircleDiameter, m_iCircleDiameter);
		}
	}

	class updateTask extends TimerTask {
		State goToState;

		updateTask(State state) {
			goToState = state;
		}

		public void run() {
			if (m_State != State.PAUSE && m_State != State.COUNTDOWN) {
				m_State = goToState;
				System.out.println("STATE IS: " + m_State);
			}

			if (m_State == State.COUNTDOWN) {
				if (m_iGlobalTimer == 0) {
					m_State = goToState;
				} else {
					m_Timer.schedule(new updateTask(State.IN_TRIAL), 500);
					m_iGlobalTimer -= 500;
				}
			}

			if (m_State == State.IN_TRIAL) {
				m_lStartTime = new Date().getTime();
				updateTrial();
			}

			UpdateGraphics();
		}
	}

	private void updateTrial() {
		m_CurrentTrial = m_vGeneratedTrials.get(m_iCurrentTrial);
		int fingerIndex = m_CurrentTrial.getCurrentFinger(m_iCurrentTrialStep);
		fingerIndex = fingerIndex + (fingerIndex < 0 ? 4 : 3);
		m_vFingers.get(fingerIndex).setFill(true);
		repaint();
	}

	private void countDownToState(int timer, State state) {
		m_iGlobalTimer = timer;
		m_State = State.COUNTDOWN;
		m_Timer.schedule(new updateTask(state), 500);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (startButton.isPressed(x, y)) {
			StartSimulation();
		} else if (pauseButton.isPressed(x, y)) {
			PauseSimulation();
		} else if (quitButton.isPressed(x, y)) {
			QuitSimulation();
		} else if (restartButton.isPressed(x, y)) {
			Reset();
		} else if (saveButton.isPressed(x, y)) {
			ExportFile();
		} else if (m_State == State.FINGER_TRACKING) {
			if (m_vFingers.size() >= 8)
				countDownToState(5000, State.IN_TRIAL);
			else
				m_vFingers.addElement(new Finger(x - m_iRectangleDimension / 2,
						y - m_iRectangleDimension * 5 / 2));
		}

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (m_State != State.IN_TRIAL) {
			repaint();
			return;
		}

		int fingerIndex = m_vGeneratedTrials.get(m_iCurrentTrial)
				.getCurrentFinger(m_iCurrentTrialStep);

		for (int i = 0; i < m_vFingers.size(); i++) {
			if (m_vFingers.get(i).isPressed(x, y)) {
				fingerIndex = fingerIndex + (fingerIndex < 0 ? 4 : 3);
				
				if (i == fingerIndex) {
					RegisterCorrectFingerPress();
				} else {
					m_vGeneratedTrials.get(m_iCurrentTrial).setErrorFinger(i);
				}
				break;
			}
		}

		repaint();
	}
	
	private void RegisterCorrectFingerPress() {
		long lEndTime = new Date().getTime();
		long diffTime = lEndTime - m_lStartTime;

		m_CurrentTrial = m_vGeneratedTrials.get(m_iCurrentTrial);
		m_CurrentTrial.setTimer(m_iCurrentTrialStep, diffTime);

		clearFingers();

		if (m_iCurrentTrialStep == 19) {
			if (m_iCurrentTrial == TOTAL_TRIALS-1) {
				m_State = State.COMPLETED;
				ExportFile();
			} else {
				m_iCurrentTrial++;
				m_iCurrentTrialStep = 0;

				if (m_iCurrentTrial == TOTAL_TRIALS/2-1)
					countDownToState(60000, State.IN_TRIAL);
				else
					countDownToState(5000, State.IN_TRIAL);
			}
		} else {
			correctSound.play();
			try {
				Thread.sleep((long) (Math.random() * 500 + 500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			m_iCurrentTrialStep++;
			updateTrial();
		}
		m_lStartTime = new Date().getTime();
	}

	private void clearFingers() {
		for (int i = 0; i < m_vFingers.size(); i++)
			m_vFingers.get(i).setFill(false);
		UpdateGraphics();
	}

	private void UpdateGraphics() {
		Graphics g;
		g = getGraphics();
		paint(g);
	}

	private void QuitSimulation() {
		int dialogResult = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Warning",
				JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	private void StartSimulation() {
		m_State = State.FINGER_TRACKING;
	}

	private void PauseSimulation() {
		if (m_bIsPaused) {
			m_bIsPaused = false;
			if (m_RecoveryState == State.COUNTDOWN)
				countDownToState(5000, State.IN_TRIAL);
			else
				m_State = m_RecoveryState;
		} else {
			m_bIsPaused = true;
			m_RecoveryState = m_State;
			m_State = State.PAUSE;
		}
	}

	private void ExportFile() {
		JTextField fileNameInput = new JTextField();
		String CompletionString = "Please enter File Name";

		if (m_State != State.COMPLETED)
			CompletionString += " (Trial is Unfinished)";

		final JComponent[] inputs = new JComponent[] {
				new JLabel(CompletionString), fileNameInput };
		int dialogResult = JOptionPane.showConfirmDialog(null, inputs,
				"Save File", JOptionPane.OK_CANCEL_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {
			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy_MM_dd HH_mm_ss");
				Date date = new Date();
				String fileName = "";

				if (fileNameInput.getText().equals(""))
					fileName = dateFormat.format(date) + "_NON_NAMED_TRIAL"
							+ ".xls";
				else
					fileName = dateFormat.format(date) + "_"
							+ fileNameInput.getText() + ".xls";

				PrintWriter writer = new PrintWriter(fileName, "US-ASCII");
				
				String endl = "\r\n";
				String tabl = "\t";
				String exportString = tabl;
				
				exportString += "Average Interswitch" + tabl;
				exportString += "Average Intraswitch" + tabl;
				exportString += "Average Homologous InterSwitch" + tabl;
				exportString += "Average Non-Homologous InterSwitch" + tabl;
				exportString += "Average Left To Right" + tabl;
				exportString += "Average Right To Left" + tabl;
				exportString += "Fastest Time" + endl;

				for (int i = 0; i < m_vGeneratedTrials.size(); i++) {
					exportString += "TRIAL #" + (i + 1) + tabl;
					exportString += m_vGeneratedTrials.get(i).ExportTrial();
				}
				
				for (int i = 0; i < 4; i++) {
					exportString += endl;
				}
				
				exportString += tabl;
				exportString += "Left Pinky" + tabl;
				exportString += "Left Ring" + tabl;
				exportString += "Left Middle" + tabl;
				exportString += "Left Index" + tabl;
				exportString += "Right Index" + tabl;
				exportString += "Right Middle" + tabl;
				exportString += "Right Ring" + tabl;
				exportString += "Right Pinky" + endl;
				
				for (int i = 0; i < m_vGeneratedTrials.size(); i++) {
					exportString += "TRIAL #" + (i + 1) + tabl;
					exportString += m_vGeneratedTrials.get(i).ExportErrorTrial();
				}
				
				for (int i = 0; i < 4; i++) {
					exportString += endl;
				}
				
				for (int i = 0; i < m_vGeneratedTrials.size(); i++) {
					exportString += "TRIAL #" + (i + 1) + endl;
					exportString += m_vGeneratedTrials.get(i).ExportTrialRaw();
				}

				writer.println(exportString);
				writer.close();

				JOptionPane.showMessageDialog(null, "Save Successful!",
						"Save Success", JOptionPane.PLAIN_MESSAGE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
