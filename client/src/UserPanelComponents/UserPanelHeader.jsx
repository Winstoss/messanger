import { SearchPanel } from "./Header/SearchPanel";
import { SettingsDropDown } from "./Header/SettingsDropDown";

export function UserPanelHeader({ query, onQueryChange, onFocusChange, onSettings, onLogout }) {
  return (
    <div>
      <div>{<SettingsDropDown onSettings={onSettings} onLogout={onLogout} />}</div>
      <div>
        <SearchPanel query={query} onQueryChange={onQueryChange} onFocusChange={onFocusChange} />
      </div>
    </div>
  );
}
