import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    FormFieldComponent,
    FormFieldDetailComponent,
    FormFieldUpdateComponent,
    FormFieldDeletePopupComponent,
    FormFieldDeleteDialogComponent,
    formFieldRoute,
    formFieldPopupRoute
} from './';

const ENTITY_STATES = [...formFieldRoute, ...formFieldPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FormFieldComponent,
        FormFieldDetailComponent,
        FormFieldUpdateComponent,
        FormFieldDeleteDialogComponent,
        FormFieldDeletePopupComponent
    ],
    entryComponents: [FormFieldComponent, FormFieldUpdateComponent, FormFieldDeleteDialogComponent, FormFieldDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormFormFieldModule {}
