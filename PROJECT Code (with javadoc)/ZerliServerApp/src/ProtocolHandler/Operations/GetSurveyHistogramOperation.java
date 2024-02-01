package ProtocolHandler.Operations;

import java.util.ArrayList;

import Entities.Survey;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import reports.SurveyHistogramGenerator;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class GetSurveyHistogramOperation defines the operation
 * that is invoked upon an GetSurveyHistogramOperation Request.
 */
public class GetSurveyHistogramOperation implements IOperation {

	/**
	 * This perform is not used since the operation is an Operate&Respond request.
	 *
	 * @param requestee the requestee
	 * @param data the data
	 * @param params the params
	 * @param response the response
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * The perform operation is the method that is being invoked when the matching request is made
	 * all database calls are managed here, along with some processing and input validation.
	 *
	 * @param requestee - the request sender
	 * @param data - the data that was sent
	 * @param params - the parameters constraints that were pinned to the data
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params, ResponseWrapper response) {
		if(data instanceof Survey)
		{
			ArrayList<int[]> listOfSurveyAnswers=new ArrayList<int[]>();
			for (int i = 0; i < 6; i++) {
				listOfSurveyAnswers.add(new int [10]);
			}
			ArrayList<byte[]> listOfImages;
			Survey survey=(Survey) data; 
			Server.SqlServerManager.GetSurveyHistogram(survey,listOfSurveyAnswers);
			SurveyHistogramGenerator shg=new SurveyHistogramGenerator();
			listOfImages=shg.get6images(listOfSurveyAnswers,survey);
			response.SetResponse(listOfImages);
			return true;
			
		}
		return false;
	}

}
