import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IWxInfo } from 'app/shared/model/wx-info.model';
import { WxInfoService } from './wx-info.service';

@Component({
    selector: 'jhi-wx-info-update',
    templateUrl: './wx-info-update.component.html'
})
export class WxInfoUpdateComponent implements OnInit {
    private _wxInfo: IWxInfo;
    isSaving: boolean;

    constructor(private wxInfoService: WxInfoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wxInfo }) => {
            this.wxInfo = wxInfo;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.wxInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.wxInfoService.update(this.wxInfo));
        } else {
            this.subscribeToSaveResponse(this.wxInfoService.create(this.wxInfo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWxInfo>>) {
        result.subscribe((res: HttpResponse<IWxInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get wxInfo() {
        return this._wxInfo;
    }

    set wxInfo(wxInfo: IWxInfo) {
        this._wxInfo = wxInfo;
    }
}
