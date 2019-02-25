import { Component, OnInit } from '@angular/core';
import {IWxUser} from "app/shared/model/wx-user.model";
import {IBroker} from "app/shared/model/broker.model";
import {JhiAlertService} from "ng-jhipster";
import {WxUserService} from "app/entities/wx-user";
import {BrokerService} from "app/entities/broker";
import {ActivatedRoute} from "@angular/router";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import * as moment from 'moment';
import {DATE_TIME_FORMAT} from "app/shared";
import {Observable} from "rxjs";
import {FormyType, IBaseProperty} from "app/shared/model/base-property.model";
import {IUserProperty, UserProperty} from "app/shared/model/user-property.model";
import {IUserDemand, UserDemand} from "app/shared/model/user-demand.model";
import {BasePropertyService} from "app/entities/base-property";

@Component({
  selector: 'jhi-user-property-demand',
  templateUrl: './user-property-demand.component.html',
  styleUrls: ['user-property-demand.css']
})
export class UserPropertyDemandComponent implements OnInit {

    private _wxUser: IWxUser;
    isSaving: boolean;

    baseProperties: IBaseProperty[];
    // userProperties: IUserProperty[];
    // userDemands: IUserDemand[];
    registerDateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private wxUserService: WxUserService,
        private basePropertyService: BasePropertyService,
        private brokerService: BrokerService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wxUser }) => {
            this.wxUser = wxUser;
        });

        // todo 通过resolve来加载数据
        this.basePropertyService.findUserPropertyDemands(this.wxUser.id).subscribe(
            (res: HttpResponse<IBaseProperty[]>) => {
                this.baseProperties = res.body;
                // 初始化为空的属性与需求值对象
                this.baseProperties.forEach(baseProperty => {
                    if (baseProperty.propertyValues.length === 0) {
                        baseProperty.propertyValues.push(new UserProperty());
                    }
                    if (baseProperty.demandValues.length === 0) {
                        baseProperty.demandValues.push(new UserDemand());
                    }
                });
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        // 保存前，先设置wxUser
        this.baseProperties.forEach(baseProperty => {
            baseProperty.propertyValues.forEach(userP => userP.wxUser = this.wxUser);
            baseProperty.demandValues.forEach(userD => userD.wxUser = this.wxUser);
        });
        this.subscribeToSaveResponse(this.basePropertyService.saveUserPropertyDemands(this.baseProperties));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBaseProperty[]>>) {
        result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    get wxUser() {
        return this._wxUser;
    }

    set wxUser(wxUser: IWxUser) {
        this._wxUser = wxUser;
        this.registerDateTime = moment(wxUser.registerDateTime).format(DATE_TIME_FORMAT);
    }

    propertyDemo(formType: FormyType, option) {
        switch (formType) {
            case FormyType.LOCATION:
                return '{"province":"xx省","city":"xx市","district":"xx区","street":"xx街道"}';
            case FormyType.MANYTOMANY:
                return '["aa","bb"]';
            case FormyType.ONETOMANY:
                if (option === 'property') {
                    return '123(数字)、"abc"(文本)、"1994-01-30"(日期)';
                } else {
                    return '["aa","bb"]';
                }
            case FormyType.ONETOONE:
                return '123(数字)、"abc"(文本)、"1994-01-30"(日期)';
            case FormyType.ONETORANGE:
                if (option === 'property') {
                    return '123(数字)、"abc"(文本)、"1994-01-30"(日期)';
                } else {
                    return '"123-456"';
                }
            case FormyType.OTHER:
                if (option === 'property') {
                    return '["https://files.jinshuju.net/1","https://files.jinshuju.net/2"](头像)、"微信：xxxx"(联系方式)';
                } else {
                    return '非填项';
                }
        }
    }

}
