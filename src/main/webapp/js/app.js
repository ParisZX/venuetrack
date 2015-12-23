	var myApp = angular.module('myApp',['ngRoute','venuetrackServices','sidebar','navbar']);

	myApp.controller('MainController', ['$scope', '$routeParams', 'venuesAPI' , 
		
		function($scope, $routeParams, venuesAPI) {
			
	    	console.log('im MainController!');

			// var venues = []; var markers = []; var infoWindows = []; var map;

	  		$scope.venues = venuesAPI.query();

    	    $scope.markers = [];

	  		thessCenter = {lat: 40.6323456, lng: 22.9408366};

		  	$scope.map = new google.maps.Map(document.getElementById('map'), {
			    	zoom: 13,
			    	center: thessCenter
		  	});
  		
			$scope.drawMarkers = function() {
			
			    var createMarker = function (info) {
	        		
				  	var infoWindow = new google.maps.InfoWindow();

			        var marker = new google.maps.Marker({
			            map: $scope.map,
			            position: new google.maps.LatLng(info.lat, info.lng),
			            id: info.id,
			            title: info.name,
			            icon: info.categories[0].icon.prefix+"bg_32"+info.categories[0].icon.suffix
			        });
			        
			        marker.content = '<div class="infoWindowContent">' + info.location.address + '</div>';
			        
			        google.maps.event.addListener(marker, 'click', function(){
			            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
			            infoWindow.open($scope.map, marker);
			        });
			        
			        $scope.markers.push(marker);
			        
			    }  
		    
			    for (i = 0; i < $scope.venues.length; i++){
			        createMarker($scope.venues[i]);
			    }

			}
  	
  	}]);


	myApp.controller('VenuesController', ['$scope', 

		function($scope) {
		  
	    	console.log('im VenuesController!');
    
    		if($scope.markers.length==0)
    			$scope.drawMarkers();

			console.log($scope.markers.length);

	}]);
	
	myApp.controller('VenueController', ['$scope', '$routeParams',

		function($scope,$routeParams) {

			console.log('im VenueController, called by href');
			console.log('The http parameter is '+ $routeParams.id);
			
			if($scope.markers.length==0)
    			$scope.drawMarkers();

			var findActive = function(){

				for (i=0;i<$scope.venues.length;i++) {
					if($scope.venues[i].id==$routeParams.id) {
						return $scope.venues[i];
					}
				}
				return false;
			}

			$scope.activeVenue = findActive();

			console.log('The activeVenue id is ' + $scope.activeVenue.id + ' and the name of the venue is ' + $scope.activeVenue.name);
			console.log('The activeVenue coordinates are ' + $scope.activeVenue.lat + ',' + $scope.activeVenue.lng);

			latLng = {
      			lat: $scope.activeVenue.lat,
      			lng: $scope.activeVenue.lng
      		};

      		console.log('The center of the map SHOULD BE ' + latLng.lat + ',' + latLng.lng);

	      	$scope.map.panTo(latLng);
      	    $scope.map.setZoom(18);

      	    console.log('The center of the map is ' + $scope.map.center.lat() + ',' + $scope.map.center.lng());

      	    var findActiveMarker = function(){

				for (i=0;i<$scope.markers.length;i++) {

					if($scope.markers[i].id==$scope.activeVenue.id)
						return $scope.markers[i];
				}
				return false;
			}

			$scope.activeMarker = findActiveMarker();

		  	var infoWindow = new google.maps.InfoWindow();
  
            infoWindow.setContent('<h2>' + $scope.activeMarker.title + '</h2>' + $scope.activeMarker.content);

            infoWindow.open($scope.map, $scope.activeMarker);

			console.log($scope.venues.length + ' venues!');

			console.log($scope.markers.length + ' markers!');


	}]);

	myApp.config(['$routeProvider', function($routeProvider) {
  		$routeProvider.
			when("/venues", {
				templateUrl: 'partials/venues.html',
				controller: 'VenuesController'
			}).
			when("/venues/:id", {
				templateUrl: 'partials/venue.html',
				controller: 'VenueController'
			}).
			otherwise({redirectTo: '/venues'});
	}]);;

