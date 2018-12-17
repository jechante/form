import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    UserMatchComponent,
    UserMatchDetailComponent,
    UserMatchUpdateComponent,
    UserMatchDeletePopupComponent,
    UserMatchDeleteDialogComponent,
    userMatchRoute,
    userMatchPopupRoute
} from './';

const ENTITY_STATES = [...userMatchRoute, ...userMatchPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserMatchComponent,
        UserMatchDetailComponent,
        UserMatchUpdateComponent,
        UserMatchDeleteDialogComponent,
        UserMatchDeletePopupComponent
    ],
    entryComponents: [UserMatchComponent, UserMatchUpdateComponent, UserMatchDeleteDialogComponent, UserMatchDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormUserMatchModule {}
