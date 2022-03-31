import { messageType } from "./Util/messageType";
import { SSETransformStream } from "./Util/SSETransformStream";

type RegistrationRequest = {
  username: string;
  password: string;
  nickname: string;
  phoneNumber: string;
  bio: string;
};

export type ServerError = {
  message: string;
  timestamp: string;
};

export function isServerError<T>(response: T | ServerError): response is ServerError {
  return "message" in response && "timestamp" in response;
}

export async function registerUser(user: RegistrationRequest): Promise<true | ServerError> {
  const response = await fetch(`http://localhost:8080/user/registration`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify(user),
  });

  return await response.json();
}

type LoginUserResponse = {
  token: string;
  userId: string;
  avatarPath: string | null;
  nickname: string;
  bio: string | null;
  phoneNumber: string | null;
};

export async function loginUser(
  username: string,
  password: string
): Promise<LoginUserResponse | ServerError> {
  const response = await fetch(`http://localhost:8080/login`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  return response.json();
}

export type UserSearchEntry = {
  userId: string;
  avatarPath: string | null;
  nickname: string;
};

export async function searchForChats(
  query: string,
  token: string
): Promise<UserSearchEntry[] | ServerError> {
  const response = await fetch(`http://localhost:8080/user/find?q=${encodeURIComponent(query)}`, {
    headers: { authorization: "Bearer " + token },
  });
  return response.json();
}

export type UserPatch = {
  nickname?: string;
  password?: string;
  bio?: string;
  phoneNumber?: string;
  image?: File;
};

export async function updateUserData(patch: UserPatch, token: string) {
  const response = await fetch(`http://localhost:8080/user/me`, {
    method: "PATCH",
    headers: { authorization: "Bearer " + token },
    body: formPatchBody(patch),
  });
  return response.json();
}

const hasValue = <T>(
  entry: [key: string, value: T | undefined]
): entry is [key: string, value: T] => entry[1] !== undefined;

function formPatchBody(patch: UserPatch) {
  const body = new FormData();

  for (const [key, value] of Object.entries(patch).filter(hasValue)) {
    body.append(key, value);
  }

  return body;
}

export type ChatType = "private" | "group";

export type Chat = {
  chatId: string;
  type: ChatType;
  chatImage: string | null;
  chatName: string;
};

export async function fetchChats(token: string): Promise<ServerError | Chat[]> {
  const response = await fetch(`http://localhost:8080/chats`, {
    headers: { authorization: "Bearer " + token },
  });
  return response.json();
}

type MessageDeletedEvent = {
  messageId: string;
  deleted: true;
};

export type ChatEvent = Message | MessageDeletedEvent;

export function isDeletedMessageEvent(event: ChatEvent): event is MessageDeletedEvent {
  return "deleted" in event && event.deleted;
}

export async function chatEventStream(
  { type, chatId }: ChatID,
  token: string,
  signal?: AbortSignal
): Promise<ReadableStream<ChatEvent>> {
  const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/sse-stream`, {
    signal: signal,
    method: "POST",
    headers: { authorization: "Bearer " + token },
  });

  if (!response.body) {
    throw new Error("SSE stream does not contain body");
  }

  return response.body
    .pipeThrough(new TextDecoderStream("utf-8"))
    .pipeThrough(SSETransformStream());
}

export type ChatID = {
  type: ChatType;
  chatId: string;
};

export async function fetchMessages({ type, chatId }: ChatID, token: string) {
  const promise = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/`, {
    method: "GET",
    headers: { authorization: "Bearer " + token },
  });
  return await promise.json();
}

export async function fetchChat(
  { type, chatId }: ChatID,
  token: string
): Promise<Chat | ServerError> {
  const promise = await fetch(`http://localhost:8080/chats/${type}/${chatId}`, {
    method: "GET",
    headers: { authorization: "Bearer " + token, Accept: "application/json" },
  });
  return await promise.json();
}

export type MessageBase = {
  messageId: string;
  nickname: string;
};

export type TextMessage = MessageBase & {
  text: string;
  file: null;
};

export type FileMessage = MessageBase & {
  text: null;
  file: string;
};

export type DescribedMessage = MessageBase & {
  text: string;
  file: string;
};

export type Message = TextMessage | FileMessage | DescribedMessage;

export type MessageID = Pick<Message, "messageId" | "file" | "text">;

export async function deleteMessage(
  chat: ChatID,
  message: MessageID,
  token: string
): Promise<true | ServerError> {
  const response = await fetch(
    `http://localhost:8080/chats/${chat.type}/${chat.chatId}/messages/${message.messageId}`,
    {
      method: "DELETE",
      headers: { authorization: "Bearer " + token, type: messageType(message) },
    }
  );

  return response.json();
}

type EditMessageOptions = {
  chat: ChatID;
  message: MessageID;
  newContents?: string;
  newAttachments?: File;
  token: string;
};

export async function editMessage({
  chat,
  message,
  newContents,
  newAttachments,
  token,
}: EditMessageOptions) {
  const response = await fetch(
    `http://localhost:8080/chats/${chat.type}/${chat.chatId}/messages/${message.messageId}`,
    {
      method: "PATCH",
      headers: { authorization: "Bearer " + token, type: messageType(message) },
      body: composeMessageBody(newContents, newAttachments),
    }
  );
  return response.json();
}

type SendMessageOptions = {
  chat: ChatID;
  contents?: string;
  attachments?: File;
  token: string;
};

function composeMessageBody(contents?: string, attachments?: File) {
  const body = new FormData();
  if (contents) body.append("text", contents);
  if (attachments) body.append("file", attachments);
  return body;
}

export async function sendMessage({ chat, contents, attachments, token }: SendMessageOptions) {
  const response = await fetch(
    `http://localhost:8080/chats/${chat.type}/${chat.chatId}/messages/`,
    {
      method: "POST",
      headers: {
        authorization: "Bearer " + token,
        type: messageType({ text: contents, file: attachments }),
      },
      body: composeMessageBody(contents, attachments),
    }
  );
  return response.json();
}

export async function createPrivateChat(withUserID: string, token: string) {
  const response = await fetch(`http://localhost:8080/chats/private/${withUserID}`, {
    method: "POST",
    headers: { authorization: "Bearer " + token },
  });
  return await response.json();
}
