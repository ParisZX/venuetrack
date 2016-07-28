  function startApp() {

      var ROOT = 'https://venuetrack.appspot.com/_ah/api';
      gapi.client.load('venuetrackEndpoints', 'v1', function() {
          angular.bootstrap(document, ["myApp"]);
      }, ROOT);
  }

  var myApp = angular.module('myApp',['ngRoute','venuetrackServices','sidebar','navbar']);

	myApp.controller('MainController', ['$scope', '$routeParams', 'venuesAPI' ,

		function($scope, $routeParams, venuesAPI) {

	    	console.log('im MainController!');

			  var allVenues; // var venues = []; var markers = []; var infoWindows = []; var map;

	  		// $scope.venues = venuesAPI.query(function(data) {
        //
        //   $scope.venues = data;
        //
        //   allVenues = data;
        //
        //   for (var i in data)
        //     if(data[i].id != null)
		    //       createMarker(data[i]);
        //
        // });

        $scope.venues = gapi.client.venuetrackEndpoints.endpoints.listVenues().execute(function(resp) {

          $scope.venues = resp.items;

          $scope.$apply();

          allVenues = resp.items;

          for (var i in resp.items)
            if(resp.items[i].id != null)
  	          createMarker(resp.items[i]);

        });

  	    $scope.markers = []; $scope.sort = 'orderby'; $scope.sortByPopularity = 'popularity'; $scope.categoryFilter = ''; $scope.locationFilter = ''; $scope.limitVenues = 200;

        // $scope.change = function(data) {
        //   $scope.venues = allVenues.filter(function (el) {
        //     var searchTo = el.name + el.location.address + el.location.city + el.categories[0].name;
        //
        //     if (searchTo.replace(/\s+/g, '').toLowerCase().indexOf(data.replace(/\s+/g, '').toLowerCase()) > -1)
        //      return el;
        //   });
        // };

        $scope.searchFilter = function(venue) {
          // default to no match
          var isMatch = true;

          if ($scope.query) {
            // split the input by space
            var parts = $scope.query.split(' ');

            // iterate each of the words that was entered
            parts.forEach(function(part) {

              var searchTo = venue.name + venue.location.address + venue.location.city + venue.categories[0].name;

              // if the word is found in the post, a set the flag to return it.
              if (!(searchTo.replace(/\s+/g, '').toLowerCase().indexOf(part.replace(/\s+/g, '').toLowerCase()) > -1)) {
                isMatch = false;
              }
            });
          } else {
            // if nothing is entered, return all posts
            isMatch = true;
          }

          return isMatch;
        };

	  		thessCenter = {lat: 40.6323456, lng: 22.9408366};

		  	$scope.map = new google.maps.Map(document.getElementById('map'), {
			    	zoom: 13,
			    	center: thessCenter
		  	});

		    var createMarker = function (info) {

			  	var infoWindow = new google.maps.InfoWindow(); var venuetrackRating;

		        var marker = new google.maps.Marker({
		            map: $scope.map,
		            position: new google.maps.LatLng(info.lat, info.lng),
		            id: info.id,
		            title: info.name,
		            icon: info.categories[0].icon.prefix+"bg_32"+info.categories[0].icon.suffix
		        });

            if(info.venuetrackRating == "neg") {
              venuetrackRating = 'glyphicon-thumbs-down';
            }
            else {
              venuetrackRating = 'glyphicon-thumbs-up';
            }

            // icon: "images/icons/"+info.categories[0].icon.prefix.slice(39)+"bg_32"+info.categories[0].icon.suffix

		        marker.content = '<div class="infoWindowContent"> <div class="marker-info marker-category">Category: ' + info.categories[0].name + " <img style='height: 25px;' src='" + info.categories[0].icon.prefix+"bg_32"+info.categories[0].icon.suffix + "'/>" + '</div>' +
            '<div class="marker-info marker-address">Address: ' + info.location.address + ' ' + info.location.city + '</div> <div class="marker-info marker-stats"> Checkins Count: ' + info.stats.checkinsCount + ' | Tips Count: ' + info.stats.tipCount + '</div>' +
            '<div class="marker-info rating-tab"> Foursquare rating: <span class="fsq-rating" style="background: #' + info.ratingColor + '">' +
            info.rating + '/10</span> | Venuetrack rating: <span class="venuetrack-rating glyphicon ' +  venuetrackRating + '" ></span> </div>' +
            '<div class="marker-info marker-url"> Venue URL: <a href="' + info.url + '">' + info.url + '</a></div></div>' ;

		        google.maps.event.addListener(marker, 'click', function(){
		            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
		            infoWindow.open($scope.map, marker);
		        });

		        $scope.markers.push(marker);

		    }

  	}]);


	myApp.controller('VenuesController', ['$scope',

		function($scope) {

	    	console.log('im VenuesController!');

    		//if($scope.markers.length==0)
    		//	$scope.drawMarkers();

			console.log($scope.markers.length);

	}]);

	myApp.controller('VenueController', ['$scope', '$routeParams',

		function($scope,$routeParams) {

			console.log('im VenueController, called by href');
			console.log('The http parameter is '+ $routeParams.id);

			//if($scope.markers.length==0)
    	//		$scope.drawMarkers();

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
