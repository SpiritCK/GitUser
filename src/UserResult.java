import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Panel;

public class UserResult extends JPanel {
	final static int DEFAULT_PAGE_SIZE = 30;
	private JFrame frame;
	private String url;
	private int curPage;
	private long totalPage;
	private long totalFound;

	/**
	 * Create the panel.
	 */
	public UserResult(JFrame f, String u, JSONObject page1) {
		totalFound = (long) page1.get("total_count");
		totalPage = (totalFound - 1)/DEFAULT_PAGE_SIZE + 1;
		frame = f;
		url = u;
		curPage = 1;
		setPreferredSize(new Dimension(500, 400));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 68, 304, 0};
		gridBagLayout.rowHeights = new int[]{83, 19, 246, 36, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
				cardLayout.show(frame.getContentPane(), "main_menu");
			}
		});
		btnBack.setVerticalAlignment(SwingConstants.TOP);
		btnBack.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnBack.insets = new Insets(5, 5, 5, 5);
		gbc_btnBack.gridx = 0;
		gbc_btnBack.gridy = 0;
		add(btnBack, gbc_btnBack);
		
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
		//JLabel logo = new JLabel("Logo");
		GridBagConstraints gbc_logo = new GridBagConstraints();
		gbc_logo.fill = GridBagConstraints.BOTH;
		gbc_logo.insets = new Insets(5, 5, 5, 5);
		gbc_logo.gridx = 2;
		gbc_logo.gridy = 0;
		add(logo, gbc_logo);
		
		JLabel TotalFound = new JLabel("Search result : "+totalFound+" match");
		GridBagConstraints gbc_TotalFound = new GridBagConstraints();
		gbc_TotalFound.anchor = GridBagConstraints.NORTHWEST;
		gbc_TotalFound.gridwidth = 3;
		gbc_TotalFound.insets = new Insets(5, 5, 5, 5);
		gbc_TotalFound.gridx = 0;
		gbc_TotalFound.gridy = 1;
		add(TotalFound, gbc_TotalFound);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		/////////////////////////////Content////////////////////////////////////
		JPanel content = new JPanel();
		scrollPane.setViewportView(content);
		GridBagLayout gbl_content = new GridBagLayout();
		gbl_content.columnWidths = new int[]{471, 0};
		gbl_content.rowHeights = new int[]{100, 100, 100, 0};
		gbl_content.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_content.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		content.setLayout(gbl_content);
		
		int startCount = (curPage-1)*30+1;
		for (int i = startCount; i < (startCount + 29 < totalFound ? startCount+30 : totalFound+1); i++) {
			String link = (String) ((JSONObject) ((JSONArray) page1.get("items")).get(i-startCount)).get("url");
			JSONObject obj;
			try {
				obj = getRequest(link);
				User user = new User(obj);
				GridBagConstraints gbc_user = new GridBagConstraints();
				gbc_user.insets = new Insets(5, 10, 5, 10);
				gbc_user.gridx = 0;
				gbc_user.gridy = i-startCount;
				content.add(user, gbc_user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		////////////////////////////////////////////////////////////////////////
		

		//////////////////////////Page navigation///////////////////////////////
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 3;
		gbc_panel_1.insets = new Insets(10, 10, 10, 10);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{194, 112, 194, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton button = new JButton("<");
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.EAST;
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 0;
		panel_1.add(button, gbc_button);
		
		JLabel lblPageOf = new JLabel("Page 1 of "+totalPage);
		GridBagConstraints gbc_lblPageOf = new GridBagConstraints();
		gbc_lblPageOf.insets = new Insets(0, 0, 0, 5);
		gbc_lblPageOf.gridx = 1;
		gbc_lblPageOf.gridy = 0;
		panel_1.add(lblPageOf, gbc_lblPageOf);
		
		JButton button_1 = new JButton(">");
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.anchor = GridBagConstraints.WEST;
		gbc_button_1.gridx = 2;
		gbc_button_1.gridy = 0;
		panel_1.add(button_1, gbc_button_1);
		////////////////////////////////////////////////////////////////////////
	}

	private JSONObject getRequest(String url) throws Exception {
		String token ="b4565a9f29f8c0d8689dd1ea6357b9cd0d8932f9";
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestProperty("Authorization", "token " + token);

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
		
	    
	    return obj3;
	}
}
