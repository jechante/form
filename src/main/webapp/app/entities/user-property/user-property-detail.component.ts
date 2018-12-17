import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserProperty } from 'app/shared/model/user-property.model';

@Component({
    selector: 'jhi-user-property-detail',
    templateUrl: './user-property-detail.component.html'
})
export class UserPropertyDetailComponent implements OnInit {
    userProperty: IUserProperty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userProperty }) => {
            this.userProperty = userProperty;
        });
    }

    previousState() {
        window.history.back();
    }
}
