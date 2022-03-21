export function debounce<Args extends any[]>(func: (...args: Args) => void, timeout: number) {
  let timeoutId: number | undefined;
  return (...args: Args) => {
    const later = () => {
      timeoutId = undefined;
      func(...args);
    };
    clearTimeout(timeoutId);
    timeoutId = setTimeout(later, timeout);
  };
}
