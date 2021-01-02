import {Component, Injectable, OnInit} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/User';
import {BehaviorSubject, Observable} from 'rxjs';
import {SharedService} from '../services/SharedService';
import {stringify} from 'querystring';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})


export class LoginComponent implements OnInit, CanActivate {

	private user: User;

	// message: User;

	constructor(private router: Router, private http: HttpClient, private sharedService: SharedService) { }


	ngOnInit(): void {
	}


	public loginRequest(username: string, password: string): any {
		this.user = new User(username, password, '', '');

		const obsuser = this.http.post('http://localhost:8080/avoice/login', this.user,
			{responseType: 'text', withCredentials: true}).subscribe(
			data => {
				if (data !== 'void') {
					if (data === '400'){
						alert('Server is close');
					} else {
						localStorage.setItem('client', JSON.stringify(this.user));
						this.user._uuid = data;
						this.router.navigate(['/messenger']);
					}
				} else {
					alert('Wrong user || password');
				}
			}
		);
	}


	// tslint:disable-next-line:max-line-length
	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		if (localStorage.getItem('client_uuid') === null){
			// TODO make an reguest sa vad ca e actuala si poate fi folosita
			this.router.navigate(['/login']);
			return false;
		} else {
			this.router.navigate(['/messenger']);
			return true;
		}
	}
}


