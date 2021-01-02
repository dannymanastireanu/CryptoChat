import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {User} from '../model/User';

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

	constructor(private router: Router, private http: HttpClient) { }

	ngOnInit(): void {
	}

	registerRequest(fullname: string, mail: string, username: string, password: string): void {
		// TODO Validate user input
		// Register in database
		const user: User = new User(username, password, fullname, mail);

		const obsuser = this.http.post('http://localhost:8080/avoice/register', user, {responseType: 'text'}).subscribe(
			data => {
				if (data === 'Saved') {
					this.router.navigate(['/login']);
				} else {
					this.router.navigate(['/register']);
					alert('Wrong request');
				}
			}
		);

		// this.router.navigate(['login']);
	}
}
