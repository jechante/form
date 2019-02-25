import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserDemand } from 'app/shared/model/user-demand.model';
import { UserDemandService } from './user-demand.service';
import { UserDemandComponent } from './user-demand.component';
import { UserDemandDetailComponent } from './user-demand-detail.component';
import { UserDemandUpdateComponent } from './user-demand-update.component';
import { UserDemandDeletePopupComponent } from './user-demand-delete-dialog.component';
import { IUserDemand } from 'app/shared/model/user-demand.model';

@Injectable({ providedIn: 'root' })
export class UserDemandResolve implements Resolve<IUserDemand> {
    constructor(private service: UserDemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((userDemand: HttpResponse<UserDemand>) => userDemand.body));
        }
        return of(new UserDemand());
    }
}

export const userDemandRoute: Routes = [
    {
        path: 'user-demand',
        component: UserDemandComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: '用户需求'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-demand/:id/view',
        component: UserDemandDetailComponent,
        resolve: {
            userDemand: UserDemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '用户需求'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-demand/new',
        component: UserDemandUpdateComponent,
        resolve: {
            userDemand: UserDemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '用户需求'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-demand/:id/edit',
        component: UserDemandUpdateComponent,
        resolve: {
            userDemand: UserDemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '用户需求'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userDemandPopupRoute: Routes = [
    {
        path: 'user-demand/:id/delete',
        component: UserDemandDeletePopupComponent,
        resolve: {
            userDemand: UserDemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '用户需求'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
