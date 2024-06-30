import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../services/storage.service";

export const authGuard: CanActivateFn = (route, state) => {
  return inject(StorageService).getToken()
    ? true
    : inject(Router).createUrlTree(['/login']);
};
