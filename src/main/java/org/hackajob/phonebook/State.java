
package org.hackajob.phonebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {

	private List<String> keywords;
	private List<Integer> keyvalues;
	private int matchedLength = 0;

	public State() {
		keywords = new ArrayList<String>();
		keyvalues = new ArrayList<Integer>();
	}

	public void addKeyword(Integer aKeyvalue, String aKeyword) {
		keyvalues.add(aKeyvalue);
		keywords.add(aKeyword);
	}

	public int size() {
		return keywords.size();
	}

	public String getKeyword(int aIndex) {
		return keywords.get(aIndex);
	}

	public String makeString(List<String> aList) {
		String build = "";
		for (int i = 0; i < aList.size(); i++) {
			build = build + " " + aList.get(i);
		}
		return build.trim();
	}

	public int isExist(String anAnswer) {
		int index = keywords.indexOf(anAnswer);
		if (index == -1)
			return index;
		else
			return keyvalues.get(index);
	}

	// ********************************************************************************
	// This the main method which returns the key-value if the given String
	// matches with one of the keywords. Important thing here is that the algorithm
	// does its evaluation on the weight of the user input.
	// ********************************************************************************
	public int search(String aQuestion) {
		aQuestion = aQuestion.toLowerCase();
		String[] array = aQuestion.split("\\s+");
		List<String> answerWords = Arrays.asList(array);
		List<String> tmpList;
		int keyvalue;
		int size = answerWords.size();
		int times = 1;
		int limit = size;
		int tmpLimit;
		String strJoined;
		while (times <= size) {
			tmpLimit = limit;
			for (int i = 0; i < times; i++) {
				tmpList = answerWords.subList(i, tmpLimit);
				strJoined = makeString(tmpList);
				keyvalue = isExist(strJoined);
				if (keyvalue != -1) {
					matchedLength = strJoined.length();
					return keyvalue;
				}
				tmpLimit = tmpLimit + 1;
			}
			times = times + 1;
			limit = limit - 1;
		}
		return -1;
	}

	public int getMatchedLength() {
		return matchedLength;
	}

}
