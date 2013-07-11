package teammates.ui.controller;

import java.util.ArrayList;

import teammates.common.datatransfer.EvaluationAttributes;
import teammates.common.datatransfer.SubmissionAttributes;
import teammates.common.datatransfer.EvaluationAttributes.EvalStatus;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Sanitizer;
import teammates.logic.api.GateKeeper;

import com.google.appengine.api.datastore.Text;

public class StudentEvalSubmissionEditSaveAction extends Action {
	

	@Override
	public ActionResult execute() throws EntityDoesNotExistException {
		
		String courseId = getRequestParam(Const.ParamsNames.COURSE_ID);
		Assumption.assertNotNull(courseId);
		
		String evalName = getRequestParam(Const.ParamsNames.EVALUATION_NAME);
		Assumption.assertNotNull(evalName);
		
		String fromEmail = getRequestParam(Const.ParamsNames.FROM_EMAIL);
		Assumption.assertNotNull(fromEmail);
		
		String teamName = getRequestParam(Const.ParamsNames.TEAM_NAME);
		String[] toEmails = getRequestParamValues(Const.ParamsNames.TO_EMAIL);
		String[] points = getRequestParamValues(Const.ParamsNames.POINTS);
		String[] justifications = getRequestParamValues(Const.ParamsNames.JUSTIFICATION);
		String[] comments = getRequestParamValues(Const.ParamsNames.COMMENTS);
		
		EvaluationAttributes eval = logic.getEvaluation(courseId, evalName);
		
		if(eval.getStatus() != EvalStatus.OPEN){
			throw new UnauthorizedAccessException("This evalutions is not currently open for editing");
		}
		
		//extract submission data
		ArrayList<SubmissionAttributes> submissionData = new ArrayList<SubmissionAttributes>();
		int submissionCount = ((toEmails == null ? 0 : toEmails.length));
		for(int i=0; i<submissionCount ; i++){
			SubmissionAttributes sub = new SubmissionAttributes();
			sub.course = courseId;
			sub.evaluation = evalName;
			sub.justification = new Text(justifications[i]);
			
			if (eval.p2pEnabled) {
				sub.p2pFeedback = new Text(comments[i]);
			}
			
			sub.points = Integer.parseInt(points[i]);
			sub.reviewee = toEmails[i];
			sub.reviewer = fromEmail;
			sub.team = teamName;
			submissionData.add(sub);
		}
		
		new GateKeeper().verifyAccessible(
				logic.getStudentForGoogleId(courseId, account.googleId),
				submissionData);
		
		try{
			logic.updateSubmissions(submissionData);
			statusToUser.add(String.format(Const.StatusMessages.STUDENT_EVALUATION_SUBMISSION_RECEIVED, Sanitizer.sanitizeForHtml(evalName), courseId));
			statusToAdmin = createLogMesage(courseId, evalName, teamName, fromEmail, toEmails, points, justifications, comments);
			
		} catch (InvalidParametersException e) {
			//TODO: redirect to the same page?
			setStatusForException(e);
		}		
		
		RedirectResult response = createRedirectResult(Const.ActionURIs.STUDENT_HOME_PAGE);
		return response;

	}

	private String createLogMesage(
			String courseId, 
			String evalName,
			String teamName,
			String fromEmail, 
			String[] toEmails, 
			String[] points,
			String[] justifications, 
			String[] comments) {
		
		String message = "<span class=\"bold\">(" + teamName + ") " + fromEmail + "'s</span> Submission for Evaluation <span class=\"bold\">(" + evalName + ")</span> for Course <span class=\"bold\">[" + courseId + "]</span> edited.<br><br>";
		
		int submissionCount = ((toEmails == null ? 0 : toEmails.length));
		for (int i = 0; i < submissionCount; i++){
			message += "<span class=\"bold\">To:</span> " + toEmails[i] + "<br>";
			message += "<span class=\"bold\">Points:</span> " + points[i] + "<br>";
			if (comments == null){	//p2pDisabled
				message += "<span class=\"bold\">Comments: </span>Disabled<br>";
			} else {
				message += "<span class=\"bold\">Comments:</span> " + comments[i].replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>") + "<br>";
			}
			message += "<span class=\"bold\">Justification:</span> " + justifications[i].replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
			message += "<br><br>";
		}  
		return message;
	}

	
	
	
}