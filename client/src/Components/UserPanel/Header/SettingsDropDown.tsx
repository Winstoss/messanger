import * as DropdownMenu from "@radix-ui/react-dropdown-menu";

type Props = {
  onSettings?: () => void;
  onLogout?: () => void;
};

export function SettingsDropDown({ onSettings, onLogout }: Props) {
  return (
    <DropdownMenu.Root>
      <DropdownMenu.Trigger />
      <DropdownMenu.Content>
        <DropdownMenu.Item onSelect={onSettings}>Settings</DropdownMenu.Item>
        <DropdownMenu.Item onSelect={onLogout}>Logout</DropdownMenu.Item>
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  );
}
