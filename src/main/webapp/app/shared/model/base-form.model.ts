import { IFormSubmit } from 'app/shared/model//form-submit.model';
import { IFormField } from 'app/shared/model//form-field.model';

export const enum FormVendor {
    SELF = 'SELF',
    JIN = 'JIN',
    OTHER = 'OTHER'
}

export interface IBaseForm {
    id?: number;
    formCode?: string;
    formName?: string;
    formDescribe?: string;
    formWeb?: string;
    submitUrl?: string;
    vendor?: FormVendor;
    enabled?: boolean;
    submits?: IFormSubmit[];
    fields?: IFormField[];
}

export class BaseForm implements IBaseForm {
    constructor(
        public id?: number,
        public formCode?: string,
        public formName?: string,
        public formDescribe?: string,
        public formWeb?: string,
        public submitUrl?: string,
        public vendor?: FormVendor,
        public enabled?: boolean,
        public submits?: IFormSubmit[],
        public fields?: IFormField[]
    ) {
        this.enabled = this.enabled || false;
    }
}
