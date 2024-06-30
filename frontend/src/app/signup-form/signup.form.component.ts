import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CustomHttpService} from "../services/custom-http.service";
import {StorageService} from "../services/storage.service";

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [
    AsyncPipe,
    RouterLink,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './signup.form.component.html',
  styleUrl: './signup.form.component.css'
})
export class SignupFormComponent implements OnInit {
  signupForm!: FormGroup;

  constructor(private readonly fb: FormBuilder, private readonly http: CustomHttpService, private readonly storageService: StorageService, private readonly router: Router) {
  }

  ngOnInit() {
    this.signupForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPass: ['', Validators.required]
    })
  }

  get firstName(): FormControl {
    return this.signupForm.get('fistName') as FormControl
  }

  get lastName(): FormControl {
    return this.signupForm.get('lastName') as FormControl
  }

  get username(): FormControl {
    return this.signupForm.get('username') as FormControl
  }

  get email(): FormControl {
    return this.signupForm.get('email') as FormControl
  }

  get password(): FormControl {
    return this.signupForm.get('password') as FormControl
  }

  get confirmPass(): FormControl {
    return this.signupForm.get('confirmPass') as FormControl
  }

  onSubmit() {
    if(!this.signupForm.valid) {
      window.alert("Please enter required fields.")
      return;
    }

    if(this.confirmPass.value !== this.password.value) {
      window.alert("Passwords mismatch");
      return;
    }
    const data = this.signupForm.value;
    data.confirmPass = undefined;
    this.http.register({...data}).subscribe(result => {
      this.storageService.addTokenInfo(result);
      this.router.navigateByUrl("/profile");
    })


  }
}
