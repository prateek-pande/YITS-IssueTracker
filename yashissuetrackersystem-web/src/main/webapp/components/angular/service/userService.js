angular.module('issueTrackingSystem.userModule').factory('userService',['$http',function($http){
	
	  return {
		  
		  initializeSelect: function() {
	          return $http.get('./getProjects')
	              .then(
	                      function(response){
	                    	  alert("in service");
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
		      	}
	  }
}]);