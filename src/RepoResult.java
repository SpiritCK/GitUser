import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("serial")
public class RepoResult extends JPanel {

	final static int DEFAULT_PAGE_SIZE = 30;
	private JFrame frame;
	private String url;
	private long curPage;
	private long totalPage;
	private long totalFound;
	private JLabel lblPageOf;
	private JLabel status;

	/**
	 * Create the panel.
	 */
	public RepoResult(JFrame f, String u, JSONObject user) {
		//totalFound = (long) page1.get("total_count");
		//totalPage = (int) page1.get("page_number");
		frame = f;
		url = u;
		curPage = 1;
		setPreferredSize(new Dimension(500, 400));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 118, 254, 0};
		gridBagLayout.rowHeights = new int[]{83, 120, 145, 36, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
				cardLayout.show(frame.getContentPane(), "user_result");
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
		

		/////////////////////////////User data//////////////////////////////////
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		//panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 300, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel pic;
		try {
			String path = user.get("avatar_url").toString();
		    URL url = new URL(path);
		    BufferedImage image = ImageIO.read(url);
			pic = new JLabel(new ImageIcon(image.getScaledInstance(80,  80, Image.SCALE_DEFAULT)));
		} catch (IOException e1) {
			pic = new JLabel();
			e1.printStackTrace();
		}
		pic.setBorder(new LineBorder(new Color(0, 0, 0)));
		pic.setPreferredSize(new Dimension(80, 80));
		GridBagConstraints gbc_pic = new GridBagConstraints();
		gbc_pic.gridheight = 7;
		gbc_pic.anchor = GridBagConstraints.CENTER;
		gbc_pic.insets = new Insets(0, 10, 0, 10);
		gbc_pic.gridx = 0;
		gbc_pic.gridy = 0;
		panel.add(pic, gbc_pic);
		
		JLabel username = new JLabel(user.get("login").toString());
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.anchor = GridBagConstraints.WEST;
		gbc_username.insets = new Insets(8, 5, 0, 0);
		gbc_username.gridx = 1;
		gbc_username.gridy = 0;
		panel.add(username, gbc_username);
		
		JLabel nama;
		if (user.get("name") == null) {
			nama = new JLabel("-");
		}
		else {
			nama = new JLabel(user.get("name").toString());
		}
		GridBagConstraints gbc_nama = new GridBagConstraints();
		gbc_nama.anchor = GridBagConstraints.WEST;
		gbc_nama.insets = new Insets(5, 5, 0, 0);
		gbc_nama.gridx = 1;
		gbc_nama.gridy = 1;
		panel.add(nama, gbc_nama);
		
		JLabel email;
		if (user.get("email") == null) {
			email = new JLabel("-");
		}
		else {
			email = new JLabel(user.get("email").toString());
		}
		GridBagConstraints gbc_email = new GridBagConstraints();
		gbc_email.anchor = GridBagConstraints.WEST;
		gbc_email.insets = new Insets(5, 5, 0, 0);
		gbc_email.gridx = 1;
		gbc_email.gridy = 2;
		panel.add(email, gbc_email);
		
		JLabel repo = new JLabel("Repositories : "+user.get("public_repos").toString());
		GridBagConstraints gbc_repo = new GridBagConstraints();
		gbc_repo.anchor = GridBagConstraints.WEST;
		gbc_repo.insets = new Insets(5, 5, 0, 0);
		gbc_repo.gridx = 1;
		gbc_repo.gridy = 3;
		panel.add(repo, gbc_repo);
		
		JLabel follower = new JLabel("Followers : "+user.get("followers").toString());
		GridBagConstraints gbc_follower = new GridBagConstraints();
		gbc_follower.anchor = GridBagConstraints.WEST;
		gbc_follower.insets = new Insets(5, 5, 5, 0);
		gbc_follower.gridx = 1;
		gbc_follower.gridy = 4;
		panel.add(follower, gbc_follower);
		////////////////////////////////////////////////////////////////////////
		
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
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		GridBagLayout gbl_content = new GridBagLayout();
		gbl_content.columnWidths = new int[]{471, 0};
		gbl_content.rowHeights = new int[]{0, 0};
		gbl_content.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_content.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		content.setLayout(gbl_content);
		
		//showResult(page1, content);
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
		gbl_panel_1.columnWidths = new int[]{0, 0, 112, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton first = new JButton("<<");
		GridBagConstraints gbc_first = new GridBagConstraints();
		gbc_first.anchor = GridBagConstraints.EAST;
		gbc_first.insets = new Insets(0, 2, 0, 2);
		gbc_first.gridx = 0;
		gbc_first.gridy = 0;
		panel_1.add(first, gbc_first);
		first.setVisible(false);
		
		JButton prev = new JButton("<");
		GridBagConstraints gbc_prev = new GridBagConstraints();
		gbc_prev.anchor = GridBagConstraints.EAST;
		gbc_prev.insets = new Insets(0, 2, 0, 2);
		gbc_prev.gridx = 1;
		gbc_prev.gridy = 0;
		panel_1.add(prev, gbc_prev);
		prev.setVisible(false);
		
		lblPageOf = new JLabel("Page 1 of "+totalPage);
		GridBagConstraints gbc_lblPageOf = new GridBagConstraints();
		gbc_lblPageOf.insets = new Insets(0, 2, 0, 2);
		gbc_lblPageOf.gridx = 2;
		gbc_lblPageOf.gridy = 0;
		panel_1.add(lblPageOf, gbc_lblPageOf);
		
		JButton next = new JButton(">");
		GridBagConstraints gbc_next = new GridBagConstraints();
		gbc_next.insets = new Insets(0, 2, 0, 2);
		gbc_next.anchor = GridBagConstraints.WEST;
		gbc_next.gridx = 3;
		gbc_next.gridy = 0;
		panel_1.add(next, gbc_next);
		
		JButton last = new JButton(">>");
		GridBagConstraints gbc_last = new GridBagConstraints();
		gbc_last.insets = new Insets(0, 2, 0, 2);
		gbc_last.anchor = GridBagConstraints.WEST;
		gbc_last.gridx = 4;
		gbc_last.gridy = 0;
		panel_1.add(last, gbc_last);

		first.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curPage = 1;
				last.setVisible(true);
				next.setVisible(true);
				first.setVisible(false);
				prev.setVisible(false);
				refresh(content);
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curPage = totalPage;
				last.setVisible(false);
				next.setVisible(false);
				first.setVisible(true);
				prev.setVisible(true);
				refresh(content);
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curPage--;
				if (!next.isVisible()) {
					next.setVisible(true);
					last.setVisible(true);
				}
				if (curPage <= 1) {
					prev.setVisible(false);
					first.setVisible(false);
				}
				refresh(content);
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curPage++;
				if (!prev.isVisible()) {
					prev.setVisible(true);
					first.setVisible(true);
				}
				if (curPage >= totalPage) {
					next.setVisible(false);
					last.setVisible(false);
				}
				refresh(content);
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		if (totalPage <= 1) {
			next.setVisible(false);
			last.setVisible(false);
		}
		////////////////////////////////////////////////////////////////////////

		/*Action refresh = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public void actionPerformed(ActionEvent e) {
				refresh(content);
            }
        };
        int temp = JComponent.WHEN_IN_FOCUSED_WINDOW;
        bindKeyStroke(temp, "refresh", KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), refresh);
        bindKeyStroke(temp, "refresh", KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), refresh);*/
	}

	private Object getRequest(String url) throws Exception {
		String token ="b4565a9f29f8c0d8689dd1ea6357b9cd0d8932f9";
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestProperty("Authorization", "token " + token);

		conn.setRequestMethod("GET");
		//int responseCode = conn.getResponseCode();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
		
		in.close();
		JSONParser parser = new JSONParser();
		Object obj2 = parser.parse(response.toString());
		System.out.println(obj2);
		
	    return obj2;
	}
	
	public void showResult(JSONObject data, JPanel content) {
		long startCount = (curPage-1)*DEFAULT_PAGE_SIZE+1;
		long finishCount = startCount + ((JSONArray) data.get("items")).size();
		if ((boolean) data.get("incomplete_results")) {
			status.setVisible(true);
		}
		else {

			status.setVisible(false);
		}
		
		for (long i = startCount; i < finishCount; i++) {
			String link = (String) ((JSONObject) ((JSONArray) data.get("items")).get((int) (i-startCount))).get("url");
			JSONObject obj;
			try {
				User user;
				/*if (detail) {
					obj = (JSONObject) getRequest(link);
					user = new User(obj, detail);
				}
				else {
					user = new User((JSONObject) ((JSONArray) data.get("items")).get((int) (i-startCount)), detail);
				}*/
				GridBagConstraints gbc_user = new GridBagConstraints();
				gbc_user.insets = new Insets(5, 10, 5, 10);
				gbc_user.fill = GridBagConstraints.BOTH;
				gbc_user.gridx = 0;
				gbc_user.gridy = (int) (i-startCount);
				//content.add(user, gbc_user);
				System.out.println(i+" : "+((JSONObject) ((JSONArray) data.get("items")).get((int) (i-startCount))).get("login"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void refresh(JPanel content) {
		content.removeAll();
		lblPageOf.setText("Page "+curPage+" of "+totalPage);
		JSONObject page;
		try {
			page = (JSONObject) getRequest(url+"&page="+curPage);
			showResult(page, content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
}
