package gituser;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Repository search result GUI
 * @author Kevin Jonathan
 */
@SuppressWarnings("serial")
public class RepoResult extends JPanel {

	/**
	 * Default page size
	 */
	final static int DEFAULT_PAGE_SIZE = 30;
	/**
	 * The frame
	 */
	private JFrame frame;
	/**
	 * Current page
	 */
	private long curPage;
	/**
	 * Total page
	 */
	private long totalPage;
	/**
	 * Page information
	 */
	private JLabel lblPageOf;
	/**
	 * User information
	 */
	private JSONObject user;
	/**
	 * Scroll pane
	 */
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public RepoResult(JFrame f, JSONObject s) {
		frame = f;
		curPage = 1;
		user = (JSONObject) s.get("item");
		totalPage = ((long) user.get("public_repos")-1)/30+1;
		System.out.println(totalPage);
		setPreferredSize(new Dimension(500, 500));
		
		prepareGUI();
	}
	
	/**
	 * Show user's repository
	 * @param data List of repository
	 * @param content Panel for showing result
	 */
	public void showResult(JSONArray data, JPanel content) {
		try {
			long startCount = (curPage-1)*DEFAULT_PAGE_SIZE+1;
			long finishCount = startCount + data.size();
			
			for (long i = startCount; i < finishCount; i++) {
				try {
					Repo repo = new Repo((JSONObject) data.get((int) (i-startCount)));
					GridBagConstraints gbc_repo = new GridBagConstraints();
					if (i == startCount) {
						gbc_repo.insets = new Insets(10, 10, 10, 10);
					}
					else {
						gbc_repo.insets = new Insets(0, 10, 10, 10);
					}
					gbc_repo.fill = GridBagConstraints.BOTH;
					gbc_repo.gridx = 0;
					gbc_repo.gridy = (int) (i-startCount);
					content.add(repo, gbc_repo);
					System.out.println(i+" : "+((JSONObject) data.get((int) (i-startCount))).get("name"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Refresh panel content
	 * @param content Panel
	 */
	public void refresh(JPanel content) {
		content.removeAll();
		lblPageOf.setText("Page "+curPage+" of "+totalPage);
		JSONArray page;
		try {
			HTTPSender http = new HTTPSender();
			page = (JSONArray) ((JSONObject) http.getRequest(user.get("repos_url").toString()+"?page="+curPage)).get("item");
			showResult(page, content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Preparing user information GUI
	 */
	private void prepareUserDataGUI() {
		JPanel userInfo = new JPanel();
		GridBagConstraints gbc_userInfo = new GridBagConstraints();
		gbc_userInfo.gridwidth = 3;
		gbc_userInfo.insets = new Insets(5, 5, 5, 5);
		gbc_userInfo.fill = GridBagConstraints.BOTH;
		gbc_userInfo.gridx = 0;
		gbc_userInfo.gridy = 1;
		add(userInfo, gbc_userInfo);
		//panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagLayout gbl_userInfo = new GridBagLayout();
		gbl_userInfo.columnWidths = new int[]{0, 0, 0, 0};
		gbl_userInfo.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_userInfo.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_userInfo.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		userInfo.setLayout(gbl_userInfo);
		
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
		gbc_pic.gridheight = 4;
		gbc_pic.anchor = GridBagConstraints.CENTER;
		gbc_pic.insets = new Insets(0, 10, 0, 10);
		gbc_pic.gridx = 0;
		gbc_pic.gridy = 0;
		userInfo.add(pic, gbc_pic);
		
		JLabel username = new JLabel(user.get("login").toString());
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.gridwidth = 2;
		gbc_username.anchor = GridBagConstraints.SOUTHWEST;
		gbc_username.insets = new Insets(0, 5, 0, 0);
		gbc_username.gridx = 1;
		gbc_username.gridy = 0;
		userInfo.add(username, gbc_username);
		
		JLabel nama;
		if (user.get("name") == null) {
			nama = new JLabel("-");
		}
		else {
			nama = new JLabel(user.get("name").toString());
		}
		GridBagConstraints gbc_nama = new GridBagConstraints();
		gbc_nama.gridwidth = 2;
		gbc_nama.anchor = GridBagConstraints.WEST;
		gbc_nama.insets = new Insets(5, 5, 0, 0);
		gbc_nama.gridx = 1;
		gbc_nama.gridy = 1;
		userInfo.add(nama, gbc_nama);
		
		JLabel email;
		if (user.get("email") == null) {
			email = new JLabel("-");
		}
		else {
			email = new JLabel(user.get("email").toString());
		}
		GridBagConstraints gbc_email = new GridBagConstraints();
		gbc_email.gridwidth = 2;
		gbc_email.anchor = GridBagConstraints.WEST;
		gbc_email.insets = new Insets(5, 5, 0, 0);
		gbc_email.gridx = 1;
		gbc_email.gridy = 2;
		userInfo.add(email, gbc_email);
		
		JLabel repo = new JLabel("Repositories : "+user.get("public_repos").toString());
		GridBagConstraints gbc_repo = new GridBagConstraints();
		gbc_repo.anchor = GridBagConstraints.NORTHWEST;
		gbc_repo.insets = new Insets(5, 5, 0, 0);
		gbc_repo.gridx = 1;
		gbc_repo.gridy = 3;
		userInfo.add(repo, gbc_repo);
		
		JLabel follower = new JLabel("Followers : "+user.get("followers").toString());
		GridBagConstraints gbc_follower = new GridBagConstraints();
		gbc_follower.anchor = GridBagConstraints.NORTHWEST;
		gbc_follower.insets = new Insets(5, 5, 0, 0);
		gbc_follower.gridx = 2;
		gbc_follower.gridy = 3;
		userInfo.add(follower, gbc_follower);
	}
	
	/**
	 * Preparing GUI
	 */
	private void prepareGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 118, 254, 0};
		gridBagLayout.rowHeights = new int[]{83, 80, 145, 36, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
		prepareUserDataGUI();
		////////////////////////////////////////////////////////////////////////

		/////////////////////////////Content////////////////////////////////////
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		JPanel content = new JPanel();
		scrollPane.setViewportView(content);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		GridBagLayout gbl_content = new GridBagLayout();
		gbl_content.columnWidths = new int[]{471, 0};
		gbl_content.rowHeights = new int[]{0, 0};
		gbl_content.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_content.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		content.setLayout(gbl_content);
		
		JSONArray data;
		try {
			HTTPSender http = new HTTPSender();
			data = (JSONArray) ((JSONObject) http.getRequest(user.get("repos_url").toString())).get("item");
			showResult(data, content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		////////////////////////////////////////////////////////////////////////
		

		//////////////////////////Page navigation///////////////////////////////
		JPanel pageNavigation = new JPanel();
		GridBagConstraints gbc_pageNavigation = new GridBagConstraints();
		gbc_pageNavigation.gridwidth = 3;
		gbc_pageNavigation.insets = new Insets(10, 10, 10, 10);
		gbc_pageNavigation.fill = GridBagConstraints.BOTH;
		gbc_pageNavigation.gridx = 0;
		gbc_pageNavigation.gridy = 3;
		add(pageNavigation, gbc_pageNavigation);
		GridBagLayout gbl_pageNavigation = new GridBagLayout();
		gbl_pageNavigation.columnWidths = new int[]{0, 0, 112, 0, 0, 0};
		gbl_pageNavigation.rowHeights = new int[]{0, 0};
		gbl_pageNavigation.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_pageNavigation.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pageNavigation.setLayout(gbl_pageNavigation);
		
		JButton first = new JButton("<<");
		GridBagConstraints gbc_first = new GridBagConstraints();
		gbc_first.anchor = GridBagConstraints.EAST;
		gbc_first.insets = new Insets(0, 2, 0, 2);
		gbc_first.gridx = 0;
		gbc_first.gridy = 0;
		pageNavigation.add(first, gbc_first);
		first.setVisible(false);
		
		JButton prev = new JButton("<");
		GridBagConstraints gbc_prev = new GridBagConstraints();
		gbc_prev.anchor = GridBagConstraints.EAST;
		gbc_prev.insets = new Insets(0, 2, 0, 2);
		gbc_prev.gridx = 1;
		gbc_prev.gridy = 0;
		pageNavigation.add(prev, gbc_prev);
		prev.setVisible(false);
		
		lblPageOf = new JLabel("Page 1 of "+totalPage);
		GridBagConstraints gbc_lblPageOf = new GridBagConstraints();
		gbc_lblPageOf.insets = new Insets(0, 2, 0, 2);
		gbc_lblPageOf.gridx = 2;
		gbc_lblPageOf.gridy = 0;
		pageNavigation.add(lblPageOf, gbc_lblPageOf);
		
		JButton next = new JButton(">");
		GridBagConstraints gbc_next = new GridBagConstraints();
		gbc_next.insets = new Insets(0, 2, 0, 2);
		gbc_next.anchor = GridBagConstraints.WEST;
		gbc_next.gridx = 3;
		gbc_next.gridy = 0;
		pageNavigation.add(next, gbc_next);
		
		JButton last = new JButton(">>");
		GridBagConstraints gbc_last = new GridBagConstraints();
		gbc_last.insets = new Insets(0, 2, 0, 2);
		gbc_last.anchor = GridBagConstraints.WEST;
		gbc_last.gridx = 4;
		gbc_last.gridy = 0;
		pageNavigation.add(last, gbc_last);

		first.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curPage = 1;
				last.setVisible(true);
				next.setVisible(true);
				first.setVisible(false);
				prev.setVisible(false);
				refresh(content);
				repaint();
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
				repaint();
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
				repaint();
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
				repaint();
			}
		});
		if (totalPage <= 1) {
			next.setVisible(false);
			last.setVisible(false);
		}
		////////////////////////////////////////////////////////////////////////
	}
	
	/**
	 * Overriding paint component
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scrollPane.getVerticalScrollBar().setValue(0);
	}
}
