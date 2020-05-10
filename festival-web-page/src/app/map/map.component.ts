import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { GoogleMapsModule } from '@angular/google-maps';
import { GoogleMap} from '@angular/google-maps';
import {AuthenticationService} from '../services/authentication.service';
import { AngularFireAuth } from 'angularfire2/auth';
import * as firebase from 'firebase/app';
import { HttpClient} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {RestApiService} from '../services/rest-api.service';
import {Observable, range} from 'rxjs';
import {compareSegments} from '@angular/compiler-cli/ngcc/src/sourcemaps/segment_marker';
import {inspect} from 'util';
import {MapRestriction} from '@agm/core/services/google-maps-types';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})

export class MapComponent implements OnInit, AfterViewInit {

  @ViewChild('map', { static: true }) map: GoogleMap;

  zoom = 14.5;
  center = { lat: 55.615304, lng: 12.081614 };
  options: google.maps.MapOptions = {
    mapTypeId: 'satellite',
    zoomControl: true,
    scrollwheel: true,
    disableDoubleClickZoom: true,
    maxZoom: 20,
    minZoom: 13,
    restriction: {
      latLngBounds: new google.maps.LatLngBounds(
        {lat: 55.604060, lng: 12.045801},
        {lat: 55.630119, lng: 12.108985}
      ),
      strictBounds: true
    }
  };

  marker = null;
  prevMarker = null;
  prevPos: google.maps.LatLng = null;
  location: google.maps.LatLng = null;
  heatmap: google.maps.visualization.HeatmapLayer = null;

  constructor(private restService: RestApiService,
              private authService: AuthenticationService) { }

  ngOnInit() {
   /* navigator.geolocation.getCurrentPosition(position => {
      this.center = {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      };
    });*/
  }

  zoomIn() {
    if (this.zoom < this.options.maxZoom) { this.zoom++; }
  }

  zoomOut() {
    if (this.zoom > this.options.minZoom) { this.zoom--; }
  }


  ngAfterViewInit(): void {
    this.restService.getLatLng().subscribe((data: any[]) => {
      this.updateHeatmap(data);
    });

    this.authService.getId().then((id: string) => {
      this.restService.getPrevPosition(id).subscribe((coords: any) => {
        this.updateLastMarker(coords);
      });
    });

    // bounds of the desired area
    const allowedBounds = new google.maps.LatLngBounds(
      new google.maps.LatLng(55.62162786584009, 12.066844448566622),
      new google.maps.LatLng(55.60921441511262, 12.092343954511193)
    );
    let lastValidCenter = this.map._googleMap.getCenter();

    google.maps.event.addListener(this.map._googleMap, 'center_changed', (map = this.map._googleMap) => {
      if (allowedBounds.contains(map.getCenter())) {
        // still within valid bounds, so save the last valid position
        lastValidCenter = map.getCenter();
        return;
      }

      // not valid anymore => return to last valid position
      map.panTo(lastValidCenter);
    });
  }

  onClick($event: google.maps.MouseEvent | google.maps.IconMouseEvent) {
    if (this.marker != null) {
      this.marker.setMap(null);
    }

    this.restService.getLatLng().subscribe((data: any[]) => {
      console.log(data);
    });

    this.marker = new google.maps.Marker({map: this.map._googleMap, position: $event.latLng});
    this.location = $event.latLng;
  }

  updateHeatmap(data: Array<any>) {
    const heatmapData = new Array<google.maps.LatLng>(data.length);
    for (let i = 0; i < data.length; i++){
      heatmapData[i] = new google.maps.LatLng(data[i].lat, data[i].lng);
    }

    if (this.heatmap == null) {
    this.heatmap = new google.maps.visualization.HeatmapLayer({
      data: heatmapData,
    });

    this.heatmap.setMap(this.map._googleMap);
    } else {
      this.heatmap.setData(data);
    }
  }

  postGeoData(){
    // this.restService.onCreatePost(this.location, this.authService.getId()).subscribe(() => {console.log('done'); });
    this.prevPos = this.location;
    this.updateLastMarker();

    return this.authService.getId().then(( value: string ) => {
      this.restService.onCreatePost(this.location, value).subscribe(() => {console.log('Position Updated'); });
    });
  }

  deletePrevPosition() {
    this.authService.getId().then(( id: string ) => {
      this.restService.onDeletePost(id).subscribe(() => {console.log('done'); });
    });
  }

  updateLastMarker(coords?: any) {
    if (coords != null) {
      console.log(coords);
      if (this.prevMarker != null) {
        this.prevMarker.setMap(null);
      }
      this.prevPos = new google.maps.LatLng(coords.lat, coords.lng);
      this.prevMarker = new google.maps.Marker(
        {map: this.map._googleMap, position: this.prevPos, icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'});
    } else {
      if (this.prevMarker != null) {
        this.prevMarker.setMap(null);
      }
      this.prevMarker = new google.maps.Marker({map: this.map._googleMap, position: this.prevPos, icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'});
    }
    console.log(this.prevMarker);
  }
}
