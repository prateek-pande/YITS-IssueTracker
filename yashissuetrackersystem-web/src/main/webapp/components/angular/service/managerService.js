angular.module('issueTrackingSystem.managerModule').factory('managerService',['$http','$q',function($http,$q){
	
	  return {
		  
		  
		  		checkUserInLdap:function(ldapUser){
		  				alert("inside service");
		  				alert("Name---in service "+ldapUser.ldapName);
		  				return $http.post('./checkMemberInLdap',ldapUser)
		  					.then(
		  								function(response){
		  								  return response.data;
		  								},
		  								function(errResponse){
		  									console.error('Error while fetching users');
		  									return $q.reject(errResponse);
		  								}
	
		  						);
			  
		  			},
		  
	  
	  getIssues:function(){
		  
		  var issues=$http({
		  method:'GET',
		      url:'./defaultIssues'
		 //spring controller call, use @ResponseBody
		  }).success(function(data){
		  alert(data);
		  return data;
		  })
		  }
	  
		 /* initializeSelect: function() {
	          return $http.get('./getPriority')
	              .then(
	                      function(response){
	                          return response.data;
	                      }, 
	                      function(errResponse){
	                          console.error('Error while fetching users');
	                              return $q.reject(errResponse);
	                      }
	                );
	      	},
	  
	      	submitCreateIssue: function(createIssue) {
		          return $http.post('./createIssue',createIssue)
		              .then(
		                      function(response){
		                    	  alert(response.data);
		                          return response.data;
		                      }, 
		                      function(errResponse){
		                          console.error('Error while fetching users');
		                              return $q.reject(errResponse);
		                      }
		                );
		      	},*/
	      	
	      	
	      	/*searchIssue:function(searchText){
				return $http.get(
						'./getIssues/'
						+ searchText)
			 	.then(
                	 function(response){
                     	return response.data;
                	 }, 
                	 function(errResponse){
                     	console.error('Error while searching issues');
                     	return $q.reject(errResponse);
                 	 }
         		);
		}*/
	      	
	  }
	 

}]);