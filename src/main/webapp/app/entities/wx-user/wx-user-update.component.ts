import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from './wx-user.service';
import { IBroker } from 'app/shared/model/broker.model';
import { BrokerService } from 'app/entities/broker';

@Component({
    selector: 'jhi-wx-user-update',
    templateUrl: './wx-user-update.component.html'
})
export class WxUserUpdateComponent implements OnInit {
    private _wxUser: IWxUser;
    isSaving: boolean;

    brokers: IBroker[];
    registerDateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private wxUserService: WxUserService,
        private brokerService: BrokerService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wxUser }) => {
            this.wxUser = wxUser;
        });
        this.brokerService.query().subscribe(
            (res: HttpResponse<IBroker[]>) => {
                this.brokers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.wxUser.registerDateTime = moment(this.registerDateTime, DATE_TIME_FORMAT);
        if (this.wxUser.id !== undefined) {
            this.subscribeToSaveResponse(this.wxUserService.update(this.wxUser));
        } else {
            this.subscribeToSaveResponse(this.wxUserService.create(this.wxUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWxUser>>) {
        result.subscribe((res: HttpResponse<IWxUser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBrokerById(index: number, item: IBroker) {
        return item.id;
    }
    get wxUser() {
        return this._wxUser;
    }

    set wxUser(wxUser: IWxUser) {
        this._wxUser = wxUser;
        this.registerDateTime = moment(wxUser.registerDateTime).format(DATE_TIME_FORMAT);
    }
}
