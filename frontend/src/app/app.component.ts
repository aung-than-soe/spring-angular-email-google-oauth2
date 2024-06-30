import { Component } from '@angular/core';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {CustomHttpService} from "./services/custom-http.service";
import {StorageService} from "./services/storage.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  constructor(private readonly route: ActivatedRoute, private readonly http: CustomHttpService, private readonly router: Router, private readonly storageService: StorageService) {
  }
  ngOnInit(): void {
    this.route.queryParams
      .subscribe(params => {
          if (params["code"] !== undefined) {
            this.http.getToken(params["code"]).subscribe((tokenInfo) => {
              if(tokenInfo) {
                this.storageService.addTokenInfo(tokenInfo);
                this.router.navigateByUrl("/profile")
              }
            });
          }
        }
      );
  }
}
