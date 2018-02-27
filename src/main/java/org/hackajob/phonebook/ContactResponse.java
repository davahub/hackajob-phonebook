package org.hackajob.phonebook;

import java.util.ArrayList;

public class ContactResponse {
	
	private ArrayList<Contact> contacts;
	
	public ContactResponse() {
		
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}
}
