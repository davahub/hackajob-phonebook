package org.hackajob.phonebook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class PhoneBook {
	private ArrayList<Contact> contacts;
	
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

	public ArrayList<Contact> searchContacts(String searchValue) {
		ArrayList<Contact> searchContacts = new ArrayList<Contact>();
		for (Contact contact : contacts) {
			String name = contact.getName();
			String address = contact.getAddress();
			String phoneNumber = contact.getPhone_number();
			if (name.toLowerCase().contains(searchValue.toLowerCase()) 
					|| address.toLowerCase().contains(searchValue.toLowerCase()) 
					|| phoneNumber.toLowerCase().contains(searchValue.toLowerCase())) {
				searchContacts.add(contact);
			}
		}
		return searchContacts;
	}
	
	public void sortByName() {
		Comparator<Contact> nameComparator = new Comparator<Contact>() {
			public int compare(Contact contact1, Contact contact2) {
				String name1 = contact1.getName().toUpperCase();
				String name2 = contact2.getName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
		Collections.sort(contacts, nameComparator);
	}
	
	public void sortByTelephone() {
		Comparator<Contact> telephoneComparator = new Comparator<Contact>() {
			public int compare(Contact contact1, Contact contact2) {
				String telephone1 = contact1.getPhone_number();
				String telephone2 = contact2.getPhone_number();
		        return (int)(extractInt(telephone1) - extractInt(telephone2));
		    }
		    long extractInt(String s) {
		        String num = s.replaceAll("\\D", "");
		        return num.isEmpty() ? 0 : Long.parseLong(num);
		    }
		};
		Collections.sort(contacts, telephoneComparator);
	}
	
	public void sortByAddress() {
		Comparator<Contact> addressComparator = new Comparator<Contact>() {
			public int compare(Contact contact1, Contact contact2) {
				String address1 = contact1.getAddress().toUpperCase();
				String address2 = contact2.getAddress().toUpperCase();
				return address1.compareTo(address2);
			}
		};
		Collections.sort(contacts, addressComparator);
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}
}
