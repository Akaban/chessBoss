package chessBot;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


public class SimpleRecenterTest extends JComponent {

	
	static Point mouseLoc;
	static JFrame frame;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Point location; // location relative to where the mouse cursor starts


	static AbstractAction action = new AbstractAction() {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public void actionPerformed(ActionEvent e) {
	        Point mLoc = MouseInfo.getPointerInfo().getLocation();
	        Rectangle bounds = frame.getBounds();

	        // Test to make sure the mouse is inside the window
	        if(bounds.contains(mLoc)){
	            Point winLoc = bounds.getLocation();
	            mouseLoc = new Point(mLoc.x - winLoc.x, mLoc.y - winLoc.y);
	            System.out.println("Mloc x :" + mouseLoc.x + " Mloc y:" + mouseLoc.y);
	        }

	    }
	};
	
	public void game()
	{
		// We add binding to the RootPane 
		JRootPane rootPane = frame.getRootPane();

		//Specify the KeyStroke and give the action a name
		KeyStroke KEY = KeyStroke.getKeyStroke("control S");
		String actionName = "captureMouseLoc";

		//map the keystroke to the actionName
		rootPane.getInputMap().put(KEY, actionName);

		//map the actionName to the action itself
		rootPane.getActionMap().put(actionName, action);
	}
	
	public SimpleRecenterTest() {
		location = new Point();
		this.frame = new JFrame();
		frame.setUndecorated(true);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setVisible(true);
		frame.setOpacity(0.10f);

		}



		// Test it
		public static void main(String[] args) {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					SimpleRecenterTest srt = new SimpleRecenterTest();
					srt.frame.setVisible(true);
					// We add binding to the RootPane 
					JRootPane rootPane = frame.getRootPane();

					//Specify the KeyStroke and give the action a name
					KeyStroke KEY = KeyStroke.getKeyStroke("control S");
					String actionName = "captureMouseLoc";

					//map the keystroke to the actionName
					rootPane.getInputMap().put(KEY, actionName);

					//map the actionName to the action itself
					rootPane.getActionMap().put(actionName, action);

				}
			});
		}
	}