import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';

@Component({
    selector: 'jhi-algorithm-update',
    templateUrl: './algorithm-update.component.html'
})
export class AlgorithmUpdateComponent implements OnInit {
    private _algorithm: IAlgorithm;
    isSaving: boolean;

    constructor(private algorithmService: AlgorithmService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            this.algorithm = algorithm;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.algorithm.id !== undefined) {
            this.subscribeToSaveResponse(this.algorithmService.update(this.algorithm));
        } else {
            this.subscribeToSaveResponse(this.algorithmService.create(this.algorithm));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAlgorithm>>) {
        result.subscribe((res: HttpResponse<IAlgorithm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get algorithm() {
        return this._algorithm;
    }

    set algorithm(algorithm: IAlgorithm) {
        this._algorithm = algorithm;
    }
}
