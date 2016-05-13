package com.yash.yits.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yash.yits.dao.MemberDao;
import com.yash.yits.domain.Issue;
import com.yash.yits.domain.Member;

/** This class interacts with database and provides the data for all member operations*/



@Repository
public class MemberDaoImpl implements MemberDao {

	 @Autowired
	 private SessionFactory sessionFactory;

	 public Member addMember(Member member) {
			Session session=sessionFactory.getCurrentSession();
			System.out.println(member);
			Criteria criteria = session.createCriteria(Member.class);
			criteria.add(Restrictions.eqOrIsNull("memberId",member.getMemberId()));
			List<Member> listOfMember=criteria.list();
			if(listOfMember.size()==1){
				
				System.out.println("User Already in database");
			}
			else{
					
				System.out.println("not in database");
				session.save(member);
			}
			return member;
		}

	public List<Member> showMembers() {
		System.out.println("dao members");
		
		Session session = sessionFactory.getCurrentSession();
		
		
		Criteria criteria =session.createCriteria(Member.class)
				.setProjection(Projections.projectionList()		
						.add(Projections.property("memberId"), "memberId")
					      .add(Projections.property("name"), "name")
					      .add(Projections.property("email"), "email")
							.add(Projections.property("contact"), "contact")
							.add(Projections.property("isActive"),"isActive"))
					    .setResultTransformer(Transformers.aliasToBean(Member.class));
		
		
		List<Member> allMembers = criteria.list();
		
		
	/*	System.out.println(allMembers);
		for (Member member : allMembers) {
			System.out.println(member.getEmail());
			System.out.println(member.getMemberId());
		}
		*/
		
		return   allMembers ;
	}

	public List<Member> searchMembers(String search) {
		System.out.println("in dao");
		Session session=sessionFactory.getCurrentSession();
		
		String selectQuery="FROM Member where name LIKE '%"+search+"%' OR email LIKE '"+search+"%' OR managerName LIKE '%"+search+"%'";
		Query query=session.createQuery(selectQuery);
		List<Member> members=query.list();
		for(Member membersList:members){
			
			System.out.println(membersList.getContact());
			
		}

		return members;
	}

	public List<Member> deleteMember(int memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *blockUnblockMember method is used to block or unblock the member
	 * 
	 */
	public List<Member> blockUnblockMember(Member member) {
	
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Member where memberId=?");
		Member member1=(Member) query.setLong(0, member.getMemberId()).uniqueResult();

		System.out.println(member1.getIsActive());
		
		if(member1.getIsActive()==0){
			
			System.out.println("inside isactive");
			member1.setIsActive(1);
			session.saveOrUpdate(member1);
			List<Member> members=showMembers();
			return members;
		}
		else{
			
			member1.setIsActive(0);
			session.saveOrUpdate(member1);
			List<Member> members=showMembers();
			
			for (Member member2 : members) {
				System.out.println(member2.getEmail());
				System.out.println(member2.getMemberId());
			}
			
			return members;
			
		}	
	}

	/**
	 * This method retrieves list of assigned issues
	 */
	public List<Issue> showAssignedIssue() {
		
		Session session= sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Issue.class);
		criteria.add(Restrictions.isNotNull("assignedUser"));
		criteria.add(Restrictions.isNotNull("closeDate"));
		
		List<Issue> issues = criteria.list();
		return issues;
	}

	/**
	 * This method retrieves list of assigned issues on the basic of searched field
	 */
	public List<Issue> searchAssignedIssue(String searchText) {
		
		searchText="%"+searchText+"%";
		Session session= sessionFactory.getCurrentSession();
		
		// Search for projects
		Criteria criteria = session.createCriteria(Issue.class,"issue");
		criteria.createAlias("issue.project", "p")
				.add(Restrictions.like("p.name", searchText));	 
		criteria.add(Restrictions.isNotNull("assignedUser"));
		criteria.add(Restrictions.isNotNull("closeDate"));
		List<Issue> issues = criteria.list();
		
		if(issues.size()!=0)
			return issues;
		
		//search for members
		System.out.println(searchText);
		Criteria criteria1 = session.createCriteria(Issue.class,"issue");
		criteria1.createAlias("issue.assignedUser", "atm")
				.createAlias("atm.member", "m")
				.add(Restrictions.like("m.name", searchText));
		criteria1.add(Restrictions.isNotNull("closeDate"));
		criteria1.add(Restrictions.isNotNull("assignedUser"));
		issues = criteria1.list();
		return issues;
	}
}
