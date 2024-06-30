import {Injectable} from '@angular/core';
import {ITokenInfo} from "../interfaces/token.interface";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() {
  }

  addTokenInfo(tokenInfo: ITokenInfo) {
    Object.entries(tokenInfo).forEach(([k, v]) => {
      sessionStorage.setItem(k, v);
    })
  }

  getToken() {
    return sessionStorage.getItem('access_token');
  }

  getAuthType() {
    return sessionStorage.getItem("auth_type") || '';
  }
}
