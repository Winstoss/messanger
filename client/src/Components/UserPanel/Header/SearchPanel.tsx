type Props = {
  query: string;
  onQueryChange: (query: string) => void;
  onFocusChange: (inFocus: boolean) => void;
};

export function SearchPanel({ query, onQueryChange, onFocusChange }: Props) {
  return (
    <div>
      <input
        type="text"
        placeholder="Search"
        value={query}
        onFocus={() => onFocusChange(true)}
        onBlur={() => onFocusChange(false)}
        onChange={(e) => {
          onQueryChange(e.target.value);
        }}
      />
    </div>
  );
}
