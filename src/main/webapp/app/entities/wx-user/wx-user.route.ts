import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from './wx-user.service';
import { WxUserComponent } from './wx-user.component';
import { WxUserDetailComponent } from './wx-user-detail.component';
import { WxUserUpdateComponent } from './wx-user-update.component';
import { WxUserDeletePopupComponent } from './wx-user-delete-dialog.component';
import { IWxUser } from 'app/shared/model/wx-user.model';
import {UserPropertyDemandComponent} from 'app/entities/wx-user/user-property-demand/user-property-demand.component';

@Injectable({ providedIn: 'root' })
export class WxUserResolve implements Resolve<IWxUser> {
    constructor(private service: WxUserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((wxUser: HttpResponse<WxUser>) => wxUser.body));
        }
        return of(new WxUser());
    }
}

export const wxUserRoute: Routes = [
    {
        path: 'wx-user',
        component: WxUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-user/:id/view',
        component: WxUserDetailComponent,
        resolve: {
            wxUser: WxUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-user/new',
        component: WxUserUpdateComponent,
        resolve: {
            wxUser: WxUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-user/:id/edit',
        component: WxUserUpdateComponent,
        resolve: {
            wxUser: WxUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wx-user/:id/property',
        component: UserPropertyDemandComponent,
        resolve: {
            wxUser: WxUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wxUserPopupRoute: Routes = [
    {
        path: 'wx-user/:id/delete',
        component: WxUserDeletePopupComponent,
        resolve: {
            wxUser: WxUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '小伊微信用户'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
