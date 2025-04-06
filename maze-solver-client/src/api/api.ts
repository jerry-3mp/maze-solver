/* tslint:disable */
/* eslint-disable */
/**
 * Maze Solver API
 * API for generating and solving mazes
 *
 * The version of the OpenAPI document: v1.0
 * Contact: support@example.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import type { Configuration } from './configuration';
import type { AxiosPromise, AxiosInstance, RawAxiosRequestConfig } from 'axios';
import globalAxios from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from './common';
import type { RequestArgs } from './base';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, BaseAPI, RequiredError, operationServerMap } from './base';

/**
 * Maze generation parameters
 * @export
 * @interface MazeGenerationRequestDTO
 */
export interface MazeGenerationRequestDTO {
    /**
     * Width of the maze (number of columns)
     * @type {number}
     * @memberof MazeGenerationRequestDTO
     */
    'width'?: number;
    /**
     * Height of the maze (number of rows)
     * @type {number}
     * @memberof MazeGenerationRequestDTO
     */
    'height'?: number;
}
/**
 * Detailed information about a maze including its grid and solution path if solved
 * @export
 * @interface MazeResponseDTO
 */
export interface MazeResponseDTO {
    /**
     * Unique identifier for the maze
     * @type {number}
     * @memberof MazeResponseDTO
     */
    'id'?: number;
    /**
     * Grid representation of the maze as a list of strings. \'s\'=start, \'e\'=end, \'w\'=wall, \'p\'=path, \' \'=empty
     * @type {Array<string>}
     * @memberof MazeResponseDTO
     */
    'grid'?: Array<string>;
    /**
     * Whether the maze has been solved
     * @type {boolean}
     * @memberof MazeResponseDTO
     */
    'solved'?: boolean;
    /**
     * Sequence of positions forming the solution path (null if not solved)
     * @type {Array<PositionDTO>}
     * @memberof MazeResponseDTO
     */
    'solvedPath'?: Array<PositionDTO>;
}
/**
 * Summary information about a maze
 * @export
 * @interface MazeSummaryDTO
 */
export interface MazeSummaryDTO {
    /**
     * Unique identifier for the maze
     * @type {number}
     * @memberof MazeSummaryDTO
     */
    'id'?: number;
    /**
     * Timestamp when the maze was created
     * @type {string}
     * @memberof MazeSummaryDTO
     */
    'createdAt'?: string;
    /**
     * Timestamp when the maze was last updated
     * @type {string}
     * @memberof MazeSummaryDTO
     */
    'updatedAt'?: string;
    /**
     * Whether the maze has been solved
     * @type {boolean}
     * @memberof MazeSummaryDTO
     */
    'solved'?: boolean;
}
/**
 * Paginated list of maze summaries
 * @export
 * @interface MazeSummaryListResponse
 */
export interface MazeSummaryListResponse {
    /**
     * List of maze summaries in the current page
     * @type {Array<MazeSummaryDTO>}
     * @memberof MazeSummaryListResponse
     */
    'content'?: Array<MazeSummaryDTO>;
    /**
     * Total number of mazes
     * @type {number}
     * @memberof MazeSummaryListResponse
     */
    'totalElements'?: number;
    /**
     * Total number of pages
     * @type {number}
     * @memberof MazeSummaryListResponse
     */
    'totalPages'?: number;
    /**
     * Number of items per page
     * @type {number}
     * @memberof MazeSummaryListResponse
     */
    'size'?: number;
    /**
     * Current page number (0-based)
     * @type {number}
     * @memberof MazeSummaryListResponse
     */
    'number'?: number;
}
/**
 * Represents a position in a maze grid
 * @export
 * @interface PositionDTO
 */
export interface PositionDTO {
    /**
     * Row index (0-based)
     * @type {number}
     * @memberof PositionDTO
     */
    'row'?: number;
    /**
     * Column index (0-based)
     * @type {number}
     * @memberof PositionDTO
     */
    'col'?: number;
}

/**
 * MazeControllerApi - axios parameter creator
 * @export
 */
export const MazeControllerApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Creates a new random maze with the specified dimensions
         * @summary Generate a random maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        generateRandomMaze: async (mazeGenerationRequestDTO: MazeGenerationRequestDTO, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'mazeGenerationRequestDTO' is not null or undefined
            assertParamExists('generateRandomMaze', 'mazeGenerationRequestDTO', mazeGenerationRequestDTO)
            const localVarPath = `/api/v1/mazes`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(mazeGenerationRequestDTO, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Creates a new random perfect maze with the specified dimensions
         * @summary Generate a random perfect maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        generateRandomPerfectMaze: async (mazeGenerationRequestDTO: MazeGenerationRequestDTO, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'mazeGenerationRequestDTO' is not null or undefined
            assertParamExists('generateRandomPerfectMaze', 'mazeGenerationRequestDTO', mazeGenerationRequestDTO)
            const localVarPath = `/api/v1/mazes/perfect`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(mazeGenerationRequestDTO, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Returns detailed information about a specific maze
         * @summary Get maze by ID
         * @param {number} id ID of maze to retrieve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getMaze: async (id: number, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            assertParamExists('getMaze', 'id', id)
            const localVarPath = `/api/v1/mazes/{id}`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Returns a paginated list of maze summaries
         * @summary Get all mazes with pagination
         * @param {number} [page] Page number (0-based)
         * @param {number} [size] Page size
         * @param {string} [sort] Sort field
         * @param {string} [direction] Sort direction
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getMazes: async (page?: number, size?: number, sort?: string, direction?: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/v1/mazes`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }

            if (size !== undefined) {
                localVarQueryParameter['size'] = size;
            }

            if (sort !== undefined) {
                localVarQueryParameter['sort'] = sort;
            }

            if (direction !== undefined) {
                localVarQueryParameter['direction'] = direction;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Attempts to solve the specified maze and returns the solution
         * @summary Solve a maze
         * @param {number} id ID of maze to solve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        solveMaze: async (id: number, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            assertParamExists('solveMaze', 'id', id)
            const localVarPath = `/api/v1/mazes/{id}/solve`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * MazeControllerApi - functional programming interface
 * @export
 */
export const MazeControllerApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = MazeControllerApiAxiosParamCreator(configuration)
    return {
        /**
         * Creates a new random maze with the specified dimensions
         * @summary Generate a random maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async generateRandomMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<MazeResponseDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.generateRandomMaze(mazeGenerationRequestDTO, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['MazeControllerApi.generateRandomMaze']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Creates a new random perfect maze with the specified dimensions
         * @summary Generate a random perfect maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async generateRandomPerfectMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<MazeResponseDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.generateRandomPerfectMaze(mazeGenerationRequestDTO, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['MazeControllerApi.generateRandomPerfectMaze']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Returns detailed information about a specific maze
         * @summary Get maze by ID
         * @param {number} id ID of maze to retrieve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getMaze(id: number, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<MazeResponseDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getMaze(id, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['MazeControllerApi.getMaze']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Returns a paginated list of maze summaries
         * @summary Get all mazes with pagination
         * @param {number} [page] Page number (0-based)
         * @param {number} [size] Page size
         * @param {string} [sort] Sort field
         * @param {string} [direction] Sort direction
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getMazes(page?: number, size?: number, sort?: string, direction?: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<MazeSummaryListResponse>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getMazes(page, size, sort, direction, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['MazeControllerApi.getMazes']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Attempts to solve the specified maze and returns the solution
         * @summary Solve a maze
         * @param {number} id ID of maze to solve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async solveMaze(id: number, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<MazeResponseDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.solveMaze(id, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['MazeControllerApi.solveMaze']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * MazeControllerApi - factory interface
 * @export
 */
export const MazeControllerApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = MazeControllerApiFp(configuration)
    return {
        /**
         * Creates a new random maze with the specified dimensions
         * @summary Generate a random maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        generateRandomMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig): AxiosPromise<MazeResponseDTO> {
            return localVarFp.generateRandomMaze(mazeGenerationRequestDTO, options).then((request) => request(axios, basePath));
        },
        /**
         * Creates a new random perfect maze with the specified dimensions
         * @summary Generate a random perfect maze
         * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        generateRandomPerfectMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig): AxiosPromise<MazeResponseDTO> {
            return localVarFp.generateRandomPerfectMaze(mazeGenerationRequestDTO, options).then((request) => request(axios, basePath));
        },
        /**
         * Returns detailed information about a specific maze
         * @summary Get maze by ID
         * @param {number} id ID of maze to retrieve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getMaze(id: number, options?: RawAxiosRequestConfig): AxiosPromise<MazeResponseDTO> {
            return localVarFp.getMaze(id, options).then((request) => request(axios, basePath));
        },
        /**
         * Returns a paginated list of maze summaries
         * @summary Get all mazes with pagination
         * @param {number} [page] Page number (0-based)
         * @param {number} [size] Page size
         * @param {string} [sort] Sort field
         * @param {string} [direction] Sort direction
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getMazes(page?: number, size?: number, sort?: string, direction?: string, options?: RawAxiosRequestConfig): AxiosPromise<MazeSummaryListResponse> {
            return localVarFp.getMazes(page, size, sort, direction, options).then((request) => request(axios, basePath));
        },
        /**
         * Attempts to solve the specified maze and returns the solution
         * @summary Solve a maze
         * @param {number} id ID of maze to solve
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        solveMaze(id: number, options?: RawAxiosRequestConfig): AxiosPromise<MazeResponseDTO> {
            return localVarFp.solveMaze(id, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * MazeControllerApi - object-oriented interface
 * @export
 * @class MazeControllerApi
 * @extends {BaseAPI}
 */
export class MazeControllerApi extends BaseAPI {
    /**
     * Creates a new random maze with the specified dimensions
     * @summary Generate a random maze
     * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MazeControllerApi
     */
    public generateRandomMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig) {
        return MazeControllerApiFp(this.configuration).generateRandomMaze(mazeGenerationRequestDTO, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Creates a new random perfect maze with the specified dimensions
     * @summary Generate a random perfect maze
     * @param {MazeGenerationRequestDTO} mazeGenerationRequestDTO 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MazeControllerApi
     */
    public generateRandomPerfectMaze(mazeGenerationRequestDTO: MazeGenerationRequestDTO, options?: RawAxiosRequestConfig) {
        return MazeControllerApiFp(this.configuration).generateRandomPerfectMaze(mazeGenerationRequestDTO, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Returns detailed information about a specific maze
     * @summary Get maze by ID
     * @param {number} id ID of maze to retrieve
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MazeControllerApi
     */
    public getMaze(id: number, options?: RawAxiosRequestConfig) {
        return MazeControllerApiFp(this.configuration).getMaze(id, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Returns a paginated list of maze summaries
     * @summary Get all mazes with pagination
     * @param {number} [page] Page number (0-based)
     * @param {number} [size] Page size
     * @param {string} [sort] Sort field
     * @param {string} [direction] Sort direction
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MazeControllerApi
     */
    public getMazes(page?: number, size?: number, sort?: string, direction?: string, options?: RawAxiosRequestConfig) {
        return MazeControllerApiFp(this.configuration).getMazes(page, size, sort, direction, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Attempts to solve the specified maze and returns the solution
     * @summary Solve a maze
     * @param {number} id ID of maze to solve
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MazeControllerApi
     */
    public solveMaze(id: number, options?: RawAxiosRequestConfig) {
        return MazeControllerApiFp(this.configuration).solveMaze(id, options).then((request) => request(this.axios, this.basePath));
    }
}



/**
 * StatusControllerApi - axios parameter creator
 * @export
 */
export const StatusControllerApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getStatus: async (options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/api/status`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * StatusControllerApi - functional programming interface
 * @export
 */
export const StatusControllerApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = StatusControllerApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getStatus(options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<string>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getStatus(options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['StatusControllerApi.getStatus']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * StatusControllerApi - factory interface
 * @export
 */
export const StatusControllerApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = StatusControllerApiFp(configuration)
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getStatus(options?: RawAxiosRequestConfig): AxiosPromise<string> {
            return localVarFp.getStatus(options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * StatusControllerApi - object-oriented interface
 * @export
 * @class StatusControllerApi
 * @extends {BaseAPI}
 */
export class StatusControllerApi extends BaseAPI {
    /**
     * 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof StatusControllerApi
     */
    public getStatus(options?: RawAxiosRequestConfig) {
        return StatusControllerApiFp(this.configuration).getStatus(options).then((request) => request(this.axios, this.basePath));
    }
}



