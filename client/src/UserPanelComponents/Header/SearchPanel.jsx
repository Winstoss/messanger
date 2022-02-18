export function SearchPanel({ query, onQueryChange, onFocusChange }) {
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
