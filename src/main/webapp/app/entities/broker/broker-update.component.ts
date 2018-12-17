import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBroker } from 'app/shared/model/broker.model';
import { BrokerService } from './broker.service';

@Component({
    selector: 'jhi-broker-update',
    templateUrl: './broker-update.component.html'
})
export class BrokerUpdateComponent implements OnInit {
    private _broker: IBroker;
    isSaving: boolean;

    constructor(private brokerService: BrokerService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ broker }) => {
            this.broker = broker;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.broker.id !== undefined) {
            this.subscribeToSaveResponse(this.brokerService.update(this.broker));
        } else {
            this.subscribeToSaveResponse(this.brokerService.create(this.broker));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBroker>>) {
        result.subscribe((res: HttpResponse<IBroker>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get broker() {
        return this._broker;
    }

    set broker(broker: IBroker) {
        this._broker = broker;
    }
}
