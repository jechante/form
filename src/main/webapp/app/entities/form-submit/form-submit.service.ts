import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<IFormSubmit>(this.resourceUrl, formSubmit, { observe: 'response' });
    }

    update(formSubmit: IFormSubmit): Observable<EntityResponseType> {
        return this.http.put<IFormSubmit>(this.resourceUrl, formSubmit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFormSubmit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFormSubmit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
