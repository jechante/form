import { IUserProperty } from 'app/shared/model//user-property.model';
import { IUserDemand } from 'app/shared/model//user-demand.model';
import { IFormField } from 'app/shared/model//form-field.model';
import { IAlgorithm } from 'app/shared/model//algorithm.model';

export const enum PropertyType {
    BASIC = 'BASIC',
    VALUES = 'VALUES',
    CHARACTER = 'CHARACTER',
    LOOKS = 'LOOKS',
    ASSET = 'ASSET',
    INTEREST = 'INTEREST'
}

export const enum FormyType {
    ONETOONE = 'ONETOONE',
    ONETOMANY = 'ONETOMANY',
    MANYTOMANY = 'MANYTOMANY',
    ONETORANGE = 'ONETORANGE',
    LOCATION = 'LOCATION',
    SCHOOL = 'SCHOOL',
    OTHER = 'OTHER'
}

export interface IBaseProperty {
    id?: number;
    propertyName?: string;
    propertyDescribe?: string;
    propertyType?: PropertyType;
    propertyScore?: number;
    propertyMaxScore?: number;
    formyType?: FormyType;
    completionRate?: number;
    serialNumber?: number;
    propertyValues?: IUserProperty[];
    demandValues?: IUserDemand[];
    fields?: IFormField[];
    filterAlgorithms?: IAlgorithm[];
    scoreAlgorithms?: IAlgorithm[];
}

export class BaseProperty implements IBaseProperty {
    constructor(
        public id?: number,
        public propertyName?: string,
        public propertyDescribe?: string,
        public propertyType?: PropertyType,
        public propertyScore?: number,
        public propertyMaxScore?: number,
        public formyType?: FormyType,
        public completionRate?: number,
        public serialNumber?: number,
        public propertyValues?: IUserProperty[],
        public demandValues?: IUserDemand[],
        public fields?: IFormField[],
        public filterAlgorithms?: IAlgorithm[],
        public scoreAlgorithms?: IAlgorithm[]
    ) {}
}
