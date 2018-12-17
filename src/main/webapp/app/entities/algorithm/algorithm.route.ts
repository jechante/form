import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Algorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';
import { AlgorithmComponent } from './algorithm.component';
import { AlgorithmDetailComponent } from './algorithm-detail.component';
import { AlgorithmUpdateComponent } from './algorithm-update.component';
import { AlgorithmDeletePopupComponent } from './algorithm-delete-dialog.component';
import { IAlgorithm } from 'app/shared/model/algorithm.model';

@Injectable({ providedIn: 'root' })
export class AlgorithmResolve implements Resolve<IAlgorithm> {
    constructor(private service: AlgorithmService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((algorithm: HttpResponse<Algorithm>) => algorithm.body));
        }
        return of(new Algorithm());
    }
}

export const algorithmRoute: Routes = [
    {
        path: 'algorithm',
        component: AlgorithmComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'algorithm/:id/view',
        component: AlgorithmDetailComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'algorithm/new',
        component: AlgorithmUpdateComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'algorithm/:id/edit',
        component: AlgorithmUpdateComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const algorithmPopupRoute: Routes = [
    {
        path: 'algorithm/:id/delete',
        component: AlgorithmDeletePopupComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
