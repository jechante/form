export interface IFormToProperty {
    id?: number;
    formID?: number;
    keyName?: string;
    keyType?: string;
    propertyId?: string;
}

export class FormToProperty implements IFormToProperty {
    constructor(public id?: number, public formID?: number, public keyName?: string, public keyType?: string, public propertyId?: string) {}
}
