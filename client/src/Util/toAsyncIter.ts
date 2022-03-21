export function toAsyncIter<T>(stream: ReadableStream<T>) {
  const reader = stream.getReader();
  return {
    [Symbol.asyncIterator]: () => ({
      next: () => reader.read(),
    }),
  };
}
