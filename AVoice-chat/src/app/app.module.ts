import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {SidebarComponent} from './sidebar/sidebar.component';
import {ChatRoomComponent} from './chat-room/chat-room.component';
import {LoginComponent} from './login/login.component';
import {MessengerComponent} from './messenger/messenger.component';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {FormsModule} from '@angular/forms';
import {RxStompService} from '@stomp/ng2-stompjs';
import {RegisterComponent} from './register/register.component';
import {AuthGuardService} from './auth/AuthGuardService';
import {AuthService} from './auth/AuthService';
import {SharedService} from './services/SharedService';
import {HttpClientModule} from '@angular/common/http';
import {ConnectedWithService} from './services/ConnectedWithService';

const appRoutes: Routes = [
	// TODO de adaugat canActivate (cand nu se va mai comporta ciudat)
	{ path: 'register', component: RegisterComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'messenger', component: MessengerComponent },
	{path: '', redirectTo: 'login', pathMatch: 'full'},
	{path: '**', component: PageNotFoundComponent}
];

@NgModule({
	declarations: [
		AppComponent, SidebarComponent, ChatRoomComponent, LoginComponent, MessengerComponent, PageNotFoundComponent, RegisterComponent
	],
	imports: [
		BrowserModule, BrowserAnimationsModule, MatButtonModule, MatCardModule, MatIconModule, RouterModule,
		RouterModule.forRoot(appRoutes), HttpClientModule, FormsModule
	],
	exports: [RouterModule],
	// AuthService, AuthGuardService (WTF?)
	providers: [RxStompService, AuthService, AuthGuardService, SharedService, ConnectedWithService],
	bootstrap: [AppComponent]
})
export class AppModule { }
