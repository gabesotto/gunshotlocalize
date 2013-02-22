/* Created By Alex Drotar and Gabe Russotto
 * Created Jan 28th 2013
 * This is an Open Source Project for Gunshot Localization.
 * This implements the Google Maps API 
*/

//var kml = new google.maps.KmlLayer("http://kml.lover.googlepages.com/my-vacation-photos.kml");
var kml = new google.maps.KmlLayer("http://ErikEKahn.me/~drotara/map.kml");

var map;
function initialize() {
    var mapOptions = {
        center: new google.maps.LatLng(40.0150, -105.27),
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
//    var location = new google.maps.LatLng(40.0250, -105.27);
//    add_marker(location);
    kml.setMap(map);
}

function add_marker(location) {
     var marker = new google.maps.Marker({
        position: location,
        map: map
    });
    marker.setTitle('Gun Shot');
    add_message(marker);
}


function add_message(marker) {
    var infowindow = new google.maps.InfoWindow({
        content: 'Gun Shot here at: 09:00 1/1/2013',
        size: new google.maps.Size(25,25)
    });
    google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map,marker);
    });
    kml.setMap(map);
}

