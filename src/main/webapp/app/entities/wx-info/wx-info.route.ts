import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WxInfo } from 'app/shared/model/wx-info.model';
import { WxInfoService } from './wx-info.service';
import { WxInfoComponent } from './wx-info.component';
import { WxInfoDetailComponent } from './wx-info-detail.component';
import { WxInfoUpdateComponent } from './wx-info-update.component';
import { WxInfoDeletePopupComponent } from './wx-info-delete-dialog.component';
import { IWxInfo } from 'app/shared/model/wx-info.model';

@Injectable({ providedIn: 'root' })
export class WxInfoResolve implements Resolve<IWxInfo> {
    constructor(private service: WxInfoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((wxInfo: HttpResponse<WxInfo>) => wxInfo.body));
        }
        return of(new WxInfo());
    }
}

export const wxInfoRoute: Routes = [
    {
        path: 'wx-info',
        component: WxInfoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WxInfos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-info/:id/view',
        component: WxInfoDetailComponent,
        resolve: {
            wxInfo: WxInfoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WxInfos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-info/new',
        component: WxInfoUpdateComponent,
        resolve: {
            wxInfo: WxInfoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WxInfos'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-info/:id/edit',
        component: WxInfoUpdateComponent,
        resolve: {
            wxInfo: WxInfoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WxInfos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wxInfoPopupRoute: Routes = [
    {
        path: 'wx-info/:id/delete',
        component: WxInfoDeletePopupComponent,
        resolve: {
            wxInfo: WxInfoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WxInfos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
