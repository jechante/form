import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserBase } from 'app/shared/model/user-base.model';

@Component({
    selector: 'jhi-user-base-detail',
    templateUrl: './user-base-detail.component.html'
})
export class UserBaseDetailComponent implements OnInit {
    userBase: IUserBase;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userBase }) => {
            this.userBase = userBase;
        });
    }

    previousState() {
        window.history.back();
    }
}
