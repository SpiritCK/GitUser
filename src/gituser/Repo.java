package gituser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.json.simple.JSONObject;
import javax.swing.JTextPane;
import javax.swing.UIManager;

/**
 * Repository GUI
 * @author Kevin Jonathan
 */
@SuppressWarnings("serial")
public class Repo extends JPanel {
	/**
	 * Default font
	 */
	private static final Font DEFAULT_FONT;
	static {
        Font font = UIManager.getFont("Label.font");
        DEFAULT_FONT = (font != null) ? font: new Font("Tahoma", Font.PLAIN, 11);
    }
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
	 * Shorten repository description
	 * @param original Original description
	 * @return Shortened description
	 */
	public static String refineDescription(String original) {
		if (original.length() <= 110) {
			return original;
		}
		else {
			int i = 109;
			while (original.charAt(i) != ' ') {
				i--;
			}
			String result = new String();
			for (int j = 0; j <= i; j++) {
				result += original.charAt(j);
			}
			result += " ...";
			return result;
		}
	}
	
	/**
	 * Preparing GUI
	 */
	private void prepareGUI() {
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setPreferredSize(new Dimension(450, 100));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel judul = new JLabel(data.get("name").toString());
		GridBagConstraints gbc_judul = new GridBagConstraints();
		gbc_judul.anchor = GridBagConstraints.WEST;
		gbc_judul.insets = new Insets(5, 5, 2, 0);
		gbc_judul.gridx = 0;
		gbc_judul.gridy = 0;
		add(judul, gbc_judul);
		
		JTextPane repo = new JTextPane();
		repo.setText("Link :\n"+data.get("html_url"));
		repo.setBackground(null);
		repo.setEditable(false);
		//repo.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		repo.setFont(DEFAULT_FONT);
		GridBagConstraints gbc_repo = new GridBagConstraints();
		gbc_repo.insets = new Insets(2, 2, 2, 0);
		gbc_repo.fill = GridBagConstraints.BOTH;
		gbc_repo.gridx = 0;
		gbc_repo.gridy = 1;
		add(repo, gbc_repo);
		
		JTextPane description = new JTextPane();
		description.setBackground(null);
		description.setEditable(false);
		if (data.get("description") != null) {
			description.setText(refineDescription(data.get("description").toString()));
		}
		else {
			description.setText("-");
		}
		GridBagConstraints gbc_description = new GridBagConstraints();
		gbc_description.anchor = GridBagConstraints.NORTHWEST;
		gbc_description.insets = new Insets(2, 2, 0, 0);
		gbc_description.fill = GridBagConstraints.BOTH;
		gbc_description.gridx = 0;
		gbc_description.gridy = 2;
		add(description, gbc_description);
	}
}
