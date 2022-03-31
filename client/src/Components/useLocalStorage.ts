import { useEffect } from "react";

export function useLocalStorage(key: string, value: string | null) {
  useEffect(() => {
    if (value) {
      localStorage.setItem(key, value);
    } else {
      localStorage.removeItem(key);
    }
  }, [key, value]);
}
