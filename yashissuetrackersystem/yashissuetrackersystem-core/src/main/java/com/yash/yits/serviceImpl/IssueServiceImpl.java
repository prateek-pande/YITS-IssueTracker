package com.yash.yits.serviceImpl;



import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import java.util.HashSet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yash.yits.dao.IssueDao;

import com.yash.yits.domain.ApplicationEnvironment;
import com.yash.yits.domain.ApplicationIssuePriority;
import com.yash.yits.domain.ApplicationIssueStatus;
import com.yash.yits.domain.ApplicationIssueType;
import com.yash.yits.domain.ApplicationTeamMember;
import com.yash.yits.domain.Issue;

import com.yash.yits.domain.Member;
import com.yash.yits.form.ApplicationIssuePriorityForm;
import com.yash.yits.form.ApplicationIssueStatusForm;
import com.yash.yits.form.ApplicationIssueTypeForm;
import com.yash.yits.form.ApplicationTeamMemberForm;

import com.yash.yits.domain.Issue;

import com.yash.yits.domain.Project;
import com.yash.yits.form.ApplicationIssuePriorityForm;
import com.yash.yits.form.ApplicationIssueTypeForm;

import com.yash.yits.form.IssueForm;
import com.yash.yits.form.MemberForm;
import com.yash.yits.form.ProjectForm;
import com.yash.yits.service.IssueService;

@Service
@Transactional
public class IssueServiceImpl implements IssueService{
	


	@Autowired
	private IssueDao issueDao;

	
	public List<IssueForm> getUnassignedIssues() {

		List<Issue> issueList=issueDao.getUnassignedIssues();
		List<IssueForm> unassignedIssueList=new ArrayList<IssueForm>();
		
		for (Issue issue: issueList) {
			
			IssueForm issueForm=new IssueForm();
			
			ApplicationIssueTypeForm applicationIssueType=new ApplicationIssueTypeForm();
			applicationIssueType.setType(issue.getApplicationIssueType().getType());
			issueForm.setApplicationIssueType(applicationIssueType);
			
			issueForm.setSummary(issue.getSummary());
			issueForm.setId(issue.getId());
			
			ApplicationIssuePriorityForm applicationIssuePriority=new ApplicationIssuePriorityForm();
			applicationIssuePriority.setType(issue.getApplicationIssuePriority().getType());
			issueForm.setApplicationIssuePriority(applicationIssuePriority);
			
			ProjectForm projectForm=new ProjectForm();
			projectForm.setName(issue.getProject().getName());
			issueForm.setProject(projectForm);
			
			unassignedIssueList.add(issueForm);
			
		}
		return unassignedIssueList;
	}


	public List<IssueForm> getDefaultIssues() {
		
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
	    
	    Date currentTime = localCalendar.getTime();
	    int currentDay = localCalendar.get(Calendar.DATE);
	    int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
	    Date date=new Date();
		
		Calendar cal = Calendar.getInstance();
		Date dateBefore=new Date();
		cal.setTime(currentTime);
		if(currentDayOfWeek==2){
			cal.add(Calendar.DATE, -8);
			dateBefore = cal.getTime();
		}
		else if(currentDayOfWeek==3){
			cal.add(Calendar.DATE, -9);
			dateBefore = cal.getTime();
		}
		else if(currentDayOfWeek==4){
			cal.add(Calendar.DATE, -10);
			dateBefore = cal.getTime();
		}
		else if(currentDayOfWeek==5){
			cal.add(Calendar.DATE, -11);
			dateBefore = cal.getTime();
		}
		else if(currentDayOfWeek==6){
			cal.add(Calendar.DATE, -12);
			dateBefore = cal.getTime();
		}
		else if(currentDayOfWeek==7){
			cal.add(Calendar.DATE, -13);
			dateBefore = cal.getTime();
		}
		else{
			cal.add(Calendar.DATE, -7);
			dateBefore = cal.getTime();
		}
		cal.add(Calendar.DATE, +14);
		Date dateAfter = cal.getTime();
		
		List<Issue> issues = issueDao.getDefaultIssues(dateBefore,dateAfter);
		
		
		List<IssueForm> issueForms = new ArrayList<IssueForm>();
		for (Issue issue : issues) {

			
			IssueForm issueForm = new IssueForm();
			
			issueForm.setId(issue.getId());
			issueForm.setCloseDate(issue.getCloseDate());
			issueForm.setCreatedDateTime(issue.getCreatedDateTime());
			issueForm.setDueDate(issue.getDueDate());
			issueForm.setSummary(issue.getSummary());

			ProjectForm projectForm=new ProjectForm();
			projectForm.setName(issue.getProject().getName());
			issueForm.setProject(projectForm);
			
			ApplicationIssuePriorityForm applicationIssuePriorityForm=new ApplicationIssuePriorityForm();
			applicationIssuePriorityForm.setType(issue.getApplicationIssuePriority().getType());
			System.out.println(issue.getApplicationIssuePriority().getType());
			issueForm.setApplicationIssuePriority(applicationIssuePriorityForm);
			System.out.println(issueForm.getApplicationIssuePriority()+"issueForm");
			
			ApplicationIssueStatusForm applicationIssueStatusForm=new ApplicationIssueStatusForm();
			applicationIssueStatusForm.setStatus(issue.getApplicationIssueStatus().getStatus());
			issueForm.setApplicationIssueStatus(applicationIssueStatusForm);

			ApplicationIssueTypeForm applicationIssueTypeForm=new ApplicationIssueTypeForm();
			applicationIssueTypeForm.setType(issue.getApplicationIssueType().getType());
			issueForm.setApplicationIssueType(applicationIssueTypeForm);
			
			issueForms.add(issueForm);
		}
		
		return issueForms;
	}

	
	public List<ProjectForm> getProjectNames() {
		List<ProjectForm> projectForms= new ArrayList<ProjectForm>();
		List<Project> projects = issueDao.getProjectNames();
		System.out.println(projects);
		Iterator<Project>  iterator = projects.iterator();
		while (iterator.hasNext()) {
			Project project = (Project) iterator.next();
			ProjectForm projectForm = new ProjectForm();
			projectForm.setId(project.getId());
			projectForm.setName(project.getName());
			projectForms.add(projectForm);
		}
		return projectForms;
	}


	public void createIssue(IssueForm issueForm,Long createdBy) {
		Project project=new Project();
		project.setId(issueForm.getProject().getId());
		
		ApplicationEnvironment applicationEnvironment=new ApplicationEnvironment();
		applicationEnvironment.setId(issueForm.getApplicationEnvironment().getId());
		
		ApplicationIssuePriority applicationIssuePriority=new ApplicationIssuePriority();
		applicationIssuePriority.setId(issueForm.getApplicationIssuePriority().getId());
		
		ApplicationIssueType applicationIssueType=new ApplicationIssueType();
		applicationIssueType.setId(issueForm.getApplicationIssueType().getId());
		
		ApplicationIssueStatus applicationIssueStatus=new ApplicationIssueStatus();
		applicationIssueStatus.setId(1);
		
		Issue issue=new Issue();
		issue.setAffectedVersion(issueForm.getAffectedVersion());
		issue.setComponent(issueForm.getComponent());
		issue.setDescription(issueForm.getDescription());
		issue.setSummary(issueForm.getSummary());
		issue.setProject(project);
		issue.setApplicationIssuePriority(applicationIssuePriority);
		issue.setApplicationEnvironment(applicationEnvironment);
		issue.setApplicationIssueType(applicationIssueType);
		issue.setApplicationIssueStatus(applicationIssueStatus);
		issueDao.createIssue(issue,createdBy);
}

	

	public Map<String, Object> getAllSelectFields(ProjectForm projectForm, MemberForm member) {

		Project project = new Project();
		project.setId(projectForm.getId());
		

		return issueDao.getAllSelectFields(project,member);

	}

	public List<String> getDefaultIssueTypes() {
		
		List<ApplicationIssueType> issueTypes=issueDao.getDefaultIssueTypes();
		
		List<String> issueTypesList=new ArrayList<String>();
		
		for (ApplicationIssueType issue : issueTypes) {
			
			String type=issue.getType();
			issueTypesList.add(type);
		}
		
		return issueTypesList;
	}


	public IssueForm fetchIssueDetails(int fetchId) {
		System.out.println("prajvi service");
		Issue fetchedIssue=issueDao.fetchIssueDetails(fetchId);
		IssueForm fetchedIssueForm=new IssueForm();
		return fetchedIssueForm;
	}
	
}
