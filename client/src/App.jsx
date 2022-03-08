import { Routes, Route } from "react-router";
import { BrowserRouter } from "react-router-dom";
import Login from "./Login";
import { LeftPanel } from "./LeftPanel";
import { RightPanel } from "./RightPanel"
import "./App.css";
import { LoginPage } from "./LoginPage";

function AppShell() {
  return (
    <div className="app-container">
      <LeftPanel />
      <RightPanel />
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<AppShell />} />
      </Routes>
    </BrowserRouter>
  );
}
