export interface IFormSubmit {
    id?: number;
    submitID?: string;
    submitSource?: string;
    formID?: number;
    formName?: string;
    submitJosn?: string;
    userID?: string;
    registerChannel?: string;
    submitDate?: number;
    submitTime?: number;
    dealflag?: string;
}

export class FormSubmit implements IFormSubmit {
    constructor(
        public id?: number,
        public submitID?: string,
        public submitSource?: string,
        public formID?: number,
        public formName?: string,
        public submitJosn?: string,
        public userID?: string,
        public registerChannel?: string,
        public submitDate?: number,
        public submitTime?: number,
        public dealflag?: string
    ) {}
}
