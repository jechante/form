import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBaseForm } from 'app/shared/model/base-form.model';

@Component({
    selector: 'jhi-base-form-detail',
    templateUrl: './base-form-detail.component.html'
})
export class BaseFormDetailComponent implements OnInit {
    baseForm: IBaseForm;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ baseForm }) => {
            this.baseForm = baseForm;
        });
    }

    previousState() {
        window.history.back();
    }
}
