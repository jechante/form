import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPushRecord } from 'app/shared/model/push-record.model';
import { PushRecordService } from './push-record.service';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from 'app/entities/wx-user';
import { IUserMatch } from 'app/shared/model/user-match.model';
import { UserMatchService } from 'app/entities/user-match';

@Component({
    selector: 'jhi-push-record-update',
    templateUrl: './push-record-update.component.html'
})
export class PushRecordUpdateComponent implements OnInit {
    private _pushRecord: IPushRecord;
    isSaving: boolean;

    wxusers: IWxUser[];

    usermatches: IUserMatch[];
    pushDateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private pushRecordService: PushRecordService,
        private wxUserService: WxUserService,
        private userMatchService: UserMatchService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pushRecord }) => {
            this.pushRecord = pushRecord;
        });
        this.wxUserService.query().subscribe(
            (res: HttpResponse<IWxUser[]>) => {
                this.wxusers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userMatchService.query().subscribe(
            (res: HttpResponse<IUserMatch[]>) => {
                this.usermatches = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.pushRecord.pushDateTime = moment(this.pushDateTime, DATE_TIME_FORMAT);
        if (this.pushRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.pushRecordService.update(this.pushRecord));
        } else {
            this.subscribeToSaveResponse(this.pushRecordService.create(this.pushRecord));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPushRecord>>) {
        result.subscribe((res: HttpResponse<IPushRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackWxUserById(index: number, item: IWxUser) {
        return item.id;
    }

    trackUserMatchById(index: number, item: IUserMatch) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get pushRecord() {
        return this._pushRecord;
    }

    set pushRecord(pushRecord: IPushRecord) {
        this._pushRecord = pushRecord;
        this.pushDateTime = moment(pushRecord.pushDateTime).format(DATE_TIME_FORMAT);
    }
}
