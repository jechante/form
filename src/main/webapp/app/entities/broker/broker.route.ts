import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Broker } from 'app/shared/model/broker.model';
import { BrokerService } from './broker.service';
import { BrokerComponent } from './broker.component';
import { BrokerDetailComponent } from './broker-detail.component';
import { BrokerUpdateComponent } from './broker-update.component';
import { BrokerDeletePopupComponent } from './broker-delete-dialog.component';
import { IBroker } from 'app/shared/model/broker.model';

@Injectable({ providedIn: 'root' })
export class BrokerResolve implements Resolve<IBroker> {
    constructor(private service: BrokerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((broker: HttpResponse<Broker>) => broker.body));
        }
        return of(new Broker());
    }
}

export const brokerRoute: Routes = [
    {
        path: 'broker',
        component: BrokerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Brokers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'broker/:id/view',
        component: BrokerDetailComponent,
        resolve: {
            broker: BrokerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brokers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'broker/new',
        component: BrokerUpdateComponent,
        resolve: {
            broker: BrokerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brokers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'broker/:id/edit',
        component: BrokerUpdateComponent,
        resolve: {
            broker: BrokerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brokers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brokerPopupRoute: Routes = [
    {
        path: 'broker/:id/delete',
        component: BrokerDeletePopupComponent,
        resolve: {
            broker: BrokerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brokers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
