import {Component, OnInit} from '@angular/core';
import {User} from '../model/User';
import {LoginComponent} from '../login/login.component';
import {SharedService} from '../services/SharedService';


@Component({
	selector: 'app-messenger',
	templateUrl: './messenger.component.html',
	styleUrls: ['./messenger.component.css']
})
export class MessengerComponent implements OnInit {
	clickedRoom = true;
	private peoples;

	private user: User = JSON.parse(localStorage.getItem('client'));
	private uuid: string = JSON.parse(localStorage.getItem('client_uuid'));

	message: User;

	constructor(private sharedService: SharedService) {
	}

	ngOnInit(): void {
		// this.sharedService.sharedMessage.subscribe(user => this.user = user);
		// console.log(`Mesaj de la login: ${this.user._username}`);
	}

}
