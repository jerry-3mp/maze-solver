import { Configuration } from './configuration';
import {
  StatusControllerApi,
  MazeControllerApi
} from './api';
import { getApiConfig } from '../config/apiConfig';

// Private API instances (singleton pattern)
let statusApiInstance: StatusControllerApi | null = null;
let mazeApiInstance: MazeControllerApi | null = null;

/**
 * Reset all API instances - useful for testing or when authentication changes
 */
export const resetApiInstances = (): void => {
  statusApiInstance = null;
  mazeApiInstance = null;
};

/**
 * Factory for creating API client instances
 * Uses singleton pattern to ensure only one instance is created per API type
 */
export const ApiFactory = {
  /**
   * Get a StatusControllerApi instance (singleton)
   * @param config Optional configuration to override the environment config
   * @returns StatusControllerApi instance
   */
  getStatusApi(config?: Configuration): StatusControllerApi {
    if (!statusApiInstance) {
      const configuration = config || new Configuration(getApiConfig());
      statusApiInstance = new StatusControllerApi(configuration);
    }
    return statusApiInstance;
  },

  /**
   * Get a MazeControllerApi instance (singleton)
   * @param config Optional configuration to override the environment config
   * @returns MazeControllerApi instance
   */
  getMazeApi(config?: Configuration): MazeControllerApi {
    if (!mazeApiInstance) {
      const configuration = config || new Configuration(getApiConfig());
      mazeApiInstance = new MazeControllerApi(configuration);
    }
    return mazeApiInstance;
  }
};

export default ApiFactory;
