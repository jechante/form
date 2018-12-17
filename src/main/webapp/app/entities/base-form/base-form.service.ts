import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBaseForm } from 'app/shared/model/base-form.model';

type EntityResponseType = HttpResponse<IBaseForm>;
type EntityArrayResponseType = HttpResponse<IBaseForm[]>;

@Injectable({ providedIn: 'root' })
export class BaseFormService {
    private resourceUrl = SERVER_API_URL + 'api/base-forms';

    constructor(private http: HttpClient) {}

    create(baseForm: IBaseForm): Observable<EntityResponseType> {
        return this.http.post<IBaseForm>(this.resourceUrl, baseForm, { observe: 'response' });
    }

    update(baseForm: IBaseForm): Observable<EntityResponseType> {
        return this.http.put<IBaseForm>(this.resourceUrl, baseForm, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBaseForm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBaseForm[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
