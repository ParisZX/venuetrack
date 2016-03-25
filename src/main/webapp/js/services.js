var venuetrackServices = angular.module('venuetrackServices', ['ngResource']);

venuetrackServices.factory('venuesAPI', ['$resource',
  function($resource){
    return $resource('endpoints?searchFor=:id', {}, {
      query: {
      	method:'GET', params:{id:'venues'}, isArray:true
      }
    });
  }]);
