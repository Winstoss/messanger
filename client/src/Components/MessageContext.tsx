import { PropsWithChildren } from "react";
import * as ContextMenu from "@radix-ui/react-context-menu";

type Props = {
  onEdit?: () => void;
  onDelete?: () => void;
};

export function MessageContext({ children, onEdit, onDelete }: PropsWithChildren<Props>) {
  return (
    <ContextMenu.Root>
      <ContextMenu.Trigger>{children}</ContextMenu.Trigger>
      <ContextMenu.Content>
        <ContextMenu.Item onSelect={onEdit}>Edit</ContextMenu.Item>
        <ContextMenu.Item onSelect={onDelete}>Delete</ContextMenu.Item>
      </ContextMenu.Content>
    </ContextMenu.Root>
  );
}
