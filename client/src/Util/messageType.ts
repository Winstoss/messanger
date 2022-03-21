import { Message } from "../api";

type MessageTypeOptions = {
  file?: string | File | null;
  text?: string | null;
};

export function messageType({ file, text }: MessageTypeOptions) {
  if (!file) return "text";
  if (!text) return "file";
  return "described";
}
