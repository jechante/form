import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPushRecord } from 'app/shared/model/push-record.model';

@Component({
    selector: 'jhi-push-record-detail',
    templateUrl: './push-record-detail.component.html'
})
export class PushRecordDetailComponent implements OnInit {
    pushRecord: IPushRecord;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pushRecord }) => {
            this.pushRecord = pushRecord;
        });
    }

    previousState() {
        window.history.back();
    }
}
