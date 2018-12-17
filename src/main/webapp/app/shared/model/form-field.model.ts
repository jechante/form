import { IBaseForm } from 'app/shared/model//base-form.model';
import { IBaseProperty } from 'app/shared/model//base-property.model';

export const enum FieldType {
    PROPERTY = 'PROPERTY',
    DEMAND = 'DEMAND'
}

export interface IFormField {
    id?: number;
    fieldName?: string;
    fieldType?: FieldType;
    baseForm?: IBaseForm;
    baseProperty?: IBaseProperty;
}

export class FormField implements IFormField {
    constructor(
        public id?: number,
        public fieldName?: string,
        public fieldType?: FieldType,
        public baseForm?: IBaseForm,
        public baseProperty?: IBaseProperty
    ) {}
}
