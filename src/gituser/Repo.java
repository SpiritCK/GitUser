package gituser;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONObject;
import javax.swing.border.BevelBorder;
import java.awt.Font;

/**
 * Repository GUI
 * @author Kevin Jonathan
 */
@SuppressWarnings("serial")
public class Repo extends JPanel {
	/**
	 * Repository information
	 */
	private JSONObject data;

	/**
	 * Create the panel.
	 */
	public Repo(JSONObject obj) {
		data = obj;
		prepareGUI();
	}
	
	/**
	 * Preparing GUI
	 */
	private void prepareGUI() {
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
			        Desktop.getDesktop().browse(new URL(data.get("html_url").toString()).toURI());
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
		});
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setPreferredSize(new Dimension(450, 80));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel judul = new JLabel(data.get("name").toString());
		GridBagConstraints gbc_judul = new GridBagConstraints();
		gbc_judul.anchor = GridBagConstraints.WEST;
		gbc_judul.insets = new Insets(5, 5, 5, 0);
		gbc_judul.gridx = 0;
		gbc_judul.gridy = 0;
		add(judul, gbc_judul);
		
		JLabel repo = new JLabel(data.get("html_url").toString());
		GridBagConstraints gbc_repo = new GridBagConstraints();
		gbc_repo.insets = new Insets(5, 5, 5, 0);
		gbc_repo.fill = GridBagConstraints.BOTH;
		gbc_repo.gridx = 0;
		gbc_repo.gridy = 1;
		add(repo, gbc_repo);
		
		JLabel description = new JLabel("asd");
		description.setFont(new Font("Dialog", Font.PLAIN, 12));
		if (data.get("description") != null) {
			description.setText(data.get("description").toString());
		}
		else {
			description.setText("-");
		}
		GridBagConstraints gbc_description = new GridBagConstraints();
		gbc_description.anchor = GridBagConstraints.NORTHWEST;
		gbc_description.insets = new Insets(5, 5, 5, 5);
		gbc_description.fill = GridBagConstraints.BOTH;
		gbc_description.gridx = 0;
		gbc_description.gridy = 2;
		add(description, gbc_description);
	}
}
