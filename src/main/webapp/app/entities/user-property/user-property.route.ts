import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserProperty } from 'app/shared/model/user-property.model';
import { UserPropertyService } from './user-property.service';
import { UserPropertyComponent } from './user-property.component';
import { UserPropertyDetailComponent } from './user-property-detail.component';
import { UserPropertyUpdateComponent } from './user-property-update.component';
import { UserPropertyDeletePopupComponent } from './user-property-delete-dialog.component';
import { IUserProperty } from 'app/shared/model/user-property.model';

@Injectable({ providedIn: 'root' })
export class UserPropertyResolve implements Resolve<IUserProperty> {
    constructor(private service: UserPropertyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((userProperty: HttpResponse<UserProperty>) => userProperty.body));
        }
        return of(new UserProperty());
    }
}

export const userPropertyRoute: Routes = [
    {
        path: 'user-property',
        component: UserPropertyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'UserProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-property/:id/view',
        component: UserPropertyDetailComponent,
        resolve: {
            userProperty: UserPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-property/new',
        component: UserPropertyUpdateComponent,
        resolve: {
            userProperty: UserPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-property/:id/edit',
        component: UserPropertyUpdateComponent,
        resolve: {
            userProperty: UserPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProperties'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userPropertyPopupRoute: Routes = [
    {
        path: 'user-property/:id/delete',
        component: UserPropertyDeletePopupComponent,
        resolve: {
            userProperty: UserPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProperties'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
