import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {User} from '../model/User';
import {Friend} from '../model/Friend';

@Injectable()
export class ConnectedWithService {

	private friend = new BehaviorSubject<Friend>(null);
	sharedFriend = this.friend.asObservable();

	private messages = new BehaviorSubject<{}>(null);
	sharedMessages = this.messages.asObservable();

	constructor() { }

	nextFriend(user: Friend): void {
		this.friend.next(user);
	}

	nextMessages(messages: {}): void {
		this.messages.next(messages);
	}

}
