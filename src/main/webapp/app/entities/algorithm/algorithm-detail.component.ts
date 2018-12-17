import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlgorithm } from 'app/shared/model/algorithm.model';

@Component({
    selector: 'jhi-algorithm-detail',
    templateUrl: './algorithm-detail.component.html'
})
export class AlgorithmDetailComponent implements OnInit {
    algorithm: IAlgorithm;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            this.algorithm = algorithm;
        });
    }

    previousState() {
        window.history.back();
    }
}
