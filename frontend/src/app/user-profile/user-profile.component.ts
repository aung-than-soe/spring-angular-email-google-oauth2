import {Component, OnInit} from '@angular/core';
import {CustomHttpService} from "../services/custom-http.service";
import {map, Observable} from "rxjs";
import {IUserProfile} from "../interfaces/user-profile.interface";
import {CommonModule} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {

  $userProfile: Observable<IUserProfile> | undefined;

  constructor(private readonly http: CustomHttpService, private readonly router: Router) {
  }

  ngOnInit() {
    this.$userProfile = this.http.getUserProfile();
  }

  logout() {
    sessionStorage.clear();
    this.router.navigateByUrl("/");
  }
}
