import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    AlgorithmComponent,
    AlgorithmDetailComponent,
    AlgorithmUpdateComponent,
    AlgorithmDeletePopupComponent,
    AlgorithmDeleteDialogComponent,
    algorithmRoute,
    algorithmPopupRoute
} from './';

const ENTITY_STATES = [...algorithmRoute, ...algorithmPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AlgorithmComponent,
        AlgorithmDetailComponent,
        AlgorithmUpdateComponent,
        AlgorithmDeleteDialogComponent,
        AlgorithmDeletePopupComponent
    ],
    entryComponents: [AlgorithmComponent, AlgorithmUpdateComponent, AlgorithmDeleteDialogComponent, AlgorithmDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormAlgorithmModule {}
