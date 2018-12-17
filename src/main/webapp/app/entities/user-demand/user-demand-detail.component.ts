import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserDemand } from 'app/shared/model/user-demand.model';

@Component({
    selector: 'jhi-user-demand-detail',
    templateUrl: './user-demand-detail.component.html'
})
export class UserDemandDetailComponent implements OnInit {
    userDemand: IUserDemand;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userDemand }) => {
            this.userDemand = userDemand;
        });
    }

    previousState() {
        window.history.back();
    }
}
