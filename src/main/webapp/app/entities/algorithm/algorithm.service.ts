import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAlgorithm } from 'app/shared/model/algorithm.model';

type EntityResponseType = HttpResponse<IAlgorithm>;
type EntityArrayResponseType = HttpResponse<IAlgorithm[]>;

@Injectable({ providedIn: 'root' })
export class AlgorithmService {
    private resourceUrl = SERVER_API_URL + 'api/algorithms';

    constructor(private http: HttpClient) {}

    create(algorithm: IAlgorithm): Observable<EntityResponseType> {
        return this.http.post<IAlgorithm>(this.resourceUrl, algorithm, { observe: 'response' });
    }

    update(algorithm: IAlgorithm): Observable<EntityResponseType> {
        return this.http.put<IAlgorithm>(this.resourceUrl, algorithm, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAlgorithm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAlgorithm[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
