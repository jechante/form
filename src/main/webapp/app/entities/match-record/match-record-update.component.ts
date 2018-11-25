import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMatchRecord } from 'app/shared/model/match-record.model';
import { MatchRecordService } from './match-record.service';

@Component({
    selector: 'jhi-match-record-update',
    templateUrl: './match-record-update.component.html'
})
export class MatchRecordUpdateComponent implements OnInit {
    private _matchRecord: IMatchRecord;
    isSaving: boolean;

    constructor(private matchRecordService: MatchRecordService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ matchRecord }) => {
            this.matchRecord = matchRecord;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.matchRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.matchRecordService.update(this.matchRecord));
        } else {
            this.subscribeToSaveResponse(this.matchRecordService.create(this.matchRecord));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMatchRecord>>) {
        result.subscribe((res: HttpResponse<IMatchRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get matchRecord() {
        return this._matchRecord;
    }

    set matchRecord(matchRecord: IMatchRecord) {
        this._matchRecord = matchRecord;
    }
}
