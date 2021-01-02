import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {User} from '../model/User';

@Injectable()
export class SharedService {

	private message = new BehaviorSubject<User>(null);
	sharedMessage = this.message.asObservable();

	constructor() { }

	nextMessage(user: User): void {
		this.message.next(user);
	}

}
