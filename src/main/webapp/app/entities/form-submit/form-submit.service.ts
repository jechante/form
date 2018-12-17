import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFormSubmit } from 'app/shared/model/form-submit.model';

type EntityResponseType = HttpResponse<IFormSubmit>;
type EntityArrayResponseType = HttpResponse<IFormSubmit[]>;

@Injectable({ providedIn: 'root' })
export class FormSubmitService {
    private resourceUrl = SERVER_API_URL + 'api/form-submits';

    constructor(private http: HttpClient) {}

    create(formSubmit: IFormSubmit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(formSubmit);
        return this.http
            .post<IFormSubmit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(formSubmit: IFormSubmit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(formSubmit);
        return this.http
            .put<IFormSubmit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFormSubmit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFormSubmit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(formSubmit: IFormSubmit): IFormSubmit {
        const copy: IFormSubmit = Object.assign({}, formSubmit, {
            createdDateTime:
                formSubmit.createdDateTime != null && formSubmit.createdDateTime.isValid() ? formSubmit.createdDateTime.toJSON() : null,
            updatedDateTime:
                formSubmit.updatedDateTime != null && formSubmit.updatedDateTime.isValid() ? formSubmit.updatedDateTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdDateTime = res.body.createdDateTime != null ? moment(res.body.createdDateTime) : null;
        res.body.updatedDateTime = res.body.updatedDateTime != null ? moment(res.body.updatedDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((formSubmit: IFormSubmit) => {
            formSubmit.createdDateTime = formSubmit.createdDateTime != null ? moment(formSubmit.createdDateTime) : null;
            formSubmit.updatedDateTime = formSubmit.updatedDateTime != null ? moment(formSubmit.updatedDateTime) : null;
        });
        return res;
    }
}
