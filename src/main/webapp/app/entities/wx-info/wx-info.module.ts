import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    WxInfoComponent,
    WxInfoDetailComponent,
    WxInfoUpdateComponent,
    WxInfoDeletePopupComponent,
    WxInfoDeleteDialogComponent,
    wxInfoRoute,
    wxInfoPopupRoute
} from './';

const ENTITY_STATES = [...wxInfoRoute, ...wxInfoPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [WxInfoComponent, WxInfoDetailComponent, WxInfoUpdateComponent, WxInfoDeleteDialogComponent, WxInfoDeletePopupComponent],
    entryComponents: [WxInfoComponent, WxInfoUpdateComponent, WxInfoDeleteDialogComponent, WxInfoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormWxInfoModule {}
