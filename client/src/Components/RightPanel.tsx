import { Routes, Route, useParams } from "react-router";
import { ChatWindow } from "./ChatWindow";

export function RightPanel() {
  return (
    <div style={{ background: "salmon" }}>
      <Routes>
        <Route path="/:type/:chatId" element={<ChatWindowArgs />} />
        <Route path="*" element="No chat selected" />
      </Routes>
    </div>
  );
}

function ChatWindowArgs() {
  const { chatId, type } = useParams<{ type: string; chatId: string }>();
  if (type !== "private" && type !== "group") throw new Error(`Unsupported chat type ${type}`);
  return <ChatWindow chatId={{ chatId: chatId!, type: type! }} />;
}
