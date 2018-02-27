package org.hackajob.phonebook;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class PhoneBook {
	private List<Contact> contacts;
	
	private Client client;
	private WebTarget target;

	public PhoneBook() {
		client = ClientBuilder.newClient();
	    target = client.target("http://www.mocky.io/v2/581335f71000004204abaf83");
	    ContactResponse response = target.request(MediaType.APPLICATION_JSON).get(ContactResponse.class);
		contacts = response.getContacts();
	}

	public void addContact(Contact aContact) {
		contacts.add(aContact);
	}

	public Contact getContact(int index) {
		return contacts.get(index);
	}

	public int getSizeMembers() {
		return contacts.size();
	}

	public PhoneBook searchContacts(String searchValue) {
		PhoneBook phonebook = new PhoneBook();
		for (Contact contact : contacts) {
			String name = contact.getName();
			String address = contact.getAddress();
			String phoneNumber = contact.getPhone_number();
			if (name.equalsIgnoreCase(searchValue) 
					|| address.equalsIgnoreCase(searchValue) 
					|| phoneNumber.equalsIgnoreCase(searchValue)) {
				phonebook.addContact(contact);
			}
		}
		return phonebook;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
}
