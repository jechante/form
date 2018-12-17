import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IUserDemand } from 'app/shared/model/user-demand.model';
import { UserDemandService } from './user-demand.service';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from 'app/entities/wx-user';
import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from 'app/entities/base-property';

@Component({
    selector: 'jhi-user-demand-update',
    templateUrl: './user-demand-update.component.html'
})
export class UserDemandUpdateComponent implements OnInit {
    private _userDemand: IUserDemand;
    isSaving: boolean;

    wxusers: IWxUser[];

    baseproperties: IBaseProperty[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private userDemandService: UserDemandService,
        private wxUserService: WxUserService,
        private basePropertyService: BasePropertyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userDemand }) => {
            this.userDemand = userDemand;
        });
        this.wxUserService.query().subscribe(
            (res: HttpResponse<IWxUser[]>) => {
                this.wxusers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.basePropertyService.query().subscribe(
            (res: HttpResponse<IBaseProperty[]>) => {
                this.baseproperties = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userDemand.id !== undefined) {
            this.subscribeToSaveResponse(this.userDemandService.update(this.userDemand));
        } else {
            this.subscribeToSaveResponse(this.userDemandService.create(this.userDemand));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserDemand>>) {
        result.subscribe((res: HttpResponse<IUserDemand>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBasePropertyById(index: number, item: IBaseProperty) {
        return item.id;
    }
    get userDemand() {
        return this._userDemand;
    }

    set userDemand(userDemand: IUserDemand) {
        this._userDemand = userDemand;
    }
}
