import { useEffect, useRef } from 'react';

type FetchDataFunction = () => Promise<void>;

const usePolling = (fetchData: FetchDataFunction, interval: number) => {
  const pollingInterval = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    const startPolling = () => {
      const intervalId = setInterval(() => {
        fetchData();
      }, interval);
      pollingInterval.current = intervalId;
    };

    startPolling();

    return () => {
      if (pollingInterval.current) {
        clearInterval(pollingInterval.current);
      }
    };
  }, [fetchData, interval]);
};

export default usePolling;
