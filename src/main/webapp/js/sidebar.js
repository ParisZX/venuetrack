(function() {

	var myApp = angular.module('sidebar', []);

	myApp.directive('sidebar', function(){

		return {

			restrict: 'E',
			templateUrl: 'partials/sidebar.html',
			controller: function() {
				
			},
			controllerAs: 'sidebar'

			}
		});


})();