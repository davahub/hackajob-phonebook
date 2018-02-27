package org.hackajob.phonebook;

import java.util.ArrayList;
import java.util.List;

public class PhoneBook {
	private List<Contact> contacts;

	public PhoneBook() {
		contacts = new ArrayList<Contact>();
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
