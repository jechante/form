export interface IBaseProperty {
    id?: number;
    propertyID?: string;
    propertyName?: string;
    propertyDescribe?: string;
    propertyType?: string;
    propertyScore?: number;
    formyType?: string;
    completionRate?: number;
}

export class BaseProperty implements IBaseProperty {
    constructor(
        public id?: number,
        public propertyID?: string,
        public propertyName?: string,
        public propertyDescribe?: string,
        public propertyType?: string,
        public propertyScore?: number,
        public formyType?: string,
        public completionRate?: number
    ) {}
}
