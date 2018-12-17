import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormField } from 'app/shared/model/form-field.model';

@Component({
    selector: 'jhi-form-field-detail',
    templateUrl: './form-field-detail.component.html'
})
export class FormFieldDetailComponent implements OnInit {
    formField: IFormField;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ formField }) => {
            this.formField = formField;
        });
    }

    previousState() {
        window.history.back();
    }
}
