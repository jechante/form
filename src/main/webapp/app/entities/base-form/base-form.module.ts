import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    BaseFormComponent,
    BaseFormDetailComponent,
    BaseFormUpdateComponent,
    BaseFormDeletePopupComponent,
    BaseFormDeleteDialogComponent,
    baseFormRoute,
    baseFormPopupRoute
} from './';

const ENTITY_STATES = [...baseFormRoute, ...baseFormPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BaseFormComponent,
        BaseFormDetailComponent,
        BaseFormUpdateComponent,
        BaseFormDeleteDialogComponent,
        BaseFormDeletePopupComponent
    ],
    entryComponents: [BaseFormComponent, BaseFormUpdateComponent, BaseFormDeleteDialogComponent, BaseFormDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormBaseFormModule {}
