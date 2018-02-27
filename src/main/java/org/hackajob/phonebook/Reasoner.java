package org.hackajob.phonebook;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Reasoner {
	private int keyvalue;
	private State currentState, greetingState, listContactState, endState;
	private PhoneBook phonebook;
	private JTextPane txtpnConversation, txtpnInfo;
	private JPanel registerPanel, webBrowserPanel;
	private JScrollPane scrBackground;
	private String question;

	public Reasoner() {
		phonebook = new PhoneBook();
		Contact contact = new Contact();
		contact.setName("dava");
		contact.setAddress("addr");
		contact.setPhone_number("111");
		phonebook.addContact(contact);

		setupState();
		
		txtpnConversation = Gui.txtpnConversation;
		txtpnInfo = Gui.txtpnInfo;
		registerPanel = Gui.registerPanel;
		webBrowserPanel = Gui.webBrowserPanel;
		scrBackground = Gui.scrBackground;
	}
	
	public void setupState() {
		greetingState = new State();
		
		// GREETING
		greetingState.addKeyword(21, "list contacts");
		greetingState.addKeyword(21, "list contact");
		greetingState.addKeyword(2, "list");
		greetingState.addKeyword(13, "hi");
		greetingState.addKeyword(13, "hello");
		greetingState.addKeyword(14, "how are you");
		greetingState.addKeyword(0, "quit");

		// LIST CONTACTS
		listContactState = new State();
		listContactState.addKeyword(21, "yes");
		listContactState.addKeyword(12, "no");
		listContactState.addKeyword(21, "contacts");
		listContactState.addKeyword(21, "contact");
		listContactState.addKeyword(21, "list");
		listContactState.addKeyword(21, "display");
		listContactState.addKeyword(21, "list contacts");
		listContactState.addKeyword(21, "list all contacts");
		listContactState.addKeyword(21, "show all contacts");
		listContactState.addKeyword(21, "display all contacts");
		
		// WOULD YOU LIKE TO QUIT?
		endState = new State();
		endState.addKeyword(-100, "yes");
		endState.addKeyword(1, "no");
		endState.addKeyword(-100, "quit");
		
		// SET STARTING STATE
		currentState = greetingState;
	}

	public void reason(String aQuestion) {
		question = aQuestion;
		displayUserResponse(aQuestion);
		keyvalue = currentState.search(aQuestion);
		txtpnInfo.setText("");
		scrBackground.setViewportView(txtpnInfo);
		switch (keyvalue) {
		case 1:
			displayBotResponse("Ok then what else can I do for you?");
			currentState = greetingState;
			break;
		case 12:
			displayBotResponse("Ok what can I do for you?");
			currentState = greetingState;
			break;
		case 13:
			displayBotResponse("Hello there!!! How can I help you today?");
			currentState = greetingState;
			break;
		case 14:
			displayBotResponse("I am great!!! How can I help you today?");
			currentState = greetingState;
			break;
		case 15:
			displayUserResponse("Ok then what else can I do for you?");
			currentState = greetingState;
			break;

		// LIST MEMBERS
		case 2:
			displayBotResponse("Would you like to list all members?");
			currentState = listContactState;
			break;
		case 21:
			displayBotResponse("Listing contacts.");
			append(txtpnInfo, "" + "<style type='text/css' media='screen'> body { margin: 0; text-decoration: none; }"
					+ "body table { margin-left: 1px; font-family: verdana; font-size: 10px; color: #FFFF99;} .names { width: 200px;} table td { border: solid 1px black;} </style>"
					+ "<table border='1' cellspacing='10'>");
			for (Contact contact : phonebook.getContacts()) {
				append(txtpnInfo,
						"<tr> " + " <td class='names'>" + "Name: "+ contact.getName() 
								+ " <br/> Phone: " + contact.getPhone_number() 
								+ " <br/>  Address: " + contact.getAddress() 
								+ "</td></tr>");
			}
			append(txtpnInfo, "</table></body>");
			currentState = greetingState;
			break;


		// ERRORS AND QUIT
		case -1:
			displayBotResponse("Sorry I cannot understand your question...");
			break;
		case 0:
			displayBotResponse("Are you sure? Would you like to quit the program?");
			currentState = endState;
			break;
		case -100:
			System.exit(0);
			break;
		default:
			displayBotResponse("ERROR: Unknown state plz contact admin...");
			break;
		}

	}
	
	public void displayUserResponse(String response) {
		append(txtpnConversation,
				"<p style='color:white;font-size:13px;'><b>You: </b>" + response + "</p>");
	}
	
	public void displayBotResponse(String response) {
		append(txtpnConversation,
				"<p style='color:00FF33;font-size:13px;'><b>Gymbot: </b>" + response + "</p>");
	}
	
	public void append(JTextPane aTxtPane, String aHTML) {
		HTMLEditorKit kit = (HTMLEditorKit) aTxtPane.getEditorKit();
		HTMLDocument doc = (HTMLDocument) aTxtPane.getDocument();
		try {
			kit.insertHTML(doc, doc.getLength(), aHTML, 0, 0, null);
			txtpnConversation.setBackground(new Color(0, 0, 0, 100));
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
	}

}
