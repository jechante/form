import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserMatch } from 'app/shared/model/user-match.model';
import { UserMatchService } from './user-match.service';
import { UserMatchComponent } from './user-match.component';
import { UserMatchDetailComponent } from './user-match-detail.component';
import { UserMatchUpdateComponent } from './user-match-update.component';
import { UserMatchDeletePopupComponent } from './user-match-delete-dialog.component';
import { IUserMatch } from 'app/shared/model/user-match.model';

@Injectable({ providedIn: 'root' })
export class UserMatchResolve implements Resolve<IUserMatch> {
    constructor(private service: UserMatchService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((userMatch: HttpResponse<UserMatch>) => userMatch.body));
        }
        return of(new UserMatch());
    }
}

export const userMatchRoute: Routes = [
    {
        path: 'user-match',
        component: UserMatchComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'UserMatches'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-match/:id/view',
        component: UserMatchDetailComponent,
        resolve: {
            userMatch: UserMatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserMatches'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-match/new',
        component: UserMatchUpdateComponent,
        resolve: {
            userMatch: UserMatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserMatches'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-match/:id/edit',
        component: UserMatchUpdateComponent,
        resolve: {
            userMatch: UserMatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserMatches'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userMatchPopupRoute: Routes = [
    {
        path: 'user-match/:id/delete',
        component: UserMatchDeletePopupComponent,
        resolve: {
            userMatch: UserMatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserMatches'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
