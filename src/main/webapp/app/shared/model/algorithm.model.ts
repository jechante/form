export interface IAlgorithm {
    id?: number;
    algorithmID?: string;
    filterStrings?: string;
    scoreStrings?: string;
    genderWeight?: number;
    remark?: string;
}

export class Algorithm implements IAlgorithm {
    constructor(
        public id?: number,
        public algorithmID?: string,
        public filterStrings?: string,
        public scoreStrings?: string,
        public genderWeight?: number,
        public remark?: string
    ) {}
}
