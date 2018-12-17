import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    PushRecordComponent,
    PushRecordDetailComponent,
    PushRecordUpdateComponent,
    PushRecordDeletePopupComponent,
    PushRecordDeleteDialogComponent,
    pushRecordRoute,
    pushRecordPopupRoute
} from './';

const ENTITY_STATES = [...pushRecordRoute, ...pushRecordPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PushRecordComponent,
        PushRecordDetailComponent,
        PushRecordUpdateComponent,
        PushRecordDeleteDialogComponent,
        PushRecordDeletePopupComponent
    ],
    entryComponents: [PushRecordComponent, PushRecordUpdateComponent, PushRecordDeleteDialogComponent, PushRecordDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormPushRecordModule {}
