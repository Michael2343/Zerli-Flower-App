package Entities;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * contain survey details to transfer between the server and the client.
 */
public class Survey implements Serializable{

/** survey id in the database. */
private int id;

/** name of the survey. */
private String content ;

/** The question array that the survey consist of. */
private String[] questionArray=new String[6];

/** The answers array that the user fill. */
private int[] answersArray= new int[6];

/**
 * Gets the answers.
 *
 * @return the answers
 */
public int[] getAnswers() {
	// TODO Auto-generated method stub
	return answersArray;
}

/**
 * Gets the questions.
 *
 * @return the questions
 */
public String[] getQuestions() {
	// TODO Auto-generated method stub
	return questionArray;
} 

/**
 * To string.
 *
 * @return the string
 */
@Override
	public String toString() {
	String string="";
		for (String s : questionArray) {
			string=string+s;
		}
		return string;
	}

/**
 * Gets the content.
 *
 * @return the content
 */
public String getContent() {
	return content;
}

/**
 * Sets the content.
 *
 * @param content the new content
 */
public void setContent(String content) {
	this.content = content;
}

/**
 * Gets the id.
 *
 * @return the id
 */
public int getId() {
	return id;
}

/**
 * Sets the id.
 *
 * @param id the new id
 */
public void setId(int id) {
	this.id = id;
}
}
