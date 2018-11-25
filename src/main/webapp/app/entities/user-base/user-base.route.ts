import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserBase } from 'app/shared/model/user-base.model';
import { UserBaseService } from './user-base.service';
import { UserBaseComponent } from './user-base.component';
import { UserBaseDetailComponent } from './user-base-detail.component';
import { UserBaseUpdateComponent } from './user-base-update.component';
import { UserBaseDeletePopupComponent } from './user-base-delete-dialog.component';
import { IUserBase } from 'app/shared/model/user-base.model';

@Injectable({ providedIn: 'root' })
export class UserBaseResolve implements Resolve<IUserBase> {
    constructor(private service: UserBaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((userBase: HttpResponse<UserBase>) => userBase.body));
        }
        return of(new UserBase());
    }
}

export const userBaseRoute: Routes = [
    {
        path: 'user-base',
        component: UserBaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserBases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-base/:id/view',
        component: UserBaseDetailComponent,
        resolve: {
            userBase: UserBaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserBases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-base/new',
        component: UserBaseUpdateComponent,
        resolve: {
            userBase: UserBaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserBases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-base/:id/edit',
        component: UserBaseUpdateComponent,
        resolve: {
            userBase: UserBaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserBases'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userBasePopupRoute: Routes = [
    {
        path: 'user-base/:id/delete',
        component: UserBaseDeletePopupComponent,
        resolve: {
            userBase: UserBaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserBases'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
