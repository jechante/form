import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFormField } from 'app/shared/model/form-field.model';

type EntityResponseType = HttpResponse<IFormField>;
type EntityArrayResponseType = HttpResponse<IFormField[]>;

@Injectable({ providedIn: 'root' })
export class FormFieldService {
    private resourceUrl = SERVER_API_URL + 'api/form-fields';

    constructor(private http: HttpClient) {}

    create(formField: IFormField): Observable<EntityResponseType> {
        return this.http.post<IFormField>(this.resourceUrl, formField, { observe: 'response' });
    }

    update(formField: IFormField): Observable<EntityResponseType> {
        return this.http.put<IFormField>(this.resourceUrl, formField, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFormField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFormField[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
