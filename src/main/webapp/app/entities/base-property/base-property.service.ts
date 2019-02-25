import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBaseProperty } from 'app/shared/model/base-property.model';

type EntityResponseType = HttpResponse<IBaseProperty>;
type EntityArrayResponseType = HttpResponse<IBaseProperty[]>;

@Injectable({ providedIn: 'root' })
export class BasePropertyService {
    private resourceUrl = SERVER_API_URL + 'api/base-properties';
    private findAllUrl = SERVER_API_URL + 'api/base-properties-all';
    private userPropertyDemandsUrl = SERVER_API_URL + 'api/user-properties-demands';

    constructor(private http: HttpClient) {}

    create(baseProperty: IBaseProperty): Observable<EntityResponseType> {
        return this.http.post<IBaseProperty>(this.resourceUrl, baseProperty, { observe: 'response' });
    }

    update(baseProperty: IBaseProperty): Observable<EntityResponseType> {
        return this.http.put<IBaseProperty>(this.resourceUrl, baseProperty, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBaseProperty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBaseProperty[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    findAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBaseProperty[]>(this.findAllUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findUserPropertyDemands(id: string): Observable<EntityArrayResponseType> {
        return this.http.get<IBaseProperty[]>(`${this.userPropertyDemandsUrl}/${id}`, { observe: 'response' });
    }

    saveUserPropertyDemands(baseProperties: IBaseProperty[]) {
        return this.http.post<IBaseProperty[]>(this.userPropertyDemandsUrl, baseProperties, { observe: 'response' });
    }
}
