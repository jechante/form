import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    MatchRecordComponent,
    MatchRecordDetailComponent,
    MatchRecordUpdateComponent,
    MatchRecordDeletePopupComponent,
    MatchRecordDeleteDialogComponent,
    matchRecordRoute,
    matchRecordPopupRoute
} from './';

const ENTITY_STATES = [...matchRecordRoute, ...matchRecordPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MatchRecordComponent,
        MatchRecordDetailComponent,
        MatchRecordUpdateComponent,
        MatchRecordDeleteDialogComponent,
        MatchRecordDeletePopupComponent
    ],
    entryComponents: [MatchRecordComponent, MatchRecordUpdateComponent, MatchRecordDeleteDialogComponent, MatchRecordDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormMatchRecordModule {}
