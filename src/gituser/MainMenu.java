package gituser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.json.simple.JSONObject;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Main menu GUI
 * @author Kevin Jonathan
 */
@SuppressWarnings("serial")
public class MainMenu extends JPanel {

	/**
	 * The frame
	 */
	private JFrame frame;
	/**
	 * Search keyword
	 */
	JTextField keyword;
	/**
	 * Number of follower
	 */
	JTextField FollowValue;
	/**
	 * Number of repository
	 */
	JTextField RepoValue;
	/**
	 * Comparison type for follower
	 */
	@SuppressWarnings("rawtypes")
	JComboBox FollowCompare;
	/**
	 * Comparison type for repository
	 */
	@SuppressWarnings("rawtypes")
	JComboBox RepoCompare;
	/**
	 * Search base
	 */
	@SuppressWarnings("rawtypes")
	JComboBox SearchBy;
	/**
	 * Follower filter on/off
	 */
	JCheckBox FollowFilter;
	/**
	 * Repository filter on/off
	 */
	JCheckBox RepoFilter;
	/**
	 * sho detailed result on/off
	 */
	private JCheckBox detail;

	/**
	 * Create the panel.
	 */
	public MainMenu(JFrame f) {
		frame = f;
		prepareGUI();
	}
	
	/**
	 * Generate search query
	 * @return query
	 */
	public String generateQuery() {
		String query = new String("https://api.github.com/search/users?q=");
		boolean clear = true;
		if (!keyword.getText().isEmpty()) {
			clear = false;
			for (int i = 0; i < keyword.getText().length(); i++) {
				if (keyword.getText().charAt(i) == ' ') {
					query += "+";
				}
				else {
					query += keyword.getText().charAt(i);
				}
			}
			
			query += "+in%3A";
			if (SearchBy.getSelectedIndex() == 0) {
				query += "login";
			}
			else if (SearchBy.getSelectedIndex() == 1) {
				query += "fullname";
			}
			else {
				query += "email";
			}
		}
		
		if (RepoFilter.isSelected() && !RepoValue.getText().isEmpty()) {
			if (!clear) {
				query += "+";
			}
			else {
				clear = false;
			}
			if (RepoCompare.getSelectedIndex() == 0) {
				query += "repos%3A<" + RepoValue.getText();
			}
			else if (RepoCompare.getSelectedIndex() == 1) {
				query += "repos%3A<%3D" + RepoValue.getText();
			}
			else if (RepoCompare.getSelectedIndex() == 2) {
				query += "repos%3A>" + RepoValue.getText();
			}
			else {
				query += "repos%3A>%3D" + RepoValue.getText();
			}
		}
		
		if (FollowFilter.isSelected() && !FollowValue.getText().isEmpty()) {
			if (!clear) {
				query += "+";
			}
			else {
				clear = false;
			}
			if (FollowCompare.getSelectedIndex() == 0) {
				query += "followers%3A<" + FollowValue.getText();
			}
			else if (FollowCompare.getSelectedIndex() == 1) {
				query += "followers%3A<%3D" + FollowValue.getText();
			}
			else if (FollowCompare.getSelectedIndex() == 2) {
				query += "followers%3A>" + FollowValue.getText();
			}
			else {
				query += "followers%3A>%3D" + FollowValue.getText();
			}
		}

		if (!clear) {
			query += "&";
		}
		else {
			clear = false;
		}
		query += "type=Users";
		return query;
	}
	
	/**
	 * Searching for user
	 */
	public void Search() {
		String url = generateQuery();
		try {
			System.out.println(url);
			HTTPSender http = new HTTPSender();
			JSONObject result = (JSONObject) http.getRequest(url);
			UserResult panel_1 = new UserResult(frame, url, result, detail.isSelected());
			frame.getContentPane().add(panel_1, "user_result");
			reset();
			CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
			cardLayout.show(frame.getContentPane(), "user_result");
		} catch (Exception e) {
			System.out.println("request error");
			e.printStackTrace();
		}
	}
	
	/**
	 * reset GUI
	 */
	public void reset() {
		keyword.setText("");
		SearchBy.setSelectedIndex(0);
		RepoFilter.setSelected(false);
		RepoCompare.setSelectedIndex(0);
		RepoValue.setText("");
		FollowFilter.setSelected(false);
		FollowCompare.setSelectedIndex(0);
		FollowValue.setText("");
		detail.setSelected(false);
	}

	/**
	 * Preparing GUI
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void prepareGUI() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				keyword.requestFocus();
			}
		});
		setPreferredSize(new Dimension(500, 400));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 68, 304, 0};
		gridBagLayout.rowHeights = new int[]{83, 19, 24, 15, 24, 24, 0, 33, 28, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel logo;
		BufferedImage x;
		try {
			x = ImageIO.read(new File("src/logo.png"));
			logo = new JLabel(new ImageIcon(((Image) x).getScaledInstance(169, 70, Image.SCALE_DEFAULT)));
		} catch (IOException e) {
			logo = new JLabel("No image");
		}
		logo.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_logo = new GridBagConstraints();
		gbc_logo.fill = GridBagConstraints.BOTH;
		gbc_logo.insets = new Insets(5, 5, 5, 5);
		gbc_logo.gridx = 2;
		gbc_logo.gridy = 0;
		add(logo, gbc_logo);
		
		JLabel label1 = new JLabel("Keyword :");
		label1.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.anchor = GridBagConstraints.WEST;
		gbc_label1.insets = new Insets(5, 5, 5, 5);
		gbc_label1.gridx = 0;
		gbc_label1.gridy = 1;
		add(label1, gbc_label1);
		
		keyword = new JTextField();
		keyword.setColumns(10);
		GridBagConstraints gbc_keyword = new GridBagConstraints();
		gbc_keyword.anchor = GridBagConstraints.NORTH;
		gbc_keyword.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyword.insets = new Insets(5, 5, 5, 5);
		gbc_keyword.gridwidth = 2;
		gbc_keyword.gridx = 1;
		gbc_keyword.gridy = 1;
		add(keyword, gbc_keyword);
		
		JLabel label2 = new JLabel("Search By :");
		label2.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.anchor = GridBagConstraints.WEST;
		gbc_label2.insets = new Insets(5, 5, 5, 5);
		gbc_label2.gridx = 0;
		gbc_label2.gridy = 2;
		add(label2, gbc_label2);
		
		SearchBy = new JComboBox();
		SearchBy.setModel(new DefaultComboBoxModel(new String[] {"Username", "Full Name", "Email"}));
		SearchBy.setSelectedIndex(0);
		GridBagConstraints gbc_SearchBy = new GridBagConstraints();
		gbc_SearchBy.anchor = GridBagConstraints.NORTH;
		gbc_SearchBy.fill = GridBagConstraints.HORIZONTAL;
		gbc_SearchBy.insets = new Insets(5, 5, 5, 5);
		gbc_SearchBy.gridwidth = 2;
		gbc_SearchBy.gridx = 1;
		gbc_SearchBy.gridy = 2;
		add(SearchBy, gbc_SearchBy);
		
		JLabel label3 = new JLabel("Filter");
		GridBagConstraints gbc_label3 = new GridBagConstraints();
		gbc_label3.anchor = GridBagConstraints.NORTHWEST;
		gbc_label3.insets = new Insets(5, 5, 5, 5);
		gbc_label3.gridx = 0;
		gbc_label3.gridy = 3;
		add(label3, gbc_label3);
		
		RepoFilter = new JCheckBox("Repository :");
		RepoFilter.setMnemonic(KeyEvent.VK_R);
		GridBagConstraints gbc_RepoFilter = new GridBagConstraints();
		gbc_RepoFilter.anchor = GridBagConstraints.NORTHWEST;
		gbc_RepoFilter.insets = new Insets(5, 5, 5, 5);
		gbc_RepoFilter.gridx = 0;
		gbc_RepoFilter.gridy = 4;
		add(RepoFilter, gbc_RepoFilter);
		
		RepoCompare = new JComboBox();
		RepoCompare.setModel(new DefaultComboBoxModel(new String[] {"<", "<=", ">", ">="}));
		GridBagConstraints gbc_RepoCompare = new GridBagConstraints();
		gbc_RepoCompare.anchor = GridBagConstraints.NORTH;
		gbc_RepoCompare.fill = GridBagConstraints.HORIZONTAL;
		gbc_RepoCompare.insets = new Insets(5, 5, 5, 5);
		gbc_RepoCompare.gridx = 1;
		gbc_RepoCompare.gridy = 4;
		add(RepoCompare, gbc_RepoCompare);
		
		RepoValue = new JTextField();
		RepoValue.setColumns(10);
		GridBagConstraints gbc_RepoValue = new GridBagConstraints();
		gbc_RepoValue.anchor = GridBagConstraints.WEST;
		gbc_RepoValue.insets = new Insets(5, 5, 5, 5);
		gbc_RepoValue.gridx = 2;
		gbc_RepoValue.gridy = 4;
		add(RepoValue, gbc_RepoValue);
		
		FollowFilter = new JCheckBox("Follower :");
		FollowFilter.setMnemonic(KeyEvent.VK_F);
		GridBagConstraints gbc_FollowFilter = new GridBagConstraints();
		gbc_FollowFilter.anchor = GridBagConstraints.NORTHWEST;
		gbc_FollowFilter.insets = new Insets(5, 5, 5, 5);
		gbc_FollowFilter.gridx = 0;
		gbc_FollowFilter.gridy = 5;
		add(FollowFilter, gbc_FollowFilter);
		
		FollowCompare = new JComboBox();
		FollowCompare.setModel(new DefaultComboBoxModel(new String[] {"<", "<=", ">", ">="}));
		GridBagConstraints gbc_FollowCompare = new GridBagConstraints();
		gbc_FollowCompare.anchor = GridBagConstraints.NORTH;
		gbc_FollowCompare.fill = GridBagConstraints.HORIZONTAL;
		gbc_FollowCompare.insets = new Insets(5, 5, 5, 5);
		gbc_FollowCompare.gridx = 1;
		gbc_FollowCompare.gridy = 5;
		add(FollowCompare, gbc_FollowCompare);
		
		FollowValue = new JTextField();
		FollowValue.setColumns(10);
		GridBagConstraints gbc_FollowValue = new GridBagConstraints();
		gbc_FollowValue.anchor = GridBagConstraints.WEST;
		gbc_FollowValue.insets = new Insets(5, 5, 5, 5);
		gbc_FollowValue.gridx = 2;
		gbc_FollowValue.gridy = 5;
		add(FollowValue, gbc_FollowValue);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setPreferredSize(new Dimension(100, 30));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Search();
			}
		});
		
		detail = new JCheckBox("Show detailed result (may cause the program to run slowly)");
		detail.setMnemonic(KeyEvent.VK_S);
		GridBagConstraints gbc_detail = new GridBagConstraints();
		gbc_detail.anchor = GridBagConstraints.NORTHWEST;
		gbc_detail.gridwidth = 3;
		gbc_detail.insets = new Insets(15, 5, 5, 5);
		gbc_detail.gridx = 0;
		gbc_detail.gridy = 6;
		add(detail, gbc_detail);
		
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.anchor = GridBagConstraints.CENTER;
		gbc_btnSearch.insets = new Insets(30, 5, 5, 5);
		gbc_btnSearch.gridwidth = 3;
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 7;
		add(btnSearch, gbc_btnSearch);
		
		Action search = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public void actionPerformed(ActionEvent e) {
				Search();
            }
        };
        int temp = JComponent.WHEN_IN_FOCUSED_WINDOW;
        bindKeyStroke(temp, "inc.xp", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), search);
	}
	
	/**
	 * Binds keyboard button
	 * @param condition main condition
	 * @param name action name
	 * @param keyStroke pressed key
	 * @param action executed action
	 */
    private void bindKeyStroke(int condition, String name, KeyStroke keyStroke, Action action) {
    	getInputMap(condition).put(keyStroke, name);
        getActionMap().put(name, action);
    }
}
