export interface IBaseForm {
    id?: number;
    formID?: number;
    formName?: string;
    formDescribe?: string;
    formWeb?: string;
}

export class BaseForm implements IBaseForm {
    constructor(
        public id?: number,
        public formID?: number,
        public formName?: string,
        public formDescribe?: string,
        public formWeb?: string
    ) {}
}
