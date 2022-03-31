import React from "react";

type Props = {
  message: string;
  editing?: boolean;
  onAttachFile?: (file?: File) => void;
  onSend?: () => void;
  onMessageChange?: (newMessage: string) => void;
  onCancelEditing?: () => void;
  attachedFile?: File | null;
};

export function MessagePanel({
  onSend,
  message,
  editing,
  onAttachFile,
  onMessageChange,
  onCancelEditing,
}: Props) {
  const handleFileChange =
    onAttachFile &&
    ((e: React.ChangeEvent<HTMLInputElement>) => {
      onAttachFile(e.target.files?.item(0) || undefined);
    });

  const handleMessageChange =
    onMessageChange &&
    ((e: React.ChangeEvent<HTMLInputElement>) => {
      onMessageChange(e.target.value);
    });

  return (
    <div>
      <form onSubmit={onSend}>
        <input
          type="text"
          placeholder="input here..."
          name="text"
          value={message}
          onChange={handleMessageChange}
        />
        <input type="file" name="file" onChange={handleFileChange} />
        <input type="submit" value="Send" />
        {editing && <button value="cancel editing" onClick={onCancelEditing} />}
      </form>
    </div>
  );
}
