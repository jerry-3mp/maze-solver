import { useState, useEffect } from 'react';
import ApiFactory from '../../api/apiFactory';
import Typography from "@mui/material/Typography";

const StatusComponent = () => {
  const [status, setStatus] = useState<string>('Loading...');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const statusApi = ApiFactory.getStatusApi();
        const response = await statusApi.getStatus();
        setStatus(response.data || 'Status OK');
        setError(null);
      } catch (err) {
        console.error('Error fetching status:', err);
        setStatus('Error');
        setError('Failed to connect to the backend server');
      }
    };

    fetchStatus();
  }, []);

  return (
      <Typography variant="h6">
        {status}
        {error && <p className="error-message" style={{ color: 'red' }}>{error}</p>}
      </Typography>
  );
};

export default StatusComponent;
