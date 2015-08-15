(function() {
	var myApp = angular.module('myApp',['ngMap','venue']);

	myApp.controller('MainController', ['$scope', '$http' , 
		function($scope, $http) {

			$scope.venues = [];

			var apiData = 'endpoints?what=venues';
			var localData = 'data/data.json';

			$http.get(apiData).success(function(data) {    
	    	
	    	$scope.venues = data;
	    
	    });





	}]);