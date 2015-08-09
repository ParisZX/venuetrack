var myAppControllers = angular.module('myAppControllers', []);

myAppControllers.controller('VenueListCtrl', ['$scope', '$http', 
	function($scope, $http) {
  		$http.get('js/data.json').success(function(data) {    
	    $scope.venues = data;
	});

  	$scope.sort = 'orderby';
}]);

myAppControllers.controller('VenueDetailCtrl', ['$scope', '$http', '$routeParams',
  	function($scope, $http, $routeParams) {
    	var venueName = $routeParams.venueName;
		
		$http.get('js/data.json').success(function(data) {    
	    	var venues = data;



			for (var i=0; i<venues.length; i++) {
				if(venues[i].name == venueName) {
					$scope.venue = venues[i];
				}
			}
		});


  }]);