/**
 * API configuration settings with Vite environment mode detection
 */

/**
 * Available environment modes
 */
type EnvironmentMode = 'development' |
    // 'staging' |
    'production';

/**
 * Get the current environment mode
 * @returns The current environment mode
 */
export const getEnvironmentMode = (): EnvironmentMode => {
  // Use Vite's environment mode variable
  const mode = import.meta.env.MODE as EnvironmentMode;
  console.log(`Current environment mode: ${mode}`);
  return mode;
};

/**
 * Get environment-specific API configuration
 * @returns Configuration object with environment-specific settings
 */
export const getApiConfig = () => {
  const mode = getEnvironmentMode();
  
  // Base path configuration for different environments
  const basePaths: Record<EnvironmentMode, string> = {
    development: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    // staging: import.meta.env.VITE_API_BASE_URL || 'https://staging-api.example.com',
    production: import.meta.env.VITE_API_BASE_URL || 'https://api.example.com'
  };
  
  return {
    basePath: basePaths[mode] || basePaths.development,
  };
};

export default getApiConfig;
