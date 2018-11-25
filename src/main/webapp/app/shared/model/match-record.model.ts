export interface IMatchRecord {
    id?: number;
    recordID?: string;
    userA?: string;
    userB?: string;
    algorithmID?: string;
    initiatorType?: string;
    scoreAtoB?: number;
    scoreBtoA?: number;
    scoreTotal?: number;
    genderWeight?: number;
    propertyA?: string;
    propertyB?: string;
}

export class MatchRecord implements IMatchRecord {
    constructor(
        public id?: number,
        public recordID?: string,
        public userA?: string,
        public userB?: string,
        public algorithmID?: string,
        public initiatorType?: string,
        public scoreAtoB?: number,
        public scoreBtoA?: number,
        public scoreTotal?: number,
        public genderWeight?: number,
        public propertyA?: string,
        public propertyB?: string
    ) {}
}
