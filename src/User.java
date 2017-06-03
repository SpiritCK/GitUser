import java.awt.Dimension;

import javax.swing.JPanel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.GridBagLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.LineBorder;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("serial")
public class User extends JPanel {
	private JSONObject data;
	private boolean detail;

	/**
	 * Create the panel.
	 */
	public User(JFrame frame, JSONObject obj, boolean d) {
		detail = d;
		data = obj;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					System.out.println("Enter "+data.get("login")+"'s repository");
					try {
						JSONObject result = (JSONObject) getRequest((String) data.get("url"));
						RepoResult panel_1 = new RepoResult(frame, result);
						frame.getContentPane().add(panel_1, "repo_result");
						CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
						cardLayout.show(frame.getContentPane(), "repo_result");
					} catch (Exception e) {
						System.out.println("request error");
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		//setPreferredSize(new Dimension(450, 120));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 300, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		if (detail) {
			JLabel pic;
			try {
				String path = data.get("avatar_url").toString();
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
			gbc_pic.insets = new Insets(10, 10, 10, 10);
			gbc_pic.gridx = 0;
			gbc_pic.gridy = 0;
			add(pic, gbc_pic);
		}
		
		JLabel username = new JLabel(data.get("login").toString());
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.anchor = GridBagConstraints.WEST;
		if (detail) {
			gbc_username.insets = new Insets(10, 5, 0, 0);
		}
		else {
			gbc_username.insets = new Insets(10, 5, 10, 10);
		}
		gbc_username.gridx = 1;
		gbc_username.gridy = 0;
		add(username, gbc_username);
		
		if (detail) {
			JLabel nama;
			if (data.get("name") == null) {
				nama = new JLabel("-");
			}
			else {
				nama = new JLabel(data.get("name").toString());
			}
			GridBagConstraints gbc_nama = new GridBagConstraints();
			gbc_nama.anchor = GridBagConstraints.WEST;
			gbc_nama.insets = new Insets(5, 5, 0, 0);
			gbc_nama.gridx = 1;
			gbc_nama.gridy = 1;
			add(nama, gbc_nama);
			
			JLabel email;
			if (data.get("email") == null) {
				email = new JLabel("-");
			}
			else {
				email = new JLabel(data.get("email").toString());
			}
			GridBagConstraints gbc_email = new GridBagConstraints();
			gbc_email.anchor = GridBagConstraints.WEST;
			gbc_email.insets = new Insets(5, 5, 0, 0);
			gbc_email.gridx = 1;
			gbc_email.gridy = 2;
			add(email, gbc_email);
			
			JLabel repo = new JLabel("Repositories : "+data.get("public_repos").toString());
			GridBagConstraints gbc_repo = new GridBagConstraints();
			gbc_repo.anchor = GridBagConstraints.WEST;
			gbc_repo.insets = new Insets(5, 5, 0, 0);
			gbc_repo.gridx = 1;
			gbc_repo.gridy = 3;
			add(repo, gbc_repo);
			
			JLabel follower = new JLabel("Followers : "+data.get("followers").toString());
			GridBagConstraints gbc_follower = new GridBagConstraints();
			gbc_follower.anchor = GridBagConstraints.WEST;
			gbc_follower.insets = new Insets(5, 5, 10, 0);
			gbc_follower.gridx = 1;
			gbc_follower.gridy = 4;
			add(follower, gbc_follower);
		}
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
		
		return obj2;
		/*JSONObject obj3 = new JSONObject();
		obj3.put("items", obj2);

		Map<String, List<String>> map = conn.getHeaderFields();

		System.out.println("Printing Response Header...\n");
		int pageNumber = 1;

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey()
	                           + " ,Value : " + entry.getValue());
			if (entry.getKey() != null && entry.getKey().toString().equals("Link")) {
				Pattern pattern = Pattern.compile("rel=\"next\"(?:.*)page=(.*?)>; rel=\"last\"");
				Matcher matcher = pattern.matcher(entry.getValue().toString());

		        List<String> listMatches = new ArrayList<String>();

		        while(matcher.find())
		        {
		            listMatches.add(matcher.group(1));
		        }

		        pageNumber = Integer.parseInt(listMatches.get(0));
			}
		}
	    
		obj3.put("page_number", pageNumber);
		System.out.println(obj3);
	    return obj3;*/
	}

}
