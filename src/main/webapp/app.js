'use strict';

// Declare app level module which depends on views, and components
var myApp = angular.module('myApp', [
  'ngRoute',
  'myApp.version',
  'ngMap'
]);

myApp.controller('VenueListCtrl', function ($scope) {
	$scope.venues = $data;
});