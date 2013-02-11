/* Created By Alex Drotar and Gabe Russotto
 * Created Jan 28th 2013
 * This is an Open Source Project for Gunshot Localization.
 * This implements the Google Maps API for the Sensor information
*/

//var kml = new google.maps.KmlLayer("http://kml.lover.googlepages.com/my-vacation-photos.kml");
var kml = new google.maps.KmlLayer("http://ErikEKahn.me/~drotara/sensors.kml");

var map;
function initialize() {
    var mapOptions = {
        center: new google.maps.LatLng(40.0150, -105.27),
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    /*var pointA = new google.maps.Latlng(40.70, -74.00); // Circle Center
    var radius = 10; // 10KM
    new google.maps.Circle({
          center: pointA,
          radius: radius* 1000, // Convert to meters
          fillColor: '#FF0000',
          fillOpacity: 0.2,
          map: map
     });*/
     kml.setMap(map);
}


