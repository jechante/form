import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IUserMatch } from 'app/shared/model/user-match.model';
import { UserMatchService } from './user-match.service';
import { IPushRecord } from 'app/shared/model/push-record.model';
import { PushRecordService } from 'app/entities/push-record';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from 'app/entities/algorithm';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from 'app/entities/wx-user';

@Component({
    selector: 'jhi-user-match-update',
    templateUrl: './user-match-update.component.html'
})
export class UserMatchUpdateComponent implements OnInit {
    private _userMatch: IUserMatch;
    isSaving: boolean;

    pushrecords: IPushRecord[];

    algorithms: IAlgorithm[];

    wxusers: IWxUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private userMatchService: UserMatchService,
        private pushRecordService: PushRecordService,
        private algorithmService: AlgorithmService,
        private wxUserService: WxUserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userMatch }) => {
            this.userMatch = userMatch;
        });
        this.pushRecordService.query().subscribe(
            (res: HttpResponse<IPushRecord[]>) => {
                this.pushrecords = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.algorithmService.query().subscribe(
            (res: HttpResponse<IAlgorithm[]>) => {
                this.algorithms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.wxUserService.query().subscribe(
            (res: HttpResponse<IWxUser[]>) => {
                this.wxusers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userMatch.id !== undefined) {
            this.subscribeToSaveResponse(this.userMatchService.update(this.userMatch));
        } else {
            this.subscribeToSaveResponse(this.userMatchService.create(this.userMatch));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserMatch>>) {
        result.subscribe((res: HttpResponse<IUserMatch>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPushRecordById(index: number, item: IPushRecord) {
        return item.id;
    }

    trackAlgorithmById(index: number, item: IAlgorithm) {
        return item.id;
    }

    trackWxUserById(index: number, item: IWxUser) {
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
    get userMatch() {
        return this._userMatch;
    }

    set userMatch(userMatch: IUserMatch) {
        this._userMatch = userMatch;
    }
}
