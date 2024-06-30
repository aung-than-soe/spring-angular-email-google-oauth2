import {Routes} from '@angular/router';
import {LoginFormComponent} from "./login-form/login.form.component";
import {UserProfileComponent} from "./user-profile/user-profile.component";
import {authGuard} from "./guards/auth.guard";
import {SignupFormComponent} from "./signup-form/signup.form.component";

export const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: 'prefix'
  },
  {
    path: "login",
    component: LoginFormComponent
  },
  {
    path: 'register',
    component: SignupFormComponent
  },
  {
    path: "profile",
    component: UserProfileComponent,
    canActivate: [authGuard]
  }
];
