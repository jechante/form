import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserMatch } from 'app/shared/model/user-match.model';

@Component({
    selector: 'jhi-user-match-detail',
    templateUrl: './user-match-detail.component.html'
})
export class UserMatchDetailComponent implements OnInit {
    userMatch: IUserMatch;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userMatch }) => {
            this.userMatch = userMatch;
        });
    }

    previousState() {
        window.history.back();
    }
}
