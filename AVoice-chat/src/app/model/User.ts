export class User {
	private uuid: string;
	private username: string;
	private password: string;
	private fullName: string;
	private mail: string;
	// tslint:disable-next-line:variable-name


	constructor(username: string, password: string, fullName: string, mail: string) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.mail = mail;
	}

	get _username(): string {
		return this.username;
	}

	set _username(value: string) {
		this.username = value;
	}

	get _password(): string {
		return this.password;
	}

	set _password(value: string) {
		this.password = value;
	}

	set _uuid(value: string) {
		this.uuid = value;
	}

	get _uuid(): string{
		return this.uuid;
	}
}
