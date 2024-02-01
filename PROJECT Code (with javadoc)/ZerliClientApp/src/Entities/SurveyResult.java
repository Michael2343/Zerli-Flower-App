package Entities;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class SurveyResult represents a survey result containing
 * the actual pdf files to be shown on request.
 */
public class SurveyResult implements Serializable {
	
	/** The survey id. */
	private int surveyId;
	
	/** The survey name. */
	private String surveyName;
	
	/** The expert summary as a pdf byte stream. */
	byte[] expertSummary;
	
	/** The survey results as a pdf byte stream. */
	byte[] surveyResults;
	
	/**
	 * Instantiates a new survey result.
	 *
	 * @param surveyId the survey id
	 * @param surveyName the survey name
	 * @param expertSummary the expert summary pdf
	 * @param surveyResults the survey results pdf
	 */
	public SurveyResult(int surveyId, String surveyName, byte[] expertSummary, byte[] surveyResults) {
		super();
		this.surveyId = surveyId;
		this.surveyName = surveyName;
		this.expertSummary = expertSummary;
		this.surveyResults = surveyResults;
	}
	
	/**
	 * Gets the survey id.
	 *
	 * @return the survey id
	 */
	public int getSurveyId() {
		return surveyId;
	}
	
	/**
	 * Sets the survey id.
	 *
	 * @param surveyId the new survey id
	 */
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	
	/**
	 * Gets the survey name.
	 *
	 * @return the survey name
	 */
	public String getSurveyName() {
		return surveyName;
	}
	
	/**
	 * Sets the survey name.
	 *
	 * @param surveyName the new survey name
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	
	/**
	 * Gets the expert summary.
	 *
	 * @return the expert summary
	 */
	public byte[] getExpertSummary() {
		return expertSummary;
	}
	
	/**
	 * Sets the expert summary.
	 *
	 * @param expertSummary the new expert summary
	 */
	public void setExpertSummary(byte[] expertSummary) {
		this.expertSummary = expertSummary;
	}
	
	/**
	 * Gets the survey results.
	 *
	 * @return the survey results
	 */
	public byte[] getSurveyResults() {
		return surveyResults;
	}
	
	/**
	 * Sets the survey results.
	 *
	 * @param surveyResults the new survey results
	 */
	public void setSurveyResults(byte[] surveyResults) {
		this.surveyResults = surveyResults;
	}


}
