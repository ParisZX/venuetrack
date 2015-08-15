(function() {

	var myApp = angular.module('venue', []);

	myApp.directive('venueDetails', function(){

		return {

			restrict: 'E',
			templateUrl: 'partials/venue-details.html',
			controller: function() {
				this.tab = 1;

				this.isActive = function(check) {
					return this.tab === check;
				};

				this.setTab = function(selected) {
					this.tab = selected;
				};
			},
			controllerAs: 'panel'

			}
		});


})();