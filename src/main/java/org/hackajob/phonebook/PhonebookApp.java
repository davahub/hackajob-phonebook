package org.hackajob.phonebook;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class PhonebookApp extends JFrame {
	private JPanel contentPane;
	private JTextField txtFieldQuest;
	private Color clrTransparent;
	private JButton btnSave;
	private JTextField txtFirstName, txtLastName, txtAge, txtExpiry, txtImage;
	private JLabel lblError;
	
	private static JTextPane txtpnConversation, txtpnInfo;
	private static JPanel registerPanel, webBrowserPanel;
	private static JScrollPane scrBackground;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PhonebookApp frame = new PhonebookApp();
				frame.setVisible(true);
				
				
			}
		});
	}
	
	private Client client;
	private WebTarget target;

	public PhonebookApp() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				txtFieldQuest.requestFocus();
			}
		});
		clrTransparent = new Color(0, 0, 0, 110);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 25, 1200, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane);
		layeredPane.setLayout(null);

		JLabel lblPicture = new JLabel("");
		
		final URL url = Thread.currentThread().getContextClassLoader().getResource("phonebook-bg.jpg");
		lblPicture.setIcon(new ImageIcon(url.getFile()));
		lblPicture.setBounds(0, 0, 1200, 700);
		layeredPane.add(lblPicture);

		JPanel panelCentre = new JPanel();
		panelCentre.setOpaque(false);
		layeredPane.setLayer(panelCentre, 1);
		panelCentre.setBounds(70, 75, 1050, 500);
		layeredPane.add(panelCentre);
		panelCentre.setLayout(new GridLayout(0, 2, 0, 0));

		JScrollPane scrConversation = new JScrollPane();
		scrConversation.setOpaque(false);
		panelCentre.add(scrConversation);
		scrConversation.setViewportBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255)), "Conversation",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 255, 255)));
		scrConversation.setAlignmentX(Component.RIGHT_ALIGNMENT);

		scrBackground = new JScrollPane();
		scrBackground.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrBackground.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrBackground.setOpaque(false);
		panelCentre.add(scrBackground);
		scrBackground.setViewportBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255)), "Information",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 255, 255)));
		scrBackground.setAlignmentX(Component.LEFT_ALIGNMENT);

		// TEXTPANE CONVERSATION
		txtpnConversation = new JTextPane() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(clrTransparent);
				Rectangle r = g.getClipBounds();
				g.fillRect(r.x, r.y, r.width, r.height);
				super.paintComponent(g);
			}
		};
		txtpnConversation.setOpaque(false);
		txtpnConversation.setEditable(false);
		txtpnConversation.setContentType("text/html");
		HTMLEditorKit kitConvers = new HTMLEditorKit();
		HTMLDocument docConvers = new HTMLDocument();
		txtpnConversation.setEditorKit(kitConvers);
		txtpnConversation.setDocument(docConvers);
		
		client = ClientBuilder.newClient();
	    target = client.target("http://www.mocky.io/v2/581335f71000004204abaf83");
	    ContactResponse response = target.request(MediaType.APPLICATION_JSON).get(ContactResponse.class);
	    
		try {
			kitConvers.insertHTML(docConvers, docConvers.getLength(),
					"<p style='color:00FF33;font-size:13px;'><b>Gymbot:</b> Hi, I am smart Phone bot how can I help?</p><br>"
					+ "<p>name: " + response.getContacts().get(0).getName() + "</p>",
					0, 0, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		scrConversation.getViewport().setOpaque(false);
		scrConversation.setViewportView(txtpnConversation);

		// WEB BROWSER
		webBrowserPanel = new JPanel();
		Dimension d = new Dimension(512, 475);
		webBrowserPanel.setLayout(new GridLayout(0, 1, 0, 0));

		// SETTING HOME PAGE
		scrBackground.getViewport().setOpaque(false);
		scrBackground.setViewportView(webBrowserPanel);

		// REGISTER PANEL
		registerPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(clrTransparent);
				Rectangle r = g.getClipBounds();
				g.fillRect(r.x, r.y, r.width, r.height);
				super.paintComponent(g);
			}
		};

		registerPanel.setLayout(null);
		registerPanel.setOpaque(false);
		registerPanel.setBounds(0, 0, 512, 475);

		txtFirstName = new JTextField();
		txtFirstName.setBorder(new LineBorder(new Color(255, 255, 102)));
		txtFirstName.setCaretColor(Color.WHITE);
		txtFirstName.setForeground(Color.WHITE);
		txtFirstName.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtFirstName.setOpaque(false);
		txtFirstName.setBounds(120, 30, 200, 25);
		registerPanel.add(txtFirstName);
		txtFirstName.setColumns(10);

		JLabel lblFirstName = new JLabel("Firstname:");
		lblFirstName.setForeground(new Color(255, 255, 102));
		lblFirstName.setFont(new Font("Verdana", Font.BOLD, 14));
		lblFirstName.setBounds(10, 34, 120, 18);
		registerPanel.add(lblFirstName);

		txtLastName = new JTextField();
		txtLastName.setCaretColor(Color.WHITE);
		txtLastName.setForeground(Color.WHITE);
		txtLastName.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtLastName.setOpaque(false);
		txtLastName.setBorder(new LineBorder(new Color(255, 255, 102)));
		txtLastName.setBounds(120, 70, 200, 25);
		registerPanel.add(txtLastName);
		txtLastName.setColumns(10);

		JLabel lblLastName = new JLabel("Lastname:");
		lblLastName.setFont(new Font("Verdana", Font.BOLD, 14));
		lblLastName.setForeground(new Color(255, 255, 102));
		lblLastName.setBounds(10, 74, 120, 18);
		registerPanel.add(lblLastName);

		txtAge = new JTextField();
		txtAge.setCaretColor(Color.WHITE);
		txtAge.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtAge.setForeground(Color.WHITE);
		txtAge.setOpaque(false);
		txtAge.setBorder(new LineBorder(new Color(255, 255, 102)));
		txtAge.setBounds(120, 110, 200, 25);
		registerPanel.add(txtAge);
		txtAge.setColumns(10);

		JLabel lblAge = new JLabel("Age:");
		lblAge.setFont(new Font("Verdana", Font.BOLD, 14));
		lblAge.setForeground(new Color(255, 255, 102));
		lblAge.setBounds(10, 114, 120, 18);
		registerPanel.add(lblAge);

		JLabel lblMemType = new JLabel("Membership Type:");
		lblMemType.setFont(new Font("Verdana", Font.BOLD, 14));
		lblMemType.setForeground(new Color(255, 255, 102));
		lblMemType.setBounds(10, 154, 150, 18);
		registerPanel.add(lblMemType);

		JRadioButton rdbtnGold = new JRadioButton("Gold");
		rdbtnGold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rdbtnGold.setFont(new Font("Verdana", Font.BOLD, 14));
		rdbtnGold.setForeground(Color.WHITE);
		rdbtnGold.setOpaque(false);
		rdbtnGold.setBounds(180, 153, 80, 23);
		registerPanel.add(rdbtnGold);

		JRadioButton rdbtnBronze = new JRadioButton("Bronze");
		rdbtnBronze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rdbtnBronze.setFont(new Font("Verdana", Font.BOLD, 14));
		rdbtnBronze.setForeground(Color.WHITE);
		rdbtnBronze.setOpaque(false);
		rdbtnBronze.setBounds(260, 153, 80, 23);
		registerPanel.add(rdbtnBronze);

		JRadioButton rdbtnSilver = new JRadioButton("Silver");
		rdbtnSilver.setSelected(true);
		rdbtnSilver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rdbtnSilver.setFont(new Font("Verdana", Font.BOLD, 14));
		rdbtnSilver.setForeground(Color.WHITE);
		rdbtnSilver.setOpaque(false);
		rdbtnSilver.setBounds(350, 153, 80, 23);
		registerPanel.add(rdbtnSilver);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnGold);
		group.add(rdbtnBronze);
		group.add(rdbtnSilver);

		txtExpiry = new JTextField();
		txtExpiry.setCaretColor(Color.WHITE);
		txtExpiry.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtExpiry.setForeground(Color.WHITE);
		txtExpiry.setOpaque(false);
		txtExpiry.setBorder(new LineBorder(new Color(255, 255, 102)));
		txtExpiry.setBounds(210, 190, 110, 25);
		registerPanel.add(txtExpiry);
		txtExpiry.setColumns(10);

		JLabel lblExpiry = new JLabel("Membership Expiry Date:");
		lblExpiry.setFont(new Font("Verdana", Font.BOLD, 14));
		lblExpiry.setForeground(new Color(255, 255, 102));
		lblExpiry.setBounds(10, 194, 200, 18);
		registerPanel.add(lblExpiry);

		txtImage = new JTextField();
		txtImage.setCaretColor(Color.WHITE);
		txtImage.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtImage.setForeground(Color.WHITE);
		txtImage.setOpaque(false);
		txtImage.setBorder(new LineBorder(new Color(255, 255, 102)));
		txtImage.setBounds(120, 230, 200, 25);
		registerPanel.add(txtImage);
		txtImage.setColumns(10);

		JLabel lblImage = new JLabel("Image:");
		lblImage.setFont(new Font("Verdana", Font.BOLD, 14));
		lblImage.setForeground(new Color(255, 255, 102));
		lblImage.setBounds(10, 234, 120, 18);
		registerPanel.add(lblImage);

		JButton btnImageSet = new JButton("Set");
		btnImageSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set up the file chooser.
				JFileChooser fc = new JFileChooser();
				// Show it.
				fc.showDialog(PhonebookApp.this, "Attach");
				File file = fc.getSelectedFile();
				txtImage.setText(file.getAbsolutePath());
			}
		});
		btnImageSet.setBounds(330, 231, 60, 25);
		registerPanel.add(btnImageSet);

		btnSave = new JButton("Save");
		btnSave.setBounds(202, 280, 117, 25);
		registerPanel.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstName = txtFirstName.getText().trim();
				String lastName = txtLastName.getText().trim();
				String tmpAge = txtAge.getText().trim();
				String txtDate = txtExpiry.getText().trim();
				String txtImagePath = txtImage.getText().trim();
				Date limitDate = customDate("31-12-2020");
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				if (!firstName.matches("^[a-zA-Z]+$")) {
					lblError.setVisible(true);
					lblError.setText("ERROR: Invalid firstname!");
				} else if (!lastName.matches("^[a-zA-Z]+$")) {
					lblError.setVisible(true);
					lblError.setText("ERROR: Invalid lastname!");
				} else if (!tmpAge.matches("^[0-9]+$")) {
					lblError.setVisible(true);
					lblError.setText("ERROR: Only number allowed in age field!");
				} else if (Integer.parseInt(tmpAge) < 18 || Integer.parseInt(tmpAge) > 150) {
					lblError.setVisible(true);
					lblError.setText("ERROR: Age must be between 18-150 range");
				} else if (!txtDate.matches("^[0-3][0-9][-][0-1][0-9]-[0-9][0-9][0-9][0-9]$")) {
					lblError.setVisible(true);
					lblError.setText("ERROR: Date format must be dd-mm-yyyy");
				} else if (customDate(txtDate).compareTo(now) == -1 || limitDate.compareTo(customDate(txtDate)) == -1) {
					lblError.setVisible(true);
					lblError.setText(
							"ERROR: Date must be between " + sdf.format(now) + " < --- < " + sdf.format(limitDate));
				} else {
					String[] parts = txtImagePath.split("/");
					String tmpImagePath = parts[parts.length - 1];

					if (txtImagePath.equals("") || txtImagePath.equals(tmpImagePath)) {
						txtImagePath = "noimage.jpg";
					} else {
						txtImagePath = tmpImagePath;
					}
					int age = Integer.parseInt(tmpAge);
//					member = new Member(id, firstName, lastName, age, memType, customDate(txtDate), txtImagePath);
//					reasoner.getGym().addMember(member);
					lblError.setVisible(true);
					lblError.setText("New member saved successfully!");
				}
			}
		});

		JLabel lblExpiryMsg = new JLabel("dd-mm-yyyy");
		lblExpiryMsg.setForeground(new Color(255, 255, 102));
		lblExpiryMsg.setBounds(330, 194, 150, 15);
		registerPanel.add(lblExpiryMsg);

		lblError = new JLabel("");
		lblError.setVisible(false);
		// lblError.setForeground(Color.WHITE);
		lblError.setForeground(Color.cyan);

		lblError.setFont(new Font("Verdana", Font.BOLD, 14));
		lblError.setBounds(10, 330, 600, 20);
		registerPanel.add(lblError);

		// TEXTPANE INFO
		txtpnInfo = new JTextPane() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(clrTransparent);
				Rectangle r = g.getClipBounds();
				g.fillRect(r.x, r.y, r.width, r.height);
				super.paintComponent(g);
			}
		};
		txtpnInfo.setOpaque(false);
		txtpnInfo.setEditable(false);
		txtpnInfo.setContentType("text/html");
		HTMLEditorKit kitInfo = new HTMLEditorKit();
		HTMLDocument docInfo = new HTMLDocument();
		txtpnInfo.setEditorKit(kitInfo);
		txtpnInfo.setDocument(docInfo);

		// TEXTFIELD QUESTION
		txtFieldQuest = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(clrTransparent);
				Rectangle rect = g.getClipBounds();
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
				super.paintComponent(g);
			}
		};

		txtFieldQuest.setCaretColor(Color.WHITE);
		txtFieldQuest.setForeground(Color.WHITE);
		txtFieldQuest.setFont(new Font("Dialog", Font.BOLD, 14));
		txtFieldQuest.setOpaque(false);
		txtFieldQuest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFieldQuest.setText("");
			}
		});

		layeredPane.setLayer(txtFieldQuest, 2);
		txtFieldQuest.setBounds(400, 590, 400, 28);
		layeredPane.add(txtFieldQuest);
		txtFieldQuest.setColumns(10);

		JLabel lblWelcome = new JLabel("Welcome to PhoneBook");
		lblWelcome.setForeground(new Color(0, 255, 128));
		lblWelcome.setFont(new Font("Arial", Font.BOLD, 35));
		layeredPane.setLayer(lblWelcome, 3);
		lblWelcome.setBounds(70, 18, 400, 45);
		layeredPane.add(lblWelcome);

		JLabel lblQuestion = new JLabel("Action");
		lblQuestion.setForeground(Color.WHITE);
		lblQuestion.setFont(new Font("Dialog", Font.BOLD, 16));
		layeredPane.setLayer(lblQuestion, 3);
		lblQuestion.setLabelFor(txtFieldQuest);
		lblQuestion.setBounds(310, 596, 150, 15);
		layeredPane.add(lblQuestion);

		JButton btnRegister = new JButton("Submit");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				reasoner.reason("register member");
			}
		});
		layeredPane.setLayer(btnRegister, 4);
		btnRegister.setBounds(830, 592, 117, 25);
		layeredPane.add(btnRegister);
	}
	
	public Date customDate(String dateString) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
		} catch (ParseException pe) {
			System.out.println("ERROR: could not parse date in string \"" + dateString + "\"");
		}
		return date;
	}
}
