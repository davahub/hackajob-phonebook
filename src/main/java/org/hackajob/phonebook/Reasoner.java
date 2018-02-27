package org.hackajob.phonebook;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Reasoner {
	private State currentState, 
	greetingState, 
	listContactState,
	searchContactState,
	searchContactActionState,
	sortContactState,
	endState;
	private PhoneBook phonebook;

	public Reasoner() {
		phonebook = new PhoneBook();
		setupState();
	}
	
	public void setupState() {
		greetingState = new State();
		
		// GREETING
		greetingState.addKeyword(21, "list contacts");
		greetingState.addKeyword(21, "list contact");
		greetingState.addKeyword(3, "search contact");
		greetingState.addKeyword(3, "search");
		greetingState.addKeyword(2, "list");
		greetingState.addKeyword(13, "hi");
		greetingState.addKeyword(13, "hello");
		greetingState.addKeyword(14, "how are you");
		greetingState.addKeyword(0, "quit");
		// SORT 
		greetingState.addKeyword(4, "sort by name");
		greetingState.addKeyword(41, "sort by telephone");
		greetingState.addKeyword(42, "sort by address");

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
		
		// SEARCH CONTACTS
		searchContactState = new State();
		searchContactState.addKeyword(31, "yes");
		searchContactState.addKeyword(15, "no");
		searchContactActionState = new State();
		searchContactActionState.addKeyword(21, "[]");
		
		// WOULD YOU LIKE TO QUIT?
		endState = new State();
		endState.addKeyword(-100, "yes");
		endState.addKeyword(1, "no");
		endState.addKeyword(-100, "quit");
		
		// SET STARTING STATE
		currentState = greetingState;
	}

	public void reason(String aQuestion) {
		displayUserResponse(aQuestion);
		int keyvalue = currentState.search(aQuestion);
		if (currentState.equals(searchContactActionState)) {
			keyvalue = 32;
		}
		Gui.txtpnInfo.setText("");
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

		// LIST CONTACTS
		case 2:
			displayBotResponse("Would you like to list all members?");
			currentState = listContactState;
			break;
		case 21:
			displayBotResponse("Listing contacts.");
			displayContacts(phonebook.getContacts());
			currentState = greetingState;
			break;
			
		// SEARCH CONTACT	
		case 3:
			displayBotResponse("Would you like to search all contacts?");
			currentState = searchContactState;
			break;	
		case 31:
			displayBotResponse("Enter search criteria?");
			currentState = searchContactActionState;
			break;
		case 32:
			displayBotResponse("Displaying search results.");
			displayContacts(phonebook.searchContacts(aQuestion));
			currentState = greetingState;
			break;	
			
		// SORT CONTACT	
		case 4:
			displayBotResponse("Sorting contacts by name");
			phonebook.sortByName();
			displayContacts(phonebook.getContacts());
			currentState = greetingState;
			break;	
		case 41:
			displayBotResponse("Sorting contacts by telephone");
			phonebook.sortByTelephone();
			displayContacts(phonebook.getContacts());
			currentState = greetingState;
			break;
		case 42:
			displayBotResponse("Sorting contacts by address");
			phonebook.sortByAddress();
			displayContacts(phonebook.getContacts());
			currentState = greetingState;
			break;	

		// ERRORS AND QUIT
		case -1:
			displayBotResponse("Sorry I cannot understand your question...");
			currentState = greetingState;
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
		append(Gui.txtpnConversation,
				"<p style='color:white;font-size:13px;'><b>You: </b>" + response + "</p>");
	}
	
	public void displayBotResponse(String response) {
		append(Gui.txtpnConversation,
				"<p style='color:00FF33;font-size:13px;'><b>Bot: </b>" + response + "</p>");
	}
	
	public void displayContacts(ArrayList<Contact> contacts) {
		append(Gui.txtpnInfo, "" + "<style type='text/css' media='screen'> body { margin: 0; text-decoration: none; }"
				+ "body table { margin-left: 1px; font-family: verdana; font-size: 12px; color: #FFFF99;} .names { width: 200px;} table td { border: solid 4px black;} </style>"
				+ "<table border='1' cellspacing='10'>");
		for (Contact contact : contacts) {
			append(Gui.txtpnInfo,
					"<tr> " + " <td class='names'>" + "Name: "+ contact.getName() 
							+ " <br/> Phone: " + contact.getPhone_number() 
							+ " <br/>  Address: " + contact.getAddress() 
							+ "</td></tr>");
		}
		append(Gui.txtpnInfo, "</table></body>");
	}
	
	public void append(JTextPane aTxtPane, String aHTML) {
		HTMLEditorKit kit = (HTMLEditorKit) aTxtPane.getEditorKit();
		HTMLDocument doc = (HTMLDocument) aTxtPane.getDocument();
		try {
			kit.insertHTML(doc, doc.getLength(), aHTML, 0, 0, null);
			Gui.txtpnConversation.setBackground(new Color(0, 0, 0, 100));
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
	}
}
