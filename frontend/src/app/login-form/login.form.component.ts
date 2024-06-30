import {Component, OnInit} from '@angular/core';
import {CustomHttpService} from "../services/custom-http.service";
import {map, Observable} from "rxjs";
import {CommonModule} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {StorageService} from "../services/storage.service";

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './login.form.component.html',
  styleUrl: './login.form.component.css'
})
export class LoginFormComponent implements OnInit {

  $url: Observable<string> | undefined;
  loginForm!: FormGroup;

  constructor(private readonly http: CustomHttpService, private readonly fb: FormBuilder, private readonly storageService: StorageService, private readonly router: Router) {
    this.getGoogleLoginURL();
  }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    })
  }

  getGoogleLoginURL() {
    this.$url = this.http.getURI().pipe(map(({url}) => url));
  }

  onSubmit() {
    if (this.loginForm?.valid) {
      this.http.login(this.loginForm.value).subscribe(tokenInfo => {
        this.storageService.addTokenInfo(tokenInfo);
        this.loginForm?.reset();
        this.router.navigateByUrl("/profile");
      })
    }
  }

}
