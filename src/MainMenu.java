import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainMenu extends JPanel {
	

	private JTextField keyword;
	private JTextField FollowValue;
	private JTextField RepoValue;
	private JComboBox FollowCompare;
	private JComboBox RepoCompare;
	private JComboBox SearchBy;
	private JCheckBox FollowFilter;
	private JCheckBox RepoFilter;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public MainMenu() {
		setPreferredSize(new Dimension(500, 400));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 68, 304, 0};
		gridBagLayout.rowHeights = new int[]{83, 19, 24, 15, 24, 24, 33, 28, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Back to main menu\n");
			}
		});
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
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.anchor = GridBagConstraints.CENTER;
		gbc_btnSearch.insets = new Insets(30, 5, 5, 5);
		gbc_btnSearch.gridwidth = 3;
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 6;
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
	
	public void Search() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = new String("https://api.github.com/search/users?q=");
				boolean clear = true;
				if (!keyword.getText().isEmpty()) {
					clear = false;
					for (int i = 0; i < keyword.getText().length(); i++) {
						if (keyword.getText().charAt(i) == ' ') {
							url += "+";
						}
						else {
							url += keyword.getText().charAt(i);
						}
					}
					
					url += "+in%3A";
					if (SearchBy.getSelectedIndex() == 0) {
						url += "login";
					}
					else if (SearchBy.getSelectedIndex() == 1) {
						url += "fullname";
					}
					else {
						url += "email";
					}
				}
				
				if (RepoFilter.isSelected() && !RepoValue.getText().isEmpty()) {
					if (!clear) {
						url += "+";
					}
					else {
						clear = false;
					}
					if (RepoCompare.getSelectedIndex() == 0) {
						url += "repos%3A<" + RepoValue.getText();
					}
					else if (RepoCompare.getSelectedIndex() == 1) {
						url += "repos%3A<%3D" + RepoValue.getText();
					}
					else if (RepoCompare.getSelectedIndex() == 2) {
						url += "repos%3A>" + RepoValue.getText();
					}
					else {
						url += "repos%3A>%3D" + RepoValue.getText();
					}
				}
				
				if (FollowFilter.isSelected() && !FollowValue.getText().isEmpty()) {
					if (!clear) {
						url += "+";
					}
					else {
						clear = false;
					}
					if (FollowCompare.getSelectedIndex() == 0) {
						url += "follower%3A<" + FollowValue.getText();
					}
					else if (FollowCompare.getSelectedIndex() == 1) {
						url += "follower%3A<%3D" + FollowValue.getText();
					}
					else if (FollowCompare.getSelectedIndex() == 2) {
						url += "follower%3A>" + FollowValue.getText();
					}
					else {
						url += "follower%3A>%3D" + FollowValue.getText();
					}
				}

				if (!clear) {
					url += "&";
				}
				else {
					clear = false;
				}
				url += "type=Users";
				try {
					getRequest(url);
				} catch (Exception e) {
					System.out.println("request error");
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void getRequest(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		conn.setRequestMethod("GET");
		int responseCode = conn.getResponseCode();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
		
		in.close();
		JSONParser parser = new JSONParser();
		Object obj2 = parser.parse(response.toString());
		System.out.println(obj2);
		JSONObject obj3 = (JSONObject) obj2;
	    System.out.println("Total count : "+obj3.get("total_count"));
	    int total = Integer.parseInt(obj3.get("total_count").toString());
	    if (total > 0) {
	    	JSONArray array = (JSONArray) obj3.get("items");
	    	System.out.println("Users :");
	    	for (int i = 0; i < total; i++) {
	    		System.out.println(i+": "+((JSONObject) array.get(i)).get("login"));
	    	}
	    }
	}
	
	/**
	 * mengikat tombol keyboard dengan aksi
	 * @param condition kondisi utama
	 * @param name nama aksi
	 * @param keyStroke tombol yang ditekan
	 * @param action aksi yang dijalankan
	 */
    protected void bindKeyStroke(int condition, String name, KeyStroke keyStroke, Action action) {
    	getInputMap(condition).put(keyStroke, name);
        getActionMap().put(name, action);
    }
}
