import axios from 'axios'

interface FetchParams {
    page?: number;
    pageSize?: number;
    url: string;
    method: string;  // post/get
    data?: object;
}

export function createAxiosFetch(url: string, method?: string) {
    const httpMethod = method || 'get';

    return (params: FetchParams) : Promise<any> => {
        return new Promise((resolve, reject) => {
            console.log(params)
            axios({
                method: httpMethod,
                url: url,
                params: {
                    page: (params.page ? params.page - 1 : 0),
                    size: params.pageSize,
                },
                data: params.data,
            }).then(function (response) {
                resolve(response.data.result);
            }).catch(function (error) {
                console.log(error);
                reject(error)
            });
        });
    } 
}