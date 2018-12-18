import { IUserMatch } from 'app/shared/model//user-match.model';
import { IBaseProperty } from 'app/shared/model//base-property.model';

export interface IAlgorithm {
    id?: number;
    name?: string;
    genderWeight?: number;
    kValue?: number;
    remark?: string;
    enabled?: boolean;
    matches?: IUserMatch[];
    filterProperties?: IBaseProperty[];
    scoreProperties?: IBaseProperty[];
}

export class Algorithm implements IAlgorithm {
    constructor(
        public id?: number,
        public name?: string,
        public genderWeight?: number,
        public kValue?: number,
        public remark?: string,
        public enabled?: boolean,
        public matches?: IUserMatch[],
        public filterProperties?: IBaseProperty[],
        public scoreProperties?: IBaseProperty[]
    ) {
        this.enabled = this.enabled || false;
    }
}
