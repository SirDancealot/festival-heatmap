import { Injectable } from '@angular/core';
import {catchError, retry} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestApiService {

url = 'http://dist.saluton.dk:18512';

  constructor(private http: HttpClient) {
  }

  public getLatLng() {
    return this.http.get(this.url + '/locationSeperate');
  }

  public onCreatePost(latLng: google.maps.LatLng, id: string): Observable<any> {
    return this.http.post<any>(
      this.url + '/saveUser',
      {
        token: id,
        latitude: latLng.lat(),
        longitude: latLng.lng()
      });
  }

  public onDeletePost(id: string): Observable<any> {
    const params = new HttpParams().set('token', id);

    return this.http.delete(this.url + '/deleteUser', { params });
  }

  public getPrevPosition(id: string) {
    const params = new HttpParams().set('token', id);

    return this.http.get<any>(this.url + '/userCoor', {params});
  }
}

