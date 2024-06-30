import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {StorageService} from "./storage.service";
import {ITokenInfo} from "../interfaces/token.interface";
import {ISignUpForm} from "../interfaces/signup-form.interface";
import {IUserProfile} from "../interfaces/user-profile.interface";

@Injectable({
  providedIn: 'root'
})
export class CustomHttpService {
  baseURI: string = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private readonly storageService: StorageService) {
  }

  getURI(): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${this.baseURI}/auth/url`).pipe(catchError(err => throwError(() => {
      console.error('Error loading url => ' + err);
      return err;
    })))
  }

  getToken(code: string): Observable<ITokenInfo> {
    return this.http.get<ITokenInfo>(`${this.baseURI}/auth/callback?code=${code}`).pipe(catchError(err => throwError(() => {
      console.error('Error loading url => ' + err);
      return err;
    })))
  }

  getUserProfile(): Observable<IUserProfile> {
    const type = this.storageService.getAuthType();
    const headers = new HttpHeaders({
      "Authorization": `Bearer ${this.storageService.getToken()}`,
      "auth": type
    });
    return this.http.get<IUserProfile>(`${this.baseURI}/profile`, { headers: headers}).pipe(catchError(err => throwError(() => {
      console.error('Error loading url => ' + err);
      return err;
    })))
  }

  register(data: ISignUpForm): Observable<ITokenInfo> {
    return this.http.post<ITokenInfo>(`${this.baseURI}/createaccount`, {...data}).pipe(catchError(err => throwError(() => {
      console.error('Error loading url => ' + err);
      return err;
    })))
  }

  login(data: { email: string, password: string}) {
    return this.http.post<ITokenInfo>(`${this.baseURI}/login`, {...data}).pipe(catchError(err => throwError(() => {
      console.error('Error loading url => ' + err);
      return err;
    })))
  }
}
