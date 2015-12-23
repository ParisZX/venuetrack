var venuetrackServices = angular.module('venuetrackServices', ['ngResource']);

venuetrackServices.factory('venuesAPI', ['$resource',
  function($resource){
    return $resource('data/:id.json', {}, {
      query: {
      	method:'GET', params:{id:'data'}, isArray:true
      }
    });
  }]);
