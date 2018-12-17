import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWxUser } from 'app/shared/model/wx-user.model';

@Component({
    selector: 'jhi-wx-user-detail',
    templateUrl: './wx-user-detail.component.html'
})
export class WxUserDetailComponent implements OnInit {
    wxUser: IWxUser;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wxUser }) => {
            this.wxUser = wxUser;
        });
    }

    previousState() {
        window.history.back();
    }
}
