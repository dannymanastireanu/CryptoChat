import {Component, Input, OnInit, Output} from '@angular/core';
import {Message} from '../model/Message';
import {User} from '../model/User';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {Friend} from '../model/Friend';
import {NgForm} from '@angular/forms';
import {ConnectedWithService} from '../services/ConnectedWithService';

@Component({
	selector: 'app-sidebar',
	templateUrl: './sidebar.component.html',
	styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

	public user;
	public friendsList: Array<Friend>;
	public personNameSearchText: string;
	public messageHistory: {};

	constructor(private http: HttpClient, private connectWithService: ConnectedWithService) {
		this.personNameSearchText = '';
		this.user = JSON.parse(localStorage.getItem('client'));
		// init with server req.
		this.friendsList = [];
		this.messageHistory = {};

	}

	ngOnInit(): void {
		this.refreshFriendList();
	}

	openConversation(friend: Friend): void {
		this.connectWithService.nextFriend(friend);
		this.setHistoryMessage(friend).then(() => {
			console.log('Update la niste mesaje');
		}, () => {console.log('eroare'); });
		this.connectWithService.nextMessages(this.messageHistory);
		console.log('Conectat cu: ' + friend.fullName);
	}

	async setHistoryMessage(myFriend: Friend): Promise<void> {
		if (this.messageHistory[myFriend.username] === undefined) {
			this.messageHistory[myFriend.username] = [];
			const headers = new HttpHeaders();
			headers.set('Content-Type', 'application/json; charset=utf-8');
			await this.http.get('http://localhost:8080/avoice/messages/' + myFriend.username,
				{responseType: 'text', withCredentials: true}).subscribe( data => {
				const multeMesaje = (JSON.parse(data) as Message);
				// tslint:disable-next-line:forin
				for (const i in multeMesaje){
					this.messageHistory[myFriend.username].push(
						new Message(multeMesaje[i].from, multeMesaje[i].to, multeMesaje[i].body,
							multeMesaje[i].timestamp, multeMesaje[i].type, multeMesaje[i].cachePath, null));
				}
			});
		}
	}

	base64ToArrayBuffer(base64): ArrayBuffer {
		const binaryString = window.atob(base64);
		const len = binaryString.length;
		const bytes = new Uint8Array(len);
		for (let i = 0; i < len; i++) {
			bytes[i] = binaryString.charCodeAt(i);
		}
		return bytes.buffer;
	}

	refreshFriendList(): void {
		const headers = new HttpHeaders();
		headers.set('Content-Type', 'application/json; charset=utf-8');
		this.http.get('http://localhost:8080/avoice/friends', {responseType: 'text', withCredentials: true}).subscribe(data => {
			this.friendsList = JSON.parse(data);
		});
	}

	searchPerson(f: NgForm): void {
		this.personNameSearchText = '';
	}

	generateInviteCode(): void {
		alert('Successfully generated');
		console.log('Generate 1/3');
	}
}

