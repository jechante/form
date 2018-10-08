import { NgModule } from '@angular/core';

import { FormSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FormSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FormSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FormSharedCommonModule {}
