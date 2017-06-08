package gituser;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
/**
 * Main program
 * @author Kevin Jonathan
 * @version 1.1
 */
public class GitUser {
	/**
	 * The frame
	 */
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GitUser window = new GitUser();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GitUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("GitUser v1.1");
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		MainMenu panel_1 = new MainMenu(frame);
		//UserResult panel_1 = new UserResult();
		frame.getContentPane().add(panel_1, "main_menu");
	}
}
