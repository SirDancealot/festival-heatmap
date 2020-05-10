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

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit{

  @ViewChild('map', { static: false}) map: GoogleMap;

  zoom = 12;
  center: google.maps.LatLngLiteral;
  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid',
    zoomControl: false,
    scrollwheel: false,
    disableDoubleClickZoom: true,
    maxZoom: 15,
    minZoom: 8,
  };

  marker = null;
  location: google.maps.LatLng = null;
  heatmap: google.maps.visualization.HeatmapLayer = null;

  constructor(private restService: RestApiService,
              private authService: AuthenticationService) { }

  ngOnInit() {
    navigator.geolocation.getCurrentPosition(position => {
      this.center = {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      };
    });
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
    this.authService.getId().then(( value: string ) => {
      this.restService.onCreatePost(this.location, value).subscribe(() => {console.log('done'); });
    });
  }

  deletePrevPosition() {
    /*this.authService.getId().then(( id: string ) => {
      this.restService.onDeletePost(id).subscribe(() => {console.log('done'); });
    });
     */
    this.restService.onDeletePost(this.authService.getEmail()).subscribe(() => {console.log('done'); });
  }
}
