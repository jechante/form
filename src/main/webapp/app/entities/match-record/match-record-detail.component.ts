import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchRecord } from 'app/shared/model/match-record.model';

@Component({
    selector: 'jhi-match-record-detail',
    templateUrl: './match-record-detail.component.html'
})
export class MatchRecordDetailComponent implements OnInit {
    matchRecord: IMatchRecord;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ matchRecord }) => {
            this.matchRecord = matchRecord;
        });
    }

    previousState() {
        window.history.back();
    }
}
