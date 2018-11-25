import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWxInfo } from 'app/shared/model/wx-info.model';

@Component({
    selector: 'jhi-wx-info-detail',
    templateUrl: './wx-info-detail.component.html'
})
export class WxInfoDetailComponent implements OnInit {
    wxInfo: IWxInfo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wxInfo }) => {
            this.wxInfo = wxInfo;
        });
    }

    previousState() {
        window.history.back();
    }
}
