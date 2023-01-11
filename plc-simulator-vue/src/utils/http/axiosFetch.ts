import axios from 'axios'

interface FetchParams {
    page?: number;
    pageSize?: number;
    url: string;
    method: string;  // post/get
}

export function createAxiosFetch(url: string, method?: string) {
    const httpMethod = method && 'get';
    return (params: FetchParams) : Promise<any> => {
        return new Promise((resolve, reject) => {
            axios({
                method: httpMethod,
                url: url,
                params: {
                    page: (params.page ? params.page - 1 : 0),
                    size: params.pageSize,
                    space: 'bixintech',
                }
            }).then(function (response) {
                resolve(response.data);
            }).catch(function (error) {
                console.log(error);
                reject(error)
            });
        });
    } 
}