package gituser;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * User search result GUI
 * @author Kevin Jonathan
 */
@SuppressWarnings("serial")
public class UserResult extends JPanel {
	final static int DEFAULT_PAGE_SIZE = 30;
	private JFrame frame;
	private String url;
	private long curPage;
	private long totalPage;
	private long totalFound;
	private JLabel lblPageOf;
	private boolean detail;
	private JLabel status;

	/**
	 * Create the panel.
	 */
	public UserResult(JFrame f, String u, JSONObject page1, boolean d) {
		totalFound = (long) ((JSONObject) page1.get("item")).get("total_count");
		totalPage = (int) page1.get("page_number");
		frame = f;
		url = u;
		curPage = 1;
		detail = d;
		setPreferredSize(new Dimension(500, 400));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{111, 118, 254, 0};
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
		gbc_TotalFound.gridwidth = 2;
		gbc_TotalFound.insets = new Insets(5, 5, 5, 5);
		gbc_TotalFound.gridx = 0;
		gbc_TotalFound.gridy = 1;
		add(TotalFound, gbc_TotalFound);
		
		status = new JLabel("Search took too long to finish");
		GridBagConstraints gbc_status = new GridBagConstraints();
		gbc_status.anchor = GridBagConstraints.NORTHEAST;
		gbc_status.insets = new Insets(5, 5, 5, 5);
		gbc_status.gridx = 2;
		gbc_status.gridy = 1;
		add(status, gbc_status);
		
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
		
		showResult(page1, content);
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

		Action refresh = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public void actionPerformed(ActionEvent e) {
				refresh(content);
            }
        };
        int temp = JComponent.WHEN_IN_FOCUSED_WINDOW;
        bindKeyStroke(temp, "refresh", KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), refresh);
        bindKeyStroke(temp, "refresh", KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), refresh);
	}
	
	public void showResult(JSONObject data, JPanel content) {
		long startCount = (curPage-1)*DEFAULT_PAGE_SIZE+1;
		long finishCount = startCount + ((JSONArray) ((JSONObject) data.get("item")).get("items")).size();
		if ((boolean) ((JSONObject) data.get("item")).get("incomplete_results")) {
			status.setVisible(true);
		}
		else {

			status.setVisible(false);
		}
		
		for (long i = startCount; i < finishCount; i++) {
			String link = (String) ((JSONObject) ((JSONArray) ((JSONObject) data.get("item")).get("items")).get((int) (i-startCount))).get("url");
			JSONObject obj;
			try {
				User user;
				if (detail) {
					HTTPSender http = new HTTPSender();
					obj = (JSONObject) http.getRequest(link);
					user = new User(frame, (JSONObject) obj.get("item"), detail);
				}
				else {
					user = new User(frame, (JSONObject) ((JSONArray) ((JSONObject) data.get("item")).get("items")).get((int) (i-startCount)), detail);
				}
				GridBagConstraints gbc_user = new GridBagConstraints();
				gbc_user.insets = new Insets(5, 10, 5, 10);
				gbc_user.fill = GridBagConstraints.BOTH;
				gbc_user.gridx = 0;
				gbc_user.gridy = (int) (i-startCount);
				content.add(user, gbc_user);
				System.out.println(i+" : "+((JSONObject) ((JSONArray) ((JSONObject) data.get("item")).get("items")).get((int) (i-startCount))).get("login"));
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
			HTTPSender http = new HTTPSender();
			page = (JSONObject) http.getRequest(url+"&page="+curPage);
			showResult(page, content);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * Binds keyboard button
	 * @param condition main condition
	 * @param name action name
	 * @param keyStroke pressed key
	 * @param action executed action
	 */
    protected void bindKeyStroke(int condition, String name, KeyStroke keyStroke, Action action) {
    	getInputMap(condition).put(keyStroke, name);
        getActionMap().put(name, action);
    }
}
