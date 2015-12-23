(function() {

	var myApp = angular.module('navbar', []);

	myApp.directive('navbar', function(){

		return {

			restrict: 'E',
			templateUrl: 'partials/navbar.html',
			controller: function() {
				
			},
			controllerAs: 'navbar'

			}
		});


})();