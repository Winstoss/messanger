export function SSETransformStream() {
  let buffer: string;

  return new TransformStream<string, any>({
    start() {
      buffer = "";
    },

    transform(chunk, controller) {
      buffer += chunk;
      while (true) {
        const endIndex = buffer.indexOf("\n\n");
        if (endIndex == -1) break;
        const eventStr = buffer.substring(0, endIndex);
        controller.enqueue(JSON.parse(eventStr.substring(5)));
        buffer = buffer.substring(endIndex + 2);
      }
    },

  });
}
